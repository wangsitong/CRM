package org.crm.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.crm.common.ResponseUtils;
import org.crm.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/business/sales/imp")
public class SalesImpController {

    @Autowired
    private SalesService salesService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object imp(File file) throws Exception {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (Exception e) {
            // e.printStackTrace();
            workbook = new HSSFWorkbook(new FileInputStream(file));
        }

        if (workbook == null) {
            return ResponseUtils.error("文件解析失败");
        }
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
        }

        return null;
    }

}
