package com.brielmayer.teda.parser;

import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.model.Header;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum BeanParser {
    ;

    public static Bean parse(final XSSFSheet sheet, final String beanId) {

        final CellAddress beanAddress = findCell(beanId, sheet);
        if (beanAddress == null) {
            throw TedaException.builder()
                    .appendMessage("Error while parsing Bean")
                    .appendMessage("Unable to find %s in %s", beanId, sheet.getSheetName())
                    .build();
        }

        final String beanName = sheet.getRow(beanAddress.getRow()).getCell(beanAddress.getColumn() + 1).getStringCellValue();

        // get header
        final List<Header> header = new ArrayList<>();
        final XSSFRow columnRow = sheet.getRow(beanAddress.getRow() + 1);
        for (byte c = 1; ; c++) {
            final XSSFCell cell = columnRow.getCell(beanAddress.getColumn() + c);
            if (cell != null && getCellValue(cell) != null) {
                // header must be a string
                final String value = (String) getCellValue(cell);
                // primary keys start with a hash tag
                header.add(new Header(value.replace("#", ""), value.startsWith("#")));
                continue;
            }
            // if empty column is reached, break
            break;
        }

        // get data
        final List<Map<String, Object>> data = new ArrayList<>();
        for (int r = 2; ; r++) {
            final XSSFRow xssfRow = sheet.getRow(beanAddress.getRow() + r);
            if (xssfRow == null) {
                // end of table reached
                break;
            }

            final Map<String, Object> row = new LinkedHashMap<>();
            for (byte c = 0; c < header.size(); c++) {
                final XSSFCell cell = xssfRow.getCell(beanAddress.getColumn() + c + 1);
                if (cell == null) {
                    row.put(header.get(c).getName(), "");
                } else {
                    row.put(header.get(c).getName(), getCellValue(cell));
                }
            }

            if (isEmptyRow(row)) {
                // end of table reached
                break;
            } else {
                data.add(row);
            }
        }
        return new Bean(beanName, header, data);
    }

    private static Object getCellValue(final Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // if cell is a date
                    return cell.getDateCellValue();
                } else if (isMathematicalInteger(cell.getNumericCellValue())) {
                    // if cell is a int / long
                    return (long) cell.getNumericCellValue();
                } else {
                    // else return double
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            // BLANK or ERROR
            default:
                return "";
        }
    }

    private static boolean isMathematicalInteger(final double x) {
        return !Double.isNaN(x) && !Double.isInfinite(x) && x == Math.rint(x);
    }

    private static CellAddress findCell(final String needle, final XSSFSheet haystack) {
        // only search first 100 rows
        for (int r = 0; r < 100; r++) {
            final XSSFRow row = haystack.getRow(r);
            if (row != null) {
                // only search first 100 columns
                for (int c = 0; c < 100; c++) {
                    final XSSFCell cell = row.getCell(c);

                    if (cell != null && getCellValue(cell).equals(needle)) {
                        return cell.getAddress();
                    }
                }
            }
        }
        return null;
    }

    // row is empty if all values are empty
    private static boolean isEmptyRow(final Map<String, Object> row) {
        for (final Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().toString().equals("")) {
                return false;
            }
        }
        return true;
    }
}


