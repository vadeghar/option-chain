package org.trade.option.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.Nifty;
import org.trade.option.entity.SpotPrice;
@Repository
public interface SpotPriceRepository extends JpaRepository<SpotPrice, Long> {
    SpotPrice findTopBySymbolOrderByIdDesc(String symbol);
}
