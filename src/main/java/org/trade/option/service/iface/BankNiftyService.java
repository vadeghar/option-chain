package org.trade.option.service.iface;

import org.trade.option.entity.BankNifty;

import java.util.List;

public interface BankNiftyService {
    public BankNifty save(BankNifty bankNifty);
    public void saveAll(List<BankNifty> bankNiftyList);
    public BankNifty getLastInserted(Integer strikePrice, String optionType, String expiry);
}
