package com.brielmayer.teda.parser;

import com.brielmayer.teda.exception.ParseException;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.model.Header;
import com.google.common.math.DoubleMath;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BeanParser {
    public static Bean parse(XSSFSheet sheet, String beanId) {

        CellAddress beanAddress = findCell(beanId, sheet);
        if(beanAddress == null) {
            String error =
                    "\nError while parsing Bean" +
                    "\nUnable to find %s in %s";
            throw new ParseException(error, beanId, sheet.getSheetName());
        }

        String beanName = sheet.getRow(beanAddress.getRow()).getCell(beanAddress.getColumn() + 1).getStringCellValue();

        // get header
        List<Header> header = new ArrayList<>();
        XSSFRow columnRow = sheet.getRow(beanAddress.getRow() + 1);
        for (byte c = 1;; c++) {
            XSSFCell cell = columnRow.getCell(beanAddress.getColumn() + c);
            if (cell != null && getCellValue(cell) != null) {
                // header must be a string
                String value = (String) getCellValue(cell);
                // primary keys start with a hash tag
                header.add(new Header(value.replace("#", ""), value.startsWith("#")));
                continue;
            }
            // if empty column is reached, break
            break;
        }

        // get data
        List<Map<String, Object>> data = new ArrayList<>();
        for (int r = 2;; r++) {
            XSSFRow xssfRow = sheet.getRow(beanAddress.getRow() + r);
            if (xssfRow == null) {
                // end of table reached
                break;
            }

            Map<String, Object> row = new LinkedHashMap<>();
            for (byte c = 0; c < header.size(); c++) {
                XSSFCell cell = xssfRow.getCell(beanAddress.getColumn() + c + 1);
                if(cell == null) {
                    row.put(header.get(c).getName(), "");
                } else {
                    row.put(header.get(c).getName(), getCellValue(cell));
                }
            }

            if(isEmptyRow(row)) {
                // end of table reached
                break;
            } else {
                data.add(row);
            }
        }
        return new Bean(beanName, header, data);
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                // if cell is a date
                    return cell.getDateCellValue();
                } else if (DoubleMath.isMathematicalInteger(cell.getNumericCellValue())) {
                    // if cell is a int / long
                    return (long)cell.getNumericCellValue();
                } else {
                    // else return double
                    return cell.getNumericCellValue();
                }
            case XSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case XSSFCell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            // BLANK or ERROR
            default:
                return "";
        }
    }

    private static CellAddress findCell(String needle, XSSFSheet haystack) {
        // only search first 100 rows
        for (int r = 0; r < 100; r++) {
            XSSFRow row = haystack.getRow(r);
            if (row != null) {
                // only search first 100 columns
                for (int c = 0; c < 100; c++) {
                    XSSFCell cell = row.getCell(c);

                    if (cell != null && getCellValue(cell).equals(needle)) {
                        return cell.getAddress();
                    }
                }
            }
        }
        return null;
    }

    // row is empty if all values are empty
    private static boolean isEmptyRow(Map<String, Object> row) {
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if(entry.getValue() != null && !entry.getValue().toString().equals("")) {
                return false;
            }
        }
        return true;
    }
}


