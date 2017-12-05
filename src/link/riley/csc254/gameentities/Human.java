package link.riley.csc254.gameentities;

public class Human extends Entity implements Mobile {
    public static final int RANGE = 3;
    static String symbol = "😀";


    public String getSymbol() {
        return symbol;
    }

    @Override
    public int getRange() {
        return RANGE;
    }
}
