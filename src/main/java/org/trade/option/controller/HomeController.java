package org.trade.option.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.entity.OptionData;
import org.trade.option.service.iface.OptionDataService;
import org.trade.option.utils.OptionTypeEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final OptionDataService optionDataService;

    public HomeController(OptionDataService optionDataService) {
        this.optionDataService = optionDataService;
    }
    @GetMapping(value = { "/"})
    public String home(Model model) {

        model.addAttribute("df", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        return "/core/index";
    }
    @GetMapping(value = { "/home" })
    public String homeViewPost(Model model) {
        List<OptionData> optionNiftyDataList = optionDataService.findAll(OcSymbolEnum.NIFTY.getOhlcSymbol(), LocalDate.now().atStartOfDay(), Sort.by("id").descending());
        List<OptionData> optionBankNiftyDataList = optionDataService.findAll(OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), LocalDate.now().atStartOfDay(), Sort.by("id").descending());

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

        return "/core/home";
    }


    @GetMapping(value = { "analysis" })
    public String analysis(Model model) {
        return "/core/analysis";
    }
    @GetMapping(value = {"/refresh"})
    public @ResponseBody Map<String, List<OptionData>> refresh() {
        Map<String, List<OptionData>> response = new HashMap<>();
        List<OptionData> optionNiftyDataList = optionDataService.findAll(OcSymbolEnum.NIFTY.getOhlcSymbol(), LocalDate.now().atStartOfDay(), Sort.by("id").descending());
        List<OptionData> optionBankNiftyDataList = optionDataService.findAll(OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), LocalDate.now().atStartOfDay(), Sort.by("id").descending());

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
    public @ResponseBody Map<String, List<OptionData>> refreshAnalysis() {
        Map<String, List<OptionData>> response = new HashMap<>();
        List<OptionData> optionNiftyDataList = optionDataService.findAll(OcSymbolEnum.NIFTY.getOhlcSymbol(), LocalDate.now().atStartOfDay(), Sort.by("id").ascending());
        List<OptionData> niftyCeList = filter(optionNiftyDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> niftyPeList = filter(optionNiftyDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());

        response.put("niftyCeList", niftyCeList);
        response.put("niftyPeList", niftyPeList);
        return response;
    }

    private List<OptionData> filter(List<OptionData> optionDataList, String symbol, String optionType) {
        return optionDataList.stream()
                .filter(n -> n.getSymbol().startsWith(symbol) && n.getOptionType().equals(optionType))
                .sorted(Comparator.comparing(OptionData::getId).reversed())
                .collect(Collectors.toList());
    }
}
