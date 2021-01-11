import java.util.*;
import java.util.function.Function;

public class MainTest {
    public static void main(String[] args) {
        MainTest mainTest = new MainTest();
        mainTest.subListProcess(Arrays.asList(1L, 2L, 3L), 2, longs -> new HashSet<>(longs));
    }

    public void subListProcess(List<Long> list, Integer limit, Function<List<Long>, Set<Long>> func) {
        Integer start = 0;
        Integer endIndex = start + limit;
        List<Long> subList;

        if (list.size() <= start) {
            subList = Collections.emptyList();
        }else if (list.size() < endIndex) {
            subList = list.subList(start, list.size());
        }else {
            subList = list.subList(start, endIndex);
        }

        Set<Long> longs = func.apply(subList);
    }
}
