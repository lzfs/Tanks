package pp.tanks.client;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> liste = Arrays.asList(1,2,3,4,5);
        System.out.println(liste);
        liste.set(0,4);
        System.out.println(liste);
    }
}
