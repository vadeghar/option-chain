package org.trade.option.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.OptionData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OptionDataRepository extends JpaRepository<OptionData, Long> {
    public OptionData findBySymbolAndCurDate(String symbol, LocalDate curDate);
    public OptionData findByStrikePriceAndOptionType(Integer strikePrice, String optionType);

    public OptionData findFirstByStrikePriceAndOptionTypeOrderByIdDesc(Integer strikePrice, String optionType);

    @Query("SELECT t FROM OptionData t WHERE t.symbol LIKE ?1% AND t.updatedAt >= ?2")
    public List<OptionData> findAll(String symbol, LocalDateTime updateFrom, Sort sort);
//    @Query(value = "select distinct(spot_price), updated_at from option_data where symbol like ?1% and updated_at>= ?2", nativeQuery = true)
//    public List<Object[]> getSpotPricesBySymbolAndDate(String symbol, LocalDateTime updateFrom, Sort sort);
}
