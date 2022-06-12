package org.trade.option.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.BankNifty;
import org.trade.option.entity.OptionData;

@Repository
public interface BankNiftyRepository extends JpaRepository<BankNifty, Long> {

    public BankNifty findFirstByStrikePriceAndOptionTypeAndExpiryOrderByIdDesc(Integer strikePrice, String optionType, String expiry);
}
