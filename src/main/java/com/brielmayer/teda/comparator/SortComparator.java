package com.brielmayer.teda.comparator;

import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.model.Header;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SortComparator implements Comparator<Map<String, Object>> {

    private List<Header> primaryKeys;

    public SortComparator(List<Header> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    @Override
    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        for (Header key : primaryKeys) {

            // check if both objects have the same data type
            if (!o1.getClass().getSimpleName().equals(o2.getClass().getSimpleName())) {
                String error =
                        "\nUnable to sort data" +
                        "\nData types are not equal within primary key \"%s\"" +
                        "\nCan not sort by comparing \"%s\" with \"%s\"";
                throw new TedaException(error, key.getName(), o1.getClass().getSimpleName(), o2.getClass().getSimpleName());
            }

            int result = o1.toString().compareTo(o2.toString());
            if (result == 0) {
                // sort result not unique
                // so continue sorting by next primary key
                continue;
            } else {
                return result;
            }
        }
        String error =
                "\nUnable to sort data" +
                "\nPrimary keys are not unique when using" +
                "\nORDER BY %s";
        throw new TedaException(error, primaryKeys.toString());
    }
}
