package org.trade.option.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.OptionData;

import java.time.LocalDate;
@Repository
public interface OptionDataRepository extends JpaRepository<OptionData, Long> {
    public OptionData findBySymbolAndCurDate(String symbol, LocalDate curDate);
    public OptionData findByStrikePriceAndOptionType(Integer strikePrice, String optionType);
}
