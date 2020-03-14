package third;

import java.math.BigInteger;

public class Var implements Factor {
    private BigInteger index;

    public Var(BigInteger index) {
        this.index = index;
    }

    @Override
    public String getDerivation() {
        BigInteger newIndex = index.subtract(BigInteger.ONE);
        if (newIndex.equals(BigInteger.ZERO)) {
            return "";
        }

        else {
            return "x**" + newIndex.toString();
        }
    }

    @Override
    public void addIndex(BigInteger index) {
        this.index = this.index.add(index);
    }

    @Override
    public String getOri() {
        if (index.equals(BigInteger.ONE)) {
            return "x";
        }
        else {
            return "x**" + index.toString();
        }
    }

    @Override
    public BigInteger getCoe() {
        return index;
    }
}
