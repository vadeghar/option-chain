package org.trade.option.service.iface;

import org.trade.option.entity.Nifty;

import java.util.List;

public interface NiftyService {

    public Nifty save(Nifty nifty);
    public void saveAll(List<Nifty> niftyList);
    public Nifty getLastInserted(Integer strikePrice, String optionType, String expiry);
}
