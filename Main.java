import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/*
 * Лабораторная работа №4. Вариант 2.
 * 1 - 1,3; 2 - 2; 3 - 1-4.
*/
public class Main {
    public static void main(String[] args) {
        System.out.println();

        Task1.run();
        System.out.println();

        Task2.run();
        System.out.println();

        Task3.run();
    }
}


class Box<T> {
    private T item;

    public Box() {
    }

    public Box(T item) {
        put(item);
    }

    public void put(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Нельзя положить в коробку null");
        }
        if (this.item != null) {
            throw new IllegalStateException("Коробка уже заполнена!");
        }
        this.item = item;
    }

    public T get() {
        T result = item;
        item = null;
        return result;
    }

    public boolean isFull() {
        return item != null;
    }

    @Override
    public String toString() {
        return isFull() ? "Box{item=" + item + "}" : "Box{пустая}";
    }
}


interface Comparing<T> {
    int compare(T other);
}


class Book implements Comparing<Book> {
    private String title;
    private int pages;

    public Book(String title, int pages) {
        this.title = title;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public int compare(Book other) {
        if (pages > other.pages) {
            return 1;
        }
        if (pages < other.pages) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', pages=" + pages + "}";
    }
}


interface Transformer<T, P> {
    P apply(T value);
}


interface Condition<T> {
    boolean test(T value);
}


interface Combiner<T> {
    T combine(T left, T right);
}


interface ResultFactory<R> {
    R create();
}


interface ResultWriter<R, T> {
    void add(R result, T value);
}


class ListUtils {

    public static <T, P> List<P> transform(List<T> source, Transformer<T, P> transformer) {
        List<P> result = new ArrayList<>();
        for (T item : source) {
            result.add(transformer.apply(item));
        }
        return result;
    }

