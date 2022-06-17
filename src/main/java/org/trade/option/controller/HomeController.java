package org.trade.option.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.entity.Nifty;
import org.trade.option.entity.OptionData;
import org.trade.option.entity.SpotPrice;
import org.trade.option.service.iface.NiftyService;
import org.trade.option.service.iface.OptionDataService;
import org.trade.option.service.iface.SpotPriceService;
import org.trade.option.utils.ExpiryUtils;
import org.trade.option.utils.OptionTypeEnum;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final OptionDataService optionDataService;
    private final SpotPriceService spotPriceService;
    private final NiftyService niftyService;
    private static final Integer noOfStrikesPricesInEachCompartment = 3;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    public HomeController(OptionDataService optionDataService, SpotPriceService spotPriceService, NiftyService niftyService) {
        this.optionDataService = optionDataService;
        this.spotPriceService = spotPriceService;
        this.niftyService = niftyService;
    }
    @GetMapping(value = { "/"})
    public String home(Model model) {

        model.addAttribute("df", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return "core/index";
    }
    @GetMapping(value = { "/home" })
    public String homeViewPost(Model model) {
        List<OptionData> optionNiftyDataList = optionDataService.findAll(OcSymbolEnum.NIFTY.getOhlcSymbol(), LocalDate.now(ZoneId.of("Asia/Kolkata")).atStartOfDay(), Sort.by("id").descending());
        List<OptionData> optionBankNiftyDataList = optionDataService.findAll(OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), LocalDate.now(ZoneId.of("Asia/Kolkata")).atStartOfDay(), Sort.by("id").descending());

        List<OptionData> niftyCeList = filter(optionNiftyDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> niftyPeList = filter(optionNiftyDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());
        List<OptionData> bankNiftyCeList = filter(optionBankNiftyDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> bankNiftyPeList = filter(optionBankNiftyDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());


        Double niftySpotPrice = niftyCeList.get(0).getSpotPrice();
        Double bankNiftySpotPrice = bankNiftyCeList.get(0).getSpotPrice();
        String expDate = niftyCeList.get(0).getExpiry();
        model.addAttribute("niftyCeList", niftyCeList);
        model.addAttribute("niftyPeList", niftyPeList);
        model.addAttribute("bankNiftyCeList", bankNiftyCeList);
        model.addAttribute("bankNiftyPeList", bankNiftyPeList);

        model.addAttribute("niftySpotPrice", niftySpotPrice);
        model.addAttribute("bankNiftySpotPrice", bankNiftySpotPrice);
        model.addAttribute("expDate", expDate);

        model.addAttribute("df", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        return "core/home";
    }


    @GetMapping(value = { "analysis" })
    public String analysis(Model model) {
        return "core/analysis";
    }
    @GetMapping(value = {"/refresh"})
    public @ResponseBody Map<String, List<OptionData>> refresh() {
        Map<String, List<OptionData>> response = new HashMap<>();
        List<OptionData> optionNiftyDataList = optionDataService.findAll(OcSymbolEnum.NIFTY.getOhlcSymbol(), LocalDate.now(ZoneId.of("Asia/Kolkata")).atStartOfDay(), Sort.by("id").descending());
        List<OptionData> optionBankNiftyDataList = optionDataService.findAll(OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), LocalDate.now(ZoneId.of("Asia/Kolkata")).atStartOfDay(), Sort.by("id").descending());

        List<OptionData> niftyCeList = filter(optionNiftyDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> niftyPeList = filter(optionNiftyDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());
        List<OptionData> bankNiftyCeList = filter(optionBankNiftyDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> bankNiftyPeList = filter(optionBankNiftyDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());

        response.put("niftyCeList", niftyCeList);
        response.put("niftyPeList", niftyPeList);
        response.put("bankNiftyCeList", bankNiftyCeList);
        response.put("bankNiftyPeList", bankNiftyPeList);
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
        System.out.println("response: "+response);

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

    @GetMapping(value = {"/refreshIndex"})
    public @ResponseBody Map<String, List<SpotPrice>> refreshIndexPage() {
        Map<String, List<SpotPrice>> response = new HashMap<>();

        response.put("niftyToday", spotPriceService.getSpotPriceBySymbol(OcSymbolEnum.NIFTY.getOhlcSymbol(), LocalDate.now(ZoneId.of("Asia/Kolkata")).format(formatter), Sort.by("id").ascending()));
        response.put("bankNiftyToday", spotPriceService.getSpotPriceBySymbol(OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), LocalDate.now(ZoneId.of("Asia/Kolkata")).format(formatter), Sort.by("id").ascending()));
        return response;
    }

    private List<OptionData> filter(List<OptionData> optionDataList, String symbol, String optionType) {
        return optionDataList.stream()
                .filter(n -> n.getSymbol().startsWith(symbol) && n.getOptionType().equals(optionType))
                .sorted(Comparator.comparing(OptionData::getId).reversed())
                .collect(Collectors.toList());
    }
}
