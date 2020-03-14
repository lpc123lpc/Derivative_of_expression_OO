package third;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String []args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        Pattern pattern = Pattern.compile("^[\t +-]*\\(");
        Matcher matcher = pattern.matcher(expression);

        String newExp;
        if (matcher.find()) {
            int st = expression.indexOf('(');
            int en = expression.lastIndexOf(')');
            newExp = expression.substring(0,st) + expression.substring(st + 1,en) + expression.substring(en + 1);
        }
        else {
            newExp = expression;
        }

        newExp = getNew(expression);

        Expression exp = new Expression(newExp);
        String output = exp.getExp();
        if (output == null) {
            System.out.println("WRONG FORMAT!");
        }
        else {
            System.out.println(output);
        }

    }

    public static String getNew(String s) {
        String flag;
        String out = s;
        String sub;
        if (s.length() > 4) {
            sub = s.substring(0,4);
        }
        else {
            sub = s;
        }
        Pattern p = Pattern.compile("[+-]*");
        Matcher m = p.matcher(sub);
        int end;
        if (m.find()) {
            sub = m.group(0);
            end = m.end();
            sub = sub.replace("+","");
            int len = sub.length();
            if (len == 3) {
                flag = "-";
            }
            else if (len == 2) {
                flag = "+";
            }
            else if (len == 1) {
                flag = "-";
            }
            else {
                flag = "+";
            }
            out = flag + s.substring(end);
        }


        return out;
    }
}
