package third;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item {
    private String poly;
    private BigInteger coe;
    private Factor [] fa = new Factor[3];
    private int [] flag = new int[3];

    public Item(String poly) {
        this.poly = poly;
        this.coe = BigInteger.ONE;
    }

    public boolean getFactor() {
        boolean flag = true;
        String integer = "(?<int>([+-]*\\d+)|([+-]+\\d*))";
        String factor1 = "(?<var>x(\\*{2}[+-]?\\d+)?)";
        String factor2 = "(?<tri>((sin)|(cos)))";
        String factor = "(" + factor1 + "|" + factor2 + "|" + integer + ")";

        Pattern p = Pattern.compile(factor);
        Matcher m = p.matcher(poly);
        //System.out.println(poly+"hello");
        Creat();
        while (m.find() && flag) {
            int end = 0;
            int start = 0;
            String str = m.group(0);
            //System.out.println(str);
            if (m.group("int") != null) {
                flag = getC(str);
                end = m.end();
                poly = poly.substring(end);
            }
            else if (m.group("var") != null) {
                flag = getFlag(str);
                if (!flag) {
                    continue;
                }
                BigInteger index = getVarIndex(str);
                if (!index.equals(BigInteger.ZERO)) {
                    if (index.abs().compareTo(new BigInteger("50")) > 0) {
                        flag = false;
                    }
                    if (this.flag[0] == 0) {
                        fa[0] = new Var(index);
                        this.flag[0] = 1;
                    }
                    else {
                        fa[0].addIndex(index);
                    }
                }
                end = m.end();
                poly = poly.substring(end);
            }
            else {
                int type;
                if (m.group(0).contains("sin")) {
                    type = 0;
                }
                else {
                    type = 1;
                }
                flag = getW(m.start(),flag,type);
            }
            m = p.matcher(poly);
        }


        ifRemove();


        return flag;
    }

    public boolean getW(int start,boolean flag,int type) {
        int end = 0;
        int endWhere = 0;
        int startWhere = 0;
        poly = poly.substring(start);
        int sum = 0;
        int fuck = 0;
        for (int i = 0;i < poly.length();i++) {

            if (poly.charAt(i) == '(') {
                if (fuck == 0) {
                    startWhere = i;
                    fuck = 1;
                }
                sum++;
            }
            if ((poly.charAt(i) == ')') && fuck == 1) {
                sum--;
                if (sum == 0) {
                    endWhere = i;
                    break;
                }
            }
        }
        String var = poly.substring(startWhere,endWhere + 1);

        poly = poly.substring(endWhere + 1);
        String x = "^[\t ]*\\*{2}(?<int>[+-]?\\d+)";
        Pattern p = Pattern.compile(x);
        Matcher m = p.matcher(poly);
        String in;
        if (m.find()) {
            in = m.group("int");
            poly = poly.substring(m.end());
        }
        else {
            in = "1";
        }
        BigInteger index = new BigInteger(in);
        if (!index.equals(BigInteger.ZERO)) {
            if (index.abs().compareTo(new BigInteger("50")) > 0) {
                return false;
            }
            if (flag) {
                add(index,var,type);
            }
        }
        return true;
    }

    public boolean getC(String string) {
        String flag = "[+-]+";
        String coe = "\\d+";
        String fuck;
        Pattern p = Pattern.compile(flag);
        Matcher m = p.matcher(string);
        if (m.find()) {
            String str = m.group(0);
            if (str.length() > 3) {
                return false;
            }
            else {
                str = str.replace("+", "");
                if (str.length() == 0) {
                    fuck = "+";
                } else if (str.length() == 1) {
                    fuck = "-";
                }
                else if (str.length() == 2) {
                    fuck = "+";
                }
                else {
                    fuck = "-";
                }
            }
        }
        else {
            fuck = "+";
        }
        Pattern p2 = Pattern.compile(coe);
        m = p2.matcher(string);
        BigInteger temp;
        if (m.find()) {
            temp = new BigInteger(fuck + m.group(0));
        }
        else {
            temp = new BigInteger(fuck + "1");
        }

        if (this.coe.equals(BigInteger.ZERO)) {
            this.coe = temp;
        }
        else {
            this.coe = this.coe.multiply(temp);
        }
        return true;

    }

    public BigInteger getIndex(String str) {
        String index = "\\)\\*{2}[+-]?\\d+";
        String newIndex = "[+-]?\\d+";
        Pattern p = Pattern.compile(index);
        Matcher m = p.matcher(str);
        if (m.find()) {
            String sub = m.group(0);
            Pattern pattern = Pattern.compile(newIndex);
            Matcher matcher = pattern.matcher(sub);
            if (matcher.find()) {
                return new BigInteger(matcher.group(0));
            }
            else {
                return null;
            }
        }
        else {
            return BigInteger.ONE;
        }
    }

    public void Creat() {
        for (int i = 0;i < 3;i++) {
            flag[i] = 0;
        }
    }

    public String getDerivation() {
        StringBuilder finaOut = new StringBuilder();
        for (int i = 0;i < 3;i++) {
            if (flag[i] != 0) {
                BigInteger coe = fa[i].getCoe().multiply(this.coe);
                StringBuilder out;
                out = new StringBuilder(coe.toString());
                String temp = fa[i].getDerivation();
                if (temp == null) {
                    return null;
                }
                else if (temp.equals("")) {
                    out = new StringBuilder(coe.toString());
                }
                else {
                    out = new StringBuilder(coe.toString() + "*" + temp);
                }

                for (int j = 0;j < 3;j++) {
                    if (flag[j] != 0 && j != i) {
                        out.append("*").append(fa[j].getOri());
                    }
                }
                if (coe.compareTo(BigInteger.ZERO) > 0) {
                    finaOut.append("+").append(out);
                }
                else {
                    finaOut.append(out);
                }
            }

        }
        return finaOut.toString();
        //return finaOut.toString();
    }

    public BigInteger getCoe() {
        return coe;
    }

    public void ifRemove() {
        if (flag[0] + flag[1] + flag[2] == 0) {
            coe = BigInteger.ZERO;
        }
    }

    public boolean getFlag(String s) {
        Pattern p = Pattern.compile("[+-]+");
        Matcher m = p.matcher(s);
        int temp = 0;
        if (m.find()) {
            temp = 1;
        }

        Pattern p2 = Pattern.compile("\\d+");
        Matcher m2 = p2.matcher(s);
        if (!m2.find()) {
            return temp != 1;
        }
        return true;
    }

    public void add(BigInteger index,String str,int type) {
        int start = str.indexOf("(");
        int end = str.lastIndexOf(")");
        String s = str.substring(start + 1,end);
        if (!index.equals(BigInteger.ZERO)) {
            if (type == 0) {
                if (this.flag[1] == 0) {
                    fa[1] = new Sin(index,s);
                    this.flag[1] = 1;
                }
                else {
                    fa[1].addIndex(index);
                }
            }
            else {
                if (this.flag[2] == 0) {
                    fa[2] = new Cos(index,s);
                    this.flag[2] = 1;
                }
                else {
                    fa[2].addIndex(index);
                }
            }
        }
    }

    public BigInteger getVarIndex(String str) {
        String index = "[+-]?\\d+";
        Pattern p = Pattern.compile(index);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return new BigInteger(m.group(0));
        }
        else {
            return BigInteger.ONE;
        }
    }
}
