package org.trade.option.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.Nifty;

import java.util.List;

@Repository
public interface NiftyRepository extends JpaRepository<Nifty, Long> {
    Nifty findFirstByStrikePriceAndOptionTypeAndExpiryOrderByIdDesc(Integer strikePrice, String optionType, String expiry);

    @Query("select nf from Nifty nf where nf.updatedAtSource like ?1%")
    public List<Nifty> findAll(String updatedAtSource, Sort sort);

    @Query("SELECT DISTINCT p.updatedAtSource from Nifty p where p.updatedAtSource like ?1%")
    List<String> getInsertedTimeList(String updatedAtSource, Sort sort);
}
