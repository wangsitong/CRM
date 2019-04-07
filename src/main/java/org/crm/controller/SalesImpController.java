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
import org.crm.model.entity.Sales;
import org.crm.service.SalesService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/business/sales/imp")
public class SalesImpController {

    @Autowired
    private SalesService salesService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object imp(@RequestParam("file") MultipartFile file) throws Exception {
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
        } catch (Exception e) {
            // e.printStackTrace();
            try {
                workbook = new XSSFWorkbook(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();

        List<Sales> dataList = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            for (int i = 3; i <= rowCount; i++) {
                Row row = sheet.getRow(i);

                String salesDate = POIUtils.getStringCellValue(row.getCell(0));
                String customerId = POIUtils.getStringCellValue(row.getCell(1));
                String customerName = POIUtils.getStringCellValue(row.getCell(2));
                String salesOil = POIUtils.getStringCellValue(row.getCell(3));
                String managerId = POIUtils.getStringCellValue(row.getCell(4));
                String managerName = POIUtils.getStringCellValue(row.getCell(5));
                String salesStation = POIUtils.getStringCellValue(row.getCell(6));
                double salesCount = Double.valueOf((String) POIUtils.getCellValue(row.getCell(7)));
                double salesPrice = Double.valueOf((String) POIUtils.getCellValue(row.getCell(8)));
                String salesChannel = POIUtils.getStringCellValue(row.getCell(9));

                Sales sales = new Sales();
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
                    return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, validate);
                }

                dataList.add(sales);
            }
        } catch (Exception e) {

        } finally {
            workbook.close();
        }

        try {
            this.salesService.save(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error("数据保存失败, " + e.getMessage());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", dataList.size());

        return ResponseUtils.getResult(ResponseUtils.STATUS_SUCCESS, resultMap);
    }

    private Map<String, Object> validate(Sales sales) {
        String[] salesChannels = new String[] { "直销", "分销", "零售" };
        Map<String, Object> validateMap = new HashMap<>();
        List<String> errors = new ArrayList<>();
        validateMap.put("errors", errors);
//        if (StringUtils.isNotBlank(sales.getSalesChannel()) || !ArrayUtils.contains(salesChannels, sales.getSalesChannel())) {
//            errors.add("销售渠道不正确");
//        }

        validateMap.put("status", errors.isEmpty());
        return validateMap;
    }

}
