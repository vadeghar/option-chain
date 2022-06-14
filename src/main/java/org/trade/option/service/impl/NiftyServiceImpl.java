package org.trade.option.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.trade.option.entity.BankNifty;
import org.trade.option.entity.Nifty;
import org.trade.option.repository.NiftyRepository;
import org.trade.option.service.iface.NiftyService;

import java.util.List;

@Service
@Slf4j
public class NiftyServiceImpl implements NiftyService {

    private final NiftyRepository niftyRepository;

    public NiftyServiceImpl(NiftyRepository niftyRepository) {
        this.niftyRepository = niftyRepository;
    }

    public Nifty save(Nifty nifty) {
        return niftyRepository.save(nifty);
    }

    public void saveAll(List<Nifty> niftyList) {
        niftyRepository.saveAll(niftyList);
    }
    public Nifty getLastInserted(Integer strikePrice, String optionType, String expiry) {
        return niftyRepository.findFirstByStrikePriceAndOptionTypeAndExpiryOrderByIdDesc(strikePrice, optionType, expiry);
    }

    @Override
    public List<Nifty> findByUdatedAtSource(String updatedAtSource, Sort sort) {
        return niftyRepository.findAll(updatedAtSource, sort);
    }
}
