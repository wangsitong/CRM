package org.crm.common;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class POIUtils {

    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    return df.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }

                return NumberToTextConverter.toText(cell.getNumericCellValue());
            case ERROR:
                return cell.getErrorCellValue();
            case BLANK:
                return null;
            case FORMULA:
            case STRING:
                return cell.getStringCellValue();
        }
        return cell.getStringCellValue();
    }

    public static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return (String) getCellValue(cell);
    }

}
