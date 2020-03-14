package third;

import java.math.BigInteger;

public class Cos implements Factor {
    private BigInteger index;
    private String var;

    public Cos(BigInteger index,String s) {
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
            if (sub.matches("[\t ]")) {
                end = var.lastIndexOf(")");
                sub = var.substring(start,end);
            }
            else if (sub.matches("[\t ]*[+-]+[\t ]*")) {
                return null;
            }
        }
        else {
            if (!var.matches(factor1) && !var.matches(factor2) && !var.matches(integer)) {
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
                return "sin(" + var + ")*(" + der + ")";
            } else {
                return "cos(" + var + ")**" + newIndex.toString() + ")sin(" + var + ")*(" + der + ")";
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
            return "cos(x)";
        }
        else {
            return "cos(x)**" + index.toString();
        }
    }

    @Override
    public BigInteger getCoe() {
        return index.multiply(new BigInteger("-1"));
    }
}
