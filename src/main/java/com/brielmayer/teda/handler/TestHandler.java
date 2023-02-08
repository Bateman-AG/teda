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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TestHandler {

    private static final Logger log  = LoggerFactory.getLogger(TestHandler.class);

    private static final String TABLE_BEAN = "#Table";

    public static void test(final BaseDatabase database, final XSSFSheet sheet) {

        final Bean expectedBean = BeanParser.parse(sheet, TABLE_BEAN);
        final Bean actualBean = new Bean(
                expectedBean.getBeanName(),
                expectedBean.getHeader(),
                database.select(expectedBean.getBeanName(), expectedBean.getHeader())
        );

        log.info("Test table: {}", expectedBean.getBeanName());

        // sort data
        final List<Header> primaryKeys = expectedBean.getHeader().stream().filter(Header::isPrimaryKey).collect(Collectors.toList());
        expectedBean.getData().sort(new SortComparator(primaryKeys));
        actualBean.getData().sort(new SortComparator(primaryKeys));

        // compare data
        compare(expectedBean, actualBean);
    }

    private static void compare(final Bean excelBean, final Bean actualBean) {

        // check number of rows
        if (excelBean.getData().size() != actualBean.getData().size()) {
            throw TedaException.builder()
                    .appendMessage("Failed to compare data for bean %s", excelBean.getBeanName())
                    .appendMessage("Number of rows are not equal")
                    .appendMessage("Expected number of rows: %d", excelBean.getData().size())
                    .appendMessage("Actual number of rows: %d", actualBean.getData().size())
                    .appendMessage()
                    .appendMessage("Actual:")
                    .appendMessage("%s", listToString(actualBean.getData()))
                    .appendMessage("Expected:")
                    .appendMessage("%s", listToString(excelBean.getData()))
                    .build();
        }

        // compare line by line
        for (int rowCount = 0; rowCount < excelBean.getData().size(); rowCount++) {

            final Map<String, Object> expectedRow = excelBean.getData().get(rowCount);
            final Map<String, Object> actualRow = actualBean.getData().get(rowCount);

            for (final Map.Entry<String, Object> entry : expectedRow.entrySet()) {
                final String key = entry.getKey();

                final Object actualValue = TypeParser.parse(actualRow.get(key));
                final Object expectedValue = TypeParser.parse(expectedRow.get(key));

                if (!ObjectComparator.compare(actualValue, expectedValue)) {
                    throw TedaException.builder()
                            .appendMessage("Error comparing Bean %s in row %d", excelBean.getBeanName(), rowCount + 1)
                            .appendMessage("Column %s: Expected (%s)\"%s\" != Actual (%s)\"%s\"", key, expectedValue.getClass().getSimpleName(), expectedValue, actualValue.getClass().getSimpleName(), actualValue)
                            .appendMessage("Expected Row:  %s", expectedRow.toString())
                            .appendMessage("Actual Row:    %s", actualRow.toString())
                            .build();
                }
            }
        }
    }

    // only used in case of an exception
    private static String listToString(final List<Map<String, Object>> data) {
        final StringBuilder retVal = new StringBuilder();
        for (final Map<String, Object> row : data) {
            retVal.append(String.format("%s\n", row.toString()));
        }
        return retVal.toString();
    }

}
