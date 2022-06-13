package org.trade.option.service.iface;

import org.springframework.data.domain.Sort;
import org.trade.option.entity.BankNifty;
import org.trade.option.entity.SpotPrice;

import java.util.List;

public interface SpotPriceService {
    public SpotPrice save(SpotPrice spotPrice);
    public void saveAll(List<SpotPrice> spotPriceList);

    public SpotPrice getLastInserted(String symbol);
    public List<SpotPrice> getSpotPriceBySymbol(String symbol, String date, Sort sort);
}
