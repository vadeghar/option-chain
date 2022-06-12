package org.trade.option.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.trade.option.entity.BankNifty;
import org.trade.option.entity.Nifty;
import org.trade.option.repository.BankNiftyRepository;
import org.trade.option.service.iface.BankNiftyService;

import java.util.List;

@Service
@Slf4j
public class BankNiftyServiceImpl implements BankNiftyService {
    private final BankNiftyRepository bankNiftyRepository;

    public BankNiftyServiceImpl(BankNiftyRepository bankNiftyRepository) {
        this.bankNiftyRepository = bankNiftyRepository;
    }

    public BankNifty save(BankNifty bankNifty) {
        return bankNiftyRepository.save(bankNifty);
    }

    public void saveAll(List<BankNifty> bankNiftyList) {
        bankNiftyRepository.saveAll(bankNiftyList);
    }

    public BankNifty getLastInserted(Integer strikePrice, String optionType, String expiry) {
        return bankNiftyRepository.findFirstByStrikePriceAndOptionTypeAndExpiryOrderByIdDesc(strikePrice, optionType, expiry);
    }
}
