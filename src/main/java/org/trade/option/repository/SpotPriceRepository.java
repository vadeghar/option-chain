package org.trade.option.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.Nifty;
import org.trade.option.entity.SpotPrice;

import java.util.List;

@Repository
public interface SpotPriceRepository extends JpaRepository<SpotPrice, Long> {
    SpotPrice findTopBySymbolOrderByIdDesc(String symbol);

    @Query("select sp from SpotPrice sp where sp.symbol = ?1 and sp.updatedAtSource like ?2%")
    List<SpotPrice> getSpotPriceBySymbol(String symbol, String date, Sort sort);
}
