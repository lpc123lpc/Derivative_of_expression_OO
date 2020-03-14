package third;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private ArrayList<Item> items;
    private String expression;

    Expression(String s) {
        this.expression = s;
        items = new ArrayList<>();
    }

    public String getExp() {
        String output;

        String integer = "[+-]?\\d+";
        String factor1 = "x(\\*{2}[+-]?\\d+)?";
        String factor2 = "((sin)|(cos))\\(.+\\)(\\*{2}[+-]?\\d+)?";
        String factor3 = "\\(.*\\)";
        String factor = "(" + factor1 + "|" + factor2 + "|" + integer + "|" + factor3 + ")";
        String item = "[+-]{0,2}(" + factor + "\\*)*[+-]{0,2}" + factor;
        String exp = item + "([+-]?" + item + ")*";

        //System.out.println(newExpression.matches(item));
        boolean  b = getFormat();
        if (b) {
            String newExpression = expression.replaceAll("[\t ]","");
            boolean flag = newExpression.matches(exp);

            newExpression = getNew(newExpression);
            item = "[+-]*(" + factor + "\\*)*" + factor;
            Pattern p = Pattern.compile(item);
            Matcher m = p.matcher(newExpression);
            while (m.find() && flag) {
                //System.out.println(m.group(0));
                /*if (m.group(0).matches("[+-]*")) {
                    flag = false;
                }
                else {*/
                Item item1 = new Item(m.group(0));
                flag = item1.getFactor();
                items.add(item1);
                //}
            }

            if (!flag) {
                output = null;
            }
            else {
                remove();
                output = print();
            }
        }
        else {
            output = null;
        }
        return output;
    }

    public void remove() {
        items.removeIf(item -> item.getCoe().equals(BigInteger.ZERO));
    }

    public String print() {
        ArrayList<String> output = new ArrayList<>();
        StringBuilder sum = new StringBuilder();
        for (Item item : items) {
            String out = item.getDerivation();
            if (out == null) {
                return null;
            }
            output.add(out);
        }

        if (output.size() == 0) {
            sum.append("0");
        }
        else {
            for (String s : output) {
                sum.append(s);
                //System.out.print(s);
            }
        }

        return sum.toString();

    }

    public boolean getFlag(String s) {
        String sub;
        if (s.length() > 4) {
            sub = s.substring(0,4);
        }
        else {
            sub = s;
        }
        Pattern p = Pattern.compile("[+-]*");
        Matcher m = p.matcher(sub);
        if (m.find()) {
            return m.group(0).length() != 4;
        }
        return true;
    }

    public String getNew(String s) {
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


        return s;
    }

    public boolean getFormat() {


        Pattern p2 = Pattern.compile("\\*\\s+\\*");
        Matcher m2 = p2.matcher(expression);
        if (m2.find()) {
            return false;
        }

        String sp = "[\t ]+";
        String sin = "(s" + sp + "i)|(i" + sp + "n)";
        String cos = "(c" + sp + "o)|(o" + sp + "s)";
        String ult = "(" + sin + ")|(" + cos + ")";
        Pattern p3 = Pattern.compile(ult);
        Matcher m3 = p3.matcher(expression);
        return !m3.find();
    }

}
