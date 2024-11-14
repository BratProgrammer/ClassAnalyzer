package org.example;

public class ClassForTest {

    private int field1 = 1;
    private String field2 = "field2";


    public void someMethod() {
        int new_var = 10;
        String new_var_2 = "some text";

        if (new_var == 10) {
            System.out.println("Java is work");
        } else {
            System.out.println("What?");
        }

        for (int i = 0; i < new_var; i++) {
            System.out.println("Cycle iteration " + (i + 1));
        }
    }

}
