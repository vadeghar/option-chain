package org.trade.option.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.entity.Nifty;
import org.trade.option.entity.SpotPrice;
import org.trade.option.service.iface.NiftyService;
import org.trade.option.service.iface.SpotPriceService;
import org.trade.option.utils.ExpiryUtils;
import org.trade.option.utils.OptionTypeEnum;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class NiftyController {
    private final SpotPriceService spotPriceService;
    private final NiftyService niftyService;
    private static final Integer noOfStrikesPricesInEachCompartment = 3;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    public NiftyController(SpotPriceService spotPriceService, NiftyService niftyService) {
        this.spotPriceService = spotPriceService;
        this.niftyService = niftyService;
    }

    @GetMapping(value = { "analysis" })
    public String analysis(Model model) {
        model.addAttribute("active", 2);
        return "core/analysis";
    }

    @GetMapping(value = {"/refresh"})
    public @ResponseBody Map<String, Object> refresh() {
        Map<String, Object> response = new HashMap<>();
        SpotPrice niftySpot = spotPriceService.getLastInserted(OcSymbolEnum.NIFTY.getOhlcSymbol());
        String inputDay = LocalDate.now(ZoneId.of("Asia/Kolkata")).format(formatter);
        List<Nifty> niftyList = niftyService.findAll(inputDay, Sort.by("id").descending());
        List<Nifty> niftyCeList = filter(niftyList, OptionTypeEnum.CE.name());
        List<Nifty> niftyPeList = filter(niftyList, OptionTypeEnum.PE.name());
        Double niftySpotPrice = niftySpot.getLastPrice();
        response.put("niftyCeList", niftyCeList);
        response.put("niftyPeList", niftyPeList);
        response.put("niftySpotPrice", niftySpotPrice);
        return response;
    }

    @GetMapping(value = {"/refreshAnalysis"})
    public @ResponseBody Map<String, Object> refreshAnalysis() {
        SpotPrice niftySpot = spotPriceService.getLastInserted(OcSymbolEnum.NIFTY.getOhlcSymbol());
        Integer atmStrike = ExpiryUtils.getATM(niftySpot.getLastPrice());
        String inputDay = LocalDate.now(ZoneId.of("Asia/Kolkata")).format(formatter);
        List<String> insertedTimeList = niftyService.getInsertedTimeList(inputDay, Sort.by("id").ascending());
        List<Nifty> todayData = niftyService.findByUdatedAtSource(inputDay, Sort.by("id").ascending());
        // Segment: 1, All In the money Call options
        List<Map<Integer, List<Nifty>>> segment1 = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        response.put("compartment1", prepareCompartment1(atmStrike, insertedTimeList, todayData, inputDay));
        response.put("compartment2", prepareCompartment2(atmStrike, insertedTimeList, todayData, inputDay));
        response.put("compartment3", prepareCompartment3(atmStrike, insertedTimeList, todayData, inputDay));
        response.put("compartment4", prepareCompartment4(atmStrike, insertedTimeList, todayData, inputDay));

        response.put("insertedTimeList", insertedTimeList);
        response.put("niftyATM", atmStrike);
        response.put("niftySpot", niftySpot.getUpdatedAtSource().replace(inputDay, "") +": "+ niftySpot.getLastPrice());
        response.put("currentDate", inputDay);
//        System.out.println("response: "+response);

        return response;
    }


    private Map<Integer, Map> prepareCompartment1(Integer atmStrike, List<String> insertedTimeList, List<Nifty> todayData, String inputDay) {
        Integer depth = 50;
        Integer maxStrikePrice = atmStrike + (noOfStrikesPricesInEachCompartment * depth);
        Integer minStrikePrice = atmStrike - (noOfStrikesPricesInEachCompartment * depth);

        Map<Integer, List<Nifty>> strikeWiseData = todayData.stream()
                .filter(nf -> nf.getStrikePrice() <= atmStrike && nf.getStrikePrice() > (minStrikePrice-50) && nf.getOptionType().equals("CE"))
                .collect(Collectors.groupingBy(Nifty::getStrikePrice));
        Map<Integer, Map> compartment1 = new HashMap<>();
        for(Integer strikePrice: strikeWiseData.keySet()) {
            Map<String, Long> insertedTimeItOi = new HashMap<>();
            for(String insertedTime : insertedTimeList) {
                insertedTimeItOi.put(insertedTime, getDataAtInsertedTime(strikeWiseData.get(strikePrice), insertedTime, inputDay));
            }
            Map<String, Long> result = insertedTimeItOi.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            compartment1.put(strikePrice, result);
        }
        return compartment1;
    }

    private Map<Integer, Map> prepareCompartment2(Integer atmStrike, List<String> insertedTimeList, List<Nifty> todayData, String inputDay) {
        Integer depth = 50;
        Integer maxStrikePrice = atmStrike + (noOfStrikesPricesInEachCompartment * depth);
        Integer minStrikePrice = atmStrike - (noOfStrikesPricesInEachCompartment * depth);
        Map<Integer, List<Nifty>> strikeWiseData = todayData.stream()
                .filter(nf -> nf.getStrikePrice() > atmStrike && nf.getStrikePrice() <= maxStrikePrice && nf.getOptionType().equals("CE"))
                .collect(Collectors.groupingBy(Nifty::getStrikePrice));
        Map<Integer, Map> compartment2 = new HashMap<>();
        for(Integer strikePrice: strikeWiseData.keySet()) {
            Map<String, Long> insertedTimeItOi = new HashMap<>();
            for(String insertedTime : insertedTimeList) {
                insertedTimeItOi.put(insertedTime, getDataAtInsertedTime(strikeWiseData.get(strikePrice), insertedTime, inputDay));
            }
            Map<String, Long> result = insertedTimeItOi.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            compartment2.put(strikePrice, result);
        }
        return compartment2;
    }

    private Map<Integer, Map> prepareCompartment4(Integer atmStrike, List<String> insertedTimeList, List<Nifty> todayData, String inputDay) {
        Integer depth = 50;
        Integer maxStrikePrice = atmStrike + (noOfStrikesPricesInEachCompartment * depth);
        Integer minStrikePrice = atmStrike - (noOfStrikesPricesInEachCompartment * depth);
        Map<Integer, List<Nifty>> strikeWiseData = todayData.stream()
                .filter(nf -> nf.getStrikePrice() > atmStrike && nf.getStrikePrice() <= maxStrikePrice && nf.getOptionType().equals("PE"))
                .collect(Collectors.groupingBy(Nifty::getStrikePrice));
        Map<Integer, Map> compartment3 = new HashMap<>();
        for(Integer strikePrice: strikeWiseData.keySet()) {
            Map<String, Long> insertedTimeItOi = new HashMap<>();
            for(String insertedTime : insertedTimeList) {
                insertedTimeItOi.put(insertedTime, getDataAtInsertedTime(strikeWiseData.get(strikePrice), insertedTime, inputDay));
            }
            Map<String, Long> result = insertedTimeItOi.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            compartment3.put(strikePrice, result);
        }
        return compartment3;
    }
    private Map<Integer, Map> prepareCompartment3(Integer atmStrike, List<String> insertedTimeList, List<Nifty> todayData, String inputDay) {
        Integer depth = 50;
        Integer maxStrikePrice = atmStrike + (noOfStrikesPricesInEachCompartment * depth);
        Integer minStrikePrice = atmStrike - (noOfStrikesPricesInEachCompartment * depth);
        Map<Integer, List<Nifty>> strikeWiseData = todayData.stream()
                .filter(nf -> nf.getStrikePrice() <= atmStrike && nf.getStrikePrice() > (minStrikePrice-50) && nf.getOptionType().equals("PE"))
                .collect(Collectors.groupingBy(Nifty::getStrikePrice));
        Map<Integer, Map> compartment4 = new HashMap<>();
        for(Integer strikePrice: strikeWiseData.keySet()) {
            Map<String, Long> insertedTimeItOi = new HashMap<>();
            for(String insertedTime : insertedTimeList) {
                insertedTimeItOi.put(insertedTime, getDataAtInsertedTime(strikeWiseData.get(strikePrice), insertedTime, inputDay));
            }
            Map<String, Long> result = insertedTimeItOi.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            compartment4.put(strikePrice, result);
        }
        return compartment4;
    }



    private Long getDataAtInsertedTime(List<Nifty> niftyList, String insertedTime, String inputDay) {
        Nifty nifty = niftyList.stream().filter(n -> n.getUpdatedAtSource().replace(inputDay,"").equals(insertedTime)).findFirst().orElse(null);
        return nifty != null ? nifty.getChangeInOi() : 0;
    }

    private List<Nifty> filter(List<Nifty> optionDataList, String optionType) {
        return optionDataList.stream()
                .filter(n -> n.getOptionType().equals(optionType))
                .sorted(Comparator.comparing(Nifty::getId).reversed())
                .collect(Collectors.toList());
    }
}