package org.crm.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.crm.common.POIUtils;
import org.crm.common.ResponseUtils;
import org.crm.model.dto.SalesDTO;
import org.crm.model.entity.Customer;
import org.crm.model.entity.Sales;
import org.crm.model.entity.Station;
import org.crm.service.CustomerService;
import org.crm.service.SalesService;
import org.crm.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/business/sales/imp")
public class SalesImpController {

    @Autowired
    private SalesService salesService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StationService stationService;

    private List<String> transferStations = new ArrayList<>();

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object imp(@RequestParam("file") MultipartFile file) throws Exception {
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
        } catch (Exception e) {
            // e.printStackTrace();
            try {
                workbook = new XSSFWorkbook(file.getInputStream());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (workbook == null) {
            throw new Exception("文件解析错误!");
        }

        List<Sales> dataList = new ArrayList<>();
        try {
            this.parseDatas(workbook, dataList);
            dataList = this.salesService.saveImportDatas(dataList);
        } catch (DataParseException e) {
            return ResponseUtils.getResult(ResponseUtils.STATUS_ERROR, e.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error("数据保存失败, " + e.getMessage());
        } finally {
            workbook.close();
        }

        try {
            this.setTransfer(dataList);
        } catch (Exception e) {
            return ResponseUtils.error("数据保存失败, " + e.getMessage());
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", dataList.size());

        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, resultMap);
    }

    private List<String> getTransferStation() {
        List<String> stationIds = new ArrayList<>();
        Station condition = new Station();
        condition.setTransfer("1");
        try {
            List<Station> dataList = this.stationService.getAll(condition);
            dataList.forEach(data -> stationIds.add(data.getStationId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stationIds;
    }

    private Date parseDatas(Workbook workbook, List<Sales> dataList) throws Exception {
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date minDate = new Date();

        for (int i = 3; i <= rowCount; i++) {
            Row row = sheet.getRow(i);

            String salesChannel = POIUtils.getStringCellValue(row.getCell(0));
            String salesDate = POIUtils.getStringCellValue(row.getCell(1));
            String customerId = POIUtils.getStringCellValue(row.getCell(2));
            String customerName = POIUtils.getStringCellValue(row.getCell(3));
            String salesOil = POIUtils.getStringCellValue(row.getCell(4));
            String managerId = POIUtils.getStringCellValue(row.getCell(5));
            String managerName = POIUtils.getStringCellValue(row.getCell(6));
            String salesStation = POIUtils.getStringCellValue(row.getCell(7));
            double salesCount = Double.valueOf((String) POIUtils.getCellValue(row.getCell(8)));
            double salesPrice = Double.valueOf((String) POIUtils.getCellValue(row.getCell(9)));

            Sales sales = new Sales();
            sales.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            sales.setSalesDate(df.parse(salesDate));
            sales.setCustomerId(customerId);
            sales.setCustomerName(customerName);
            sales.setSalesOil(salesOil);
            sales.setManagerId(managerId);
            sales.setManagerName(managerName);
            sales.setSalesStation(salesStation);
            sales.setSalesCount(salesCount);
            sales.setSalesPrice(salesPrice);
            sales.setSalesChannel(salesChannel);

            Map<String, Object> validate = this.validate(sales);
            boolean status = (boolean) validate.get("status");
            if (!status) {
                validate.put("row", i);
                // return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, validate);
                throw new DataParseException("", validate);
            }

            if (sales.getSalesDate().before(minDate)) {
                minDate = sales.getSalesDate();
            }

            dataList.add(sales);
        }
        return minDate;
    }

    private Map<String, Object> validate(Sales sales) {
        String[] salesChannels = new String[] { "直销", "分销", "零售" };
        Map<String, Object> validateMap = new HashMap<>();
        List<String> errors = new ArrayList<>();
        validateMap.put("errors", errors);
        if (StringUtils.isBlank(sales.getSalesChannel()) || !ArrayUtils.contains(salesChannels, sales.getSalesChannel())) {
            errors.add("销售渠道不正确");
        }

        validateMap.put("status", errors.isEmpty());
        return validateMap;
    }

    private void setTransfer(List<Sales> dataList) {
        try {
            this.transferStations = this.getTransferStation();

            Map<String, Customer> cacheMap = new HashMap<>();
            for (Sales sales : dataList) {
                if (!this.transferStations.contains(sales.getSalesStation())) {
                    continue;
                }
                sales.setTransfer("1");
                Customer customer = cacheMap.get(sales.getCustomerId());
                if (customer == null) {
                    customer = this.customerService.getByCode(sales.getCustomerId());
                    cacheMap.put(sales.getCustomerId(), customer);
                }
                if (customer != null) {
                    sales.setOriginalManagerId(customer.getManagerId());
                    sales.setOriginalManagerName(customer.getManagerName());
                }
            }
            this.salesService.saveOrUpdate(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}


class DataParseException extends Exception {

    private Object data;

    public Object getData() {
        return data;
    }

    public DataParseException(String message, Object data) {
        super(message);
        this.data = data;
    }

}