    public static <T> List<T> filter(List<T> source, Condition<T> condition) {
        List<T> result = new ArrayList<>();
        for (T item : source) {
            if (condition.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T> T reduce(List<T> source, T initial, Combiner<T> combiner) {
        if (initial == null) {
            throw new IllegalArgumentException("Начальное значение не может быть null");
        }
        T result = initial;
        for (T item : source) {
            result = combiner.combine(result, item);
        }
        return result;
    }


    public static <T, R> R collect(List<T> source,
                                   ResultFactory<R> factory,
                                   ResultWriter<R, T> writer) {
        R result = factory.create();
        for (T item : source) {
            writer.add(result, item);
        }
        return result;
    }
}


class Task1 {

    private static void showValue(Box<Integer> box) {
        Integer value = box.get();
        System.out.println("Из коробки достали: " + value);
    }

    public static void run() {
        System.out.println("=== задача 1.1 ===");
        Box<Integer> box = new Box<>();
        System.out.println("Создали коробку: " + box + ", заполнена? " + box.isFull());

        box.put(3);
        System.out.println("Положили число 3: " + box + ", заполнена? " + box.isFull());

        try {
            box.put(5);
        } catch (IllegalStateException e) {
            System.out.println("Попытка положить 5 в полную коробку -> ошибка: " + e.getMessage());
        }

        showValue(box);
        System.out.println("После извлечения: " + box + ", заполнена? " + box.isFull());

        System.out.println();
        System.out.println("=== задача 1.3 ===");
        Book b1 = new Book("Война и мир", 1225);
        Book b2 = new Book("Мцыри", 80);
        System.out.println(b1);
        System.out.println(b2);
        System.out.println("b1.compare(b2) = " + b1.compare(b2) + "  (у первой страниц больше)");
        System.out.println("b2.compare(b1) = " + b2.compare(b1) + " (у второй страниц меньше)");
        System.out.println("b1.compare(b1) = " + b1.compare(b1) + "  (поровну)");
    }
}


class Task2 {

    public static double findMax(List<Box<? extends Number>> boxes) {
        Double max = null;
        for (Box<? extends Number> box : boxes) {
            Double value = readAndRestore(box);
            if (value != null && (max == null || value > max)) {
                max = value;
            }
        }
        if (max == null) {
            throw new IllegalArgumentException("Во всех коробках пусто - максимума нет");
        }
        return max;
    }

    private static <T extends Number> Double readAndRestore(Box<T> box) {
        if (!box.isFull()) {
            return null;
        }
        T value = box.get();
        box.put(value);
        return value.doubleValue();
    }

    public static void run() {
        System.out.println("=== задача 2.2 ===");
        Box<Integer> intBox = new Box<>(3);
        Box<Double> doubleBox = new Box<>(7.5);
        Box<Long> longBox = new Box<>(5L);
        Box<Integer> emptyBox = new Box<>();

        List<Box<? extends Number>> boxes = new ArrayList<>();
        boxes.add(intBox);
        boxes.add(doubleBox);
        boxes.add(longBox);
        boxes.add(emptyBox);

        System.out.println("Коробки: " + boxes);
        System.out.println("Максимальное значение: " + findMax(boxes));
        System.out.println("Коробки после поиска (значения на месте): " + boxes);
    }
}


class Task3 {
    static class StringLength implements Transformer<String, Integer> {
        @Override
        public Integer apply(String value) {
            return value.length();
        }
    }

    static class AbsoluteValue implements Transformer<Integer, Integer> {
        @Override
        public Integer apply(Integer value) {
            return Math.abs(value);
        }
    }

    static class ArrayMax implements Transformer<int[], Integer> {
        @Override
        public Integer apply(int[] values) {
            if (values.length == 0) {
                throw new IllegalArgumentException("У пустого массива нет максимума");
            }
            int max = values[0];
            for (int v : values) {
                if (v > max) {
                    max = v;
                }
            }
            return max;
        }
    }

    static class ListSize implements Transformer<List<Integer>, Integer> {
        @Override
        public Integer apply(List<Integer> value) {
            return value.size();
        }
    }

    static class LengthAtLeastThree implements Condition<String> {
        @Override
        public boolean test(String value) {
            return value.length() >= 3;
        }
    }

    static class NotPositive implements Condition<Integer> {
        @Override
        public boolean test(Integer value) {
            return value <= 0;
        }
    }

    static class NoPositiveInArray implements Condition<int[]> {
        @Override
        public boolean test(int[] values) {
            for (int v : values) {
                if (v > 0) {
                    return false;
                }
            }
            return true;
        }
    }

    static class StringConcat implements Combiner<String> {
        @Override
        public String combine(String left, String right) {
            return left + right;
        }
    }

    static class IntegerSum implements Combiner<Integer> {
        @Override
        public Integer combine(Integer left, Integer right) {
            return left + right;
        }
    }

    static class PosNegFactory implements ResultFactory<List<List<Integer>>> {
        @Override
        public List<List<Integer>> create() {
            List<List<Integer>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            result.add(new ArrayList<>());
            return result;
        }
    }

    static class PosNegWriter implements ResultWriter<List<List<Integer>>, Integer> {
        @Override
        public void add(List<List<Integer>> result, Integer value) {
            if (value > 0) {
                result.get(0).add(value);
            } else if (value < 0) {
                result.get(1).add(value);
            }
        }
    }

    static class LengthGroupsFactory implements ResultFactory<Map<Integer, List<String>>> {
        @Override
        public Map<Integer, List<String>> create() {
            return new TreeMap<>();
        }
    }

    static class LengthGroupsWriter implements ResultWriter<Map<Integer, List<String>>, String> {
        @Override
        public void add(Map<Integer, List<String>> result, String value) {
            int length = value.length();
            if (!result.containsKey(length)) {
                result.put(length, new ArrayList<>());
            }
            result.get(length).add(value);
        }
    }

    static class UniqueFactory implements ResultFactory<Set<String>> {
        @Override
        public Set<String> create() {
            return new LinkedHashSet<>();
        }
    }

    static class UniqueWriter implements ResultWriter<Set<String>, String> {
        @Override
        public void add(Set<String> result, String value) {
            result.add(value);
        }
    }

    private static String arraysToString(List<int[]> arrays) {
        List<String> parts = new ArrayList<>();
        for (int[] a : arrays) {
            parts.add(Arrays.toString(a));
        }
        return parts.toString();
    }

    public static void run() {
        List<String> words = Arrays.asList("qwerty", "asdfg", "zx");
        List<Integer> numbers = Arrays.asList(1, -3, 7);

        System.out.println("=== задача 3.1 (transform) ===");
        System.out.println("Строки " + words + " -> длины: "
                + ListUtils.transform(words, new StringLength()));
        System.out.println("Числа " + numbers + " -> модули: "
                + ListUtils.transform(numbers, new AbsoluteValue()));

        List<int[]> arrays = new ArrayList<>();
        arrays.add(new int[]{1, 5, 3});
        arrays.add(new int[]{-4, -2, -9});
        arrays.add(new int[]{10});
        System.out.println("Массивы " + arraysToString(arrays) + " -> максимумы: "
                + ListUtils.transform(arrays, new ArrayMax()));

        System.out.println();
        System.out.println("=== задача 3.2 (filter) ===");
        System.out.println("Строки " + words + ", убрали строки короче 3 символов: "
                + ListUtils.filter(words, new LengthAtLeastThree()));
        System.out.println("Числа " + numbers + ", убрали положительные: "
                + ListUtils.filter(numbers, new NotPositive()));

        List<int[]> arrays2 = new ArrayList<>();
        arrays2.add(new int[]{1, 5, 3});
        arrays2.add(new int[]{-4, -2, 0});
        arrays2.add(new int[]{2, -3});
        arrays2.add(new int[]{-1});
        System.out.println("Массивы " + arraysToString(arrays2));
        System.out.println("  оставили только те, где нет положительных: "
                + arraysToString(ListUtils.filter(arrays2, new NoPositiveInArray())));

        System.out.println();
        System.out.println("=== задача 3.3 (reduce) ===");
        System.out.println("Строки " + words + " -> одна строка: \""
                + ListUtils.reduce(words, "", new StringConcat()) + "\"");
        System.out.println("Числа " + numbers + " -> сумма: "
                + ListUtils.reduce(numbers, 0, new IntegerSum()));

        List<List<Integer>> lists = new ArrayList<>();
        lists.add(Arrays.asList(1, 2, 3));
        lists.add(Arrays.asList(4, 5));
        lists.add(new ArrayList<Integer>());
        lists.add(Arrays.asList(6));
        List<Integer> sizes = ListUtils.transform(lists, new ListSize());
        int total = ListUtils.reduce(sizes, 0, new IntegerSum());
        System.out.println("Списки " + lists + " -> размеры " + sizes
                + " -> всего элементов: " + total);

        List<Integer> emptyList = new ArrayList<>();
        System.out.println("Пустой список, сумма: "
                + ListUtils.reduce(emptyList, 0, new IntegerSum()) + " (без ошибок и без null)");
        List<String> emptyStrings = new ArrayList<>();
        System.out.println("Пустой список, склейка строк: \""
                + ListUtils.reduce(emptyStrings, "", new StringConcat()) + "\"");

        System.out.println();
        System.out.println("=== задача 3.4 (collect) ===");
        List<List<Integer>> posNeg = ListUtils.collect(numbers, new PosNegFactory(), new PosNegWriter());
        System.out.println("Числа " + numbers);
        System.out.println("  положительные: " + posNeg.get(0));
        System.out.println("  отрицательные: " + posNeg.get(1));

        List<String> words4 = Arrays.asList("qwerty", "asdfg", "zx", "qw");
        Map<Integer, List<String>> groups =
                ListUtils.collect(words4, new LengthGroupsFactory(), new LengthGroupsWriter());
        System.out.println("Строки " + words4 + " по длине:");
        for (Map.Entry<Integer, List<String>> entry : groups.entrySet()) {
            System.out.println("  длина " + entry.getKey() + " -> " + entry.getValue());
        }

        List<String> words5 = Arrays.asList("qwerty", "asdfg", "qwerty", "qw");
        Set<String> unique = ListUtils.collect(words5, new UniqueFactory(), new UniqueWriter());
        System.out.println("Строки " + words5 + " без повторов: " + unique);
    }
}
