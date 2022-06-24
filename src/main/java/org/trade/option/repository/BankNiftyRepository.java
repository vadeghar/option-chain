package org.trade.option.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.trade.option.entity.BankNifty;
import org.trade.option.entity.OptionData;

import java.util.List;

@Repository
public interface BankNiftyRepository extends JpaRepository<BankNifty, Long> {

    public BankNifty findFirstByStrikePriceAndOptionTypeAndExpiryOrderByIdDesc(Integer strikePrice, String optionType, String expiry);

    @Query("SELECT DISTINCT p.updatedAtSource from BankNifty p where p.updatedAtSource like ?1%")
    List<String> getInsertedTimeList(String updatedAtSource, Sort sort);
    @Query("select bnf from BankNifty bnf where bnf.updatedAtSource like ?1%")
    List<BankNifty> findAll(String updatedAtSource, Sort sort);
}
