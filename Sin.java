package third;

import java.math.BigInteger;

public class Sin implements Factor {
    private String var;
    private BigInteger index;

    public Sin(BigInteger index,String s) {
        this.var = s;
        this.index = index;
    }

    @Override
    public String getDerivation() {
        String integer = "[+-]?\\d+";
        String factor1 = "x(\\*{2}[+-]?\\d+)?";
        String factor2 = "((sin)|(cos))\\(.+\\)(\\*{2}[+-]?\\d+)?";
        String sub = var;
        if (var.contains("(")) {
            int start = var.indexOf("(");
            int end;
            sub = var.substring(0,start);
            if (sub.matches("[\t ]*")) {
                end = var.lastIndexOf(")");
                sub = var.substring(start + 1,end);
            }
            else if (sub.matches("[\t ]*[+-]+[\t ]*")) {
                return null;
            }
        }
        else {
            if (!var.matches(factor1) && !var.matches(factor2)&&!var.matches(integer)) {
                return null;
            }
        }
        Expression x = new Expression(sub);
        String der = x.getExp();
        BigInteger newIndex = index.subtract(BigInteger.ONE);
        if (der == null) {
            return null;
        }
        else {
            if (newIndex.equals(BigInteger.ZERO)) {
                return "cos(" + var + ")*(" + der + ")";
            } else {
                return "sin(" + var + ")**" + newIndex.toString() + ")*cos(" + var + ")*(" + der + ")";
            }
        }
    }

    @Override
    public void addIndex(BigInteger index) {
        this.index = this.index.add(index);
    }

    @Override
    public String getOri() {
        if (index.equals(BigInteger.ONE)) {
            return "sin(x)";
        }
        else {
            return "sin(x)**" + index.toString();
        }
    }

    @Override
    public BigInteger getCoe() {
        return index;
    }
}
