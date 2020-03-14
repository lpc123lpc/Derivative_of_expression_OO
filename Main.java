package third;

import java.util.Scanner;

public class Main {

    public static void main(String []args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        Expression exp = new Expression(expression);
        String output = exp.getExp();
        if (output == null) {
            System.out.println("WRONG FORMAT!");
        }
        else {
            System.out.println(output);
        }

    }
}
