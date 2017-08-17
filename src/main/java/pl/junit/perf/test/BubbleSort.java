package pl.junit.perf.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pliszewski on 16.08.2017.
 */
public class BubbleSort {

    public static List<Integer> sort(List<Integer> toSort) {
        List<Integer> list = new ArrayList<>(toSort);
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
        return list;
    }
}
