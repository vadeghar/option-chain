package org.trade.option.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.Nifty;

@Repository
public interface NiftyRepository extends JpaRepository<Nifty, Long> {
    Nifty findFirstByStrikePriceAndOptionTypeAndExpiryOrderByIdDesc(Integer strikePrice, String optionType, String expiry);
}
