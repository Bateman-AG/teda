package com.brielmayer.teda.handler;

import com.brielmayer.teda.comparator.ObjectComparator;
import com.brielmayer.teda.comparator.SortComparator;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.model.Header;
import com.brielmayer.teda.parser.BeanParser;
import com.brielmayer.teda.parser.TypeParser;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestHandler {

    private static final String TABLE_BEAN = "#Table";

    public static void test(BaseDatabase database, XSSFSheet sheet) {

        Bean expectedBean = BeanParser.parse(sheet, TABLE_BEAN);
        Bean actualBean = new Bean(
                    expectedBean.getBeanName(),
                    expectedBean.getHeader(),
                    database.select(expectedBean.getBeanName(), expectedBean.getHeader())
        );

        // sort data
        List<Header> primaryKeys = expectedBean.getHeader().stream().filter(Header::isPrimaryKey).collect(Collectors.toList());
        Collections.sort(expectedBean.getData(), new SortComparator(primaryKeys));
        Collections.sort(actualBean.getData(), new SortComparator(primaryKeys));

        // compare data
        compare(expectedBean, actualBean);
    }

    private static void compare(Bean excelBean, Bean actualBean) {

        // check number of rows
        if (excelBean.getData().size() != actualBean.getData().size()) {
            String error =
                    "\nFailed to compare data for bean %s" +
                    "\nNumber of rows are not equal" +
                    "\nExpected number of rows: %d" +
                    "\nActual number of rows: %d" +
                    "\n\n" +
                    "\nActual:" +
                    "\n%s" +
                    "\nExpected:" +
                    "\n%s";
            throw new TedaException(error, excelBean.getBeanName(), excelBean.getData().size(), actualBean.getData().size(), listToString(actualBean.getData()), listToString(excelBean.getData()));
        }

        // compare line by line
        for (int rowCount = 0; rowCount < excelBean.getData().size(); rowCount++) {

            Map<String, Object> expectedRow = excelBean.getData().get(rowCount);
            Map<String, Object> actualRow = actualBean.getData().get(rowCount);

            for (Map.Entry<String, Object> entry : expectedRow.entrySet()) {
                String key = entry.getKey();

                Object actualValue = TypeParser.parse(actualRow.get(key));
                Object expectedValue = TypeParser.parse(expectedRow.get(key));

                if(!ObjectComparator.getInstance().compare(actualValue, expectedValue)) {
                    String errorMessage =
                            "\nError comparing Bean %s in row %d" +
                            "\nColumn %s: Expected (%s)\"%s\" != Actual (%s)\"%s\"" +
                            "\nExpected Row:  %s" +
                            "\nActual Row:    %s";
                    throw new TedaException(errorMessage,
                            excelBean.getBeanName(), rowCount + 1,
                            key, expectedValue.getClass().getSimpleName(), expectedValue, actualValue.getClass().getSimpleName(), actualValue,
                            expectedRow.toString(),
                            actualRow.toString());
                }
            }
        }
    }

    // only used in case of an exception
    private static String listToString(List<Map<String, Object>> data) {
        String retVal = "";
        for (Map<String, Object> row : data) {
            retVal += String.format("%s\n", row.toString());
        }
        return retVal;
    }

}
