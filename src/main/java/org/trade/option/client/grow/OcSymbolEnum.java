package org.trade.option.client.grow;

public enum OcSymbolEnum {

    NIFTY("nifty", "NIFTY"),
    BANK_NIFTY("nifty-bank", "BANKNIFTY")
    ;

    private String symbol;
    private String ohlcSymbol;

    OcSymbolEnum(String symbol, String ohlcSymbol) {
        this.symbol = symbol;
        this.ohlcSymbol = ohlcSymbol;
    }

    public String getOhlcSymbol() {
        return ohlcSymbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
