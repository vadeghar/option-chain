package org.trade.option.service.iface;

import org.springframework.data.domain.Sort;
import org.trade.option.entity.BankNifty;

import java.util.List;

public interface BankNiftyService {
    public BankNifty save(BankNifty bankNifty);
    public void saveAll(List<BankNifty> bankNiftyList);
    public BankNifty getLastInserted(Integer strikePrice, String optionType, String expiry);

    List<BankNifty> findByUdatedAtSource(String updatedAtSource, Sort sort);

    List<String> getInsertedTimeList(String updatedAtSource, Sort sort);

    List<BankNifty> findAll(String updatedAtSource, Sort sort);
}
