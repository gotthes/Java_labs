import java.util.ArrayList;
import java.util.List;

/** Лабораторная работа №2, вариант 2: задания 1.2, 1.3, 2.2, 3.3, 4.8, 5.5. */
public class Main {

    public static void main(String[] args) {
        task1_2();
        Name[] names = task1_3();
        task2_2(names);
        task3_3();
        task4_8();
        task5_5();
    }

    private static void header(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    private static void task1_2() {
        header("Задание 1.2. Человек");
        Human cleopatra = new Human("Клеопатра", 152);
        Human pushkin = new Human("Пушкин", 167);
        Human vladimir = new Human("Владимир", 189);
        System.out.println(cleopatra);
        System.out.println(pushkin);
        System.out.println(vladimir);
    }

    private static Name[] task1_3() {
        header("Задание 1.3. Имена");
        Name cleopatra = new Name(null, "Клеопатра", null);
        Name pushkin = new Name("Пушкин", "Александр", "Сергеевич");
        Name mayakovsky = new Name("Маяковский", "Владимир", null);
        System.out.println(cleopatra);
        System.out.println(pushkin);
        System.out.println(mayakovsky);
        return new Name[] {cleopatra, pushkin, mayakovsky};
    }

    private static void task2_2(Name[] names) {
        header("Задание 2.2. Человек с именем");
        System.out.println(new Person(names[0], 152));
        System.out.println(new Person(names[1], 167));
        System.out.println(new Person(names[2], 189));
    }

    private static void task3_3() {
        header("Задание 3.3. Города (рис. 2)");
        City a = new City("A");
        City b = new City("B");
        City c = new City("C");
        City d = new City("D");
        City e = new City("E");
        City f = new City("F");

        a.addRoute(f, 1);
        f.addRoute(b, 1);
        twoWay(a, b, 5);
        twoWay(a, d, 6);
        twoWay(b, c, 3);
        twoWay(c, d, 4);
        twoWay(d, e, 2);
        twoWay(e, f, 2);

        printCities(a, b, c, d, e, f);
    }

    private static void twoWay(City x, City y, int cost) {
        x.addRoute(y, cost);
        y.addRoute(x, cost);
    }

    private static void task4_8() {
        header("Задание 4.8. Города V2.");
        City d = new City("D");
        City e = new City("E", new Route(d, 2));
        City c = new City("C", new Route(d, 4));
        City b = new City("B", new Route(c, 3));
        City f = new City("F", new Route(b, 1), new Route(e, 2));
        City a = new City("A", new Route(f, 1), new Route(b, 5), new Route(d, 6));

        d.addRoute(e, 2);
        d.addRoute(c, 4);
        d.addRoute(a, 6);
        c.addRoute(b, 3);
        b.addRoute(a, 5);
        e.addRoute(f, 2);

        printCities(a, b, c, d, e, f);
    }

    private static void printCities(City... cities) {
        for (City city : cities) {
            System.out.println(city);
        }
    }

    private static void task5_5() {
        header("Задание 5.5. Дроби");
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(2, 3);
        System.out.println("a = " + a + ", b = " + b);

        System.out.println("-- операции с дробью --");
        System.out.println(a + " + " + b + " = " + a.sum(b));
        System.out.println(a + " - " + b + " = " + a.minus(b));
        System.out.println(a + " * " + b + " = " + a.mult(b));
        System.out.println(a + " / " + b + " = " + a.div(b));

        System.out.println("-- операции с целым числом --");
        System.out.println(a + " + 2 = " + a.sum(2));
        System.out.println(a + " - 2 = " + a.minus(2));
        System.out.println(a + " * 2 = " + a.mult(2));
        System.out.println(a + " / 2 = " + a.div(2));

        System.out.println("-- исходные дроби не изменились: a = " + a + ", b = " + b);

        System.out.println("-- цепочка f1.sum(f2).div(f3).minus(5) --");
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction f3 = new Fraction(2, 5);
        System.out.println("f1 = " + f1 + ", f2 = " + f2 + ", f3 = " + f3);
        System.out.println("f1.sum(f2).div(f3).minus(5) = " + f1.sum(f2).div(f3).minus(5));
    }
}

/** Задание 1.2. */
class Human {

    private String name;
    private int height;

    public Human(String name, int height) {
        this.name = name;
        this.height = height;
    }

    @Override
    public String toString() {
        return name + ", рост: " + height;
    }
}

/** Задание 1.3. */
class Name {

    private String lastName;
    private String firstName;
    private String patronymic;

    public Name(String lastName, String firstName, String patronymic) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        append(sb, lastName);
        append(sb, firstName);
        append(sb, patronymic);
        return sb.toString();
    }

    private static void append(StringBuilder sb, String part) {
        if (part == null || part.trim().isEmpty()) {
            return;
        }
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(part);
    }
}

/** Задание 2.2. */
class Person {

    private Name name;
    private int height;

    public Person(Name name, int height) {
        this.name = name;
        this.height = height;
    }

    @Override
    public String toString() {
        return name + ", рост: " + height;
    }
}

class Route {

    private City destination;
    private int cost;

    public Route(City destination, int cost) {
        this.destination = destination;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return destination.getName() + ":" + cost;
    }
}

class City {

    private String name;
    private List<Route> routes;

    public City(String name) {
        this.name = name;
        this.routes = new ArrayList<>();
    }

    public City(String name, Route... routes) {
        this(name);
        for (Route route : routes) {
            this.routes.add(route);
        }
    }

    public void addRoute(City destination, int cost) {
        routes.add(new Route(destination, cost));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " -> [");
        for (int i = 0; i < routes.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(routes.get(i));
        }
        return sb.append("]").toString();
    }
}

/** Задание 5.5. */
class Fraction {

    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Знаменатель не может быть равен нулю");
        }
        int g = gcd(Math.abs(numerator), Math.abs(denominator));
        if (denominator < 0) {
            g = -g;
        }
        this.numerator = numerator / g;
        this.denominator = denominator / g;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public Fraction sum(Fraction other) {
        return new Fraction(numerator * other.denominator + other.numerator * denominator,
                            denominator * other.denominator);
    }

    public Fraction minus(Fraction other) {
        return new Fraction(numerator * other.denominator - other.numerator * denominator,
                            denominator * other.denominator);
    }

    public Fraction mult(Fraction other) {
        return new Fraction(numerator * other.numerator, denominator * other.denominator);
    }

    public Fraction div(Fraction other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("Деление на нулевую дробь");
        }
        return new Fraction(numerator * other.denominator, denominator * other.numerator);
    }

    public Fraction sum(int number)   { return sum(new Fraction(number, 1)); }
    public Fraction minus(int number) { return minus(new Fraction(number, 1)); }
    public Fraction mult(int number)  { return mult(new Fraction(number, 1)); }
    public Fraction div(int number)   { return div(new Fraction(number, 1)); }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}