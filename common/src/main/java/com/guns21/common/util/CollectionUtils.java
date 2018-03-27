package com.guns21.common.util;

import java.util.List;
import java.util.function.UnaryOperator;

public class CollectionUtils {


    public static void replaceAll(List list, UnaryOperator operator) {
        if (list != null) {
            list.replaceAll(operator);
        }
    }
}
