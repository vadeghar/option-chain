//package org.trade.option.service.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.trade.option.entity.BankNifty;
//import org.trade.option.repository.BankNiftyRepository;
//import org.trade.option.service.iface.BankNiftyService;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//public class BankNiftyServiceImpl implements BankNiftyService {
//    private final BankNiftyRepository bankNiftyRepository;
//
//    public BankNiftyServiceImpl(BankNiftyRepository bankNiftyRepository) {
//        this.bankNiftyRepository = bankNiftyRepository;
//    }
//
//    public BankNifty save(BankNifty bankNifty) {
//        return bankNiftyRepository.save(bankNifty);
//    }
//
//    public void saveAll(List<BankNifty> bankNiftyList) {
//        bankNiftyRepository.saveAll(bankNiftyList);
//    }
//
//    public BankNifty getLastInserted(Integer strikePrice, String optionType, String expiry) {
//        return bankNiftyRepository.findFirstByStrikePriceAndOptionTypeAndExpiryOrderByIdDesc(strikePrice, optionType, expiry);
//    }
//
//    @Override
//    public List<BankNifty> findByUdatedAtSource(String updatedAtSource, Sort sort) {
//        return bankNiftyRepository.findAll(updatedAtSource, sort);
//    }
//
//    @Override
//    public List<String> getInsertedTimeList(String updatedAtSource, Sort sort) {
//        List<String> dateTimeList = bankNiftyRepository.getInsertedTimeList(updatedAtSource, sort);
//        List<String> timeList = dateTimeList.stream().map(s -> s.replace(updatedAtSource, "")).collect(Collectors.toList());
//        return timeList;
//    }
//
//    @Override
//    public List<BankNifty> findAll(String updatedAtSource, Sort sort) {
//        List<BankNifty> all = bankNiftyRepository.findAll(updatedAtSource, sort);
////        List<BankNifty> timeList = all.stream().map(s -> replaceDateTimeWithTime(s, updatedAtSource)).collect(Collectors.toList());
//        return all;
//    }
//
//    private BankNifty replaceDateTimeWithTime(BankNifty s, String updatedAtSource) {
//        s.setUpdatedAtSource(s.getUpdatedAtSource().replace(updatedAtSource, ""));
//        return s;
//    }
//}
