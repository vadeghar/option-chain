package org.trade.option.service.iface;

import org.springframework.data.domain.Sort;
import org.trade.option.entity.Nifty;

import java.util.List;

public interface NiftyService {

    public Nifty save(Nifty nifty);
    public void saveAll(List<Nifty> niftyList);
    public Nifty getLastInserted(Integer strikePrice, String optionType, String expiry);
    public List<Nifty> findByUdatedAtSource(String updatedAtSource, Sort sort);
    public List<String> getInsertedTimeList(String updatedAtSource, Sort sort);

    List<Nifty> findAll(String updatedAtSource, Sort sort);
}
