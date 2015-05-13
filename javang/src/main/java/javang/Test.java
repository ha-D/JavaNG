package javang;

public class Test {
    public static void main(String[] args) {
        System.out.println(Test.class.getResource("abc.txt"));
        ClassLoader classLoader = Test.class.getClassLoader();
        System.out.println(classLoader.getResource("abc.txt"));
    }

}
