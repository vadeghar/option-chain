package org.trade.option.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.entity.OptionData;
import org.trade.option.service.iface.OptionDataService;
import org.trade.option.utils.OptionTypeEnum;

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

    @GetMapping(value = { "/", "/home" })
    public String homeViewPost(Model model) {
        List<OptionData> optionDataList = optionDataService.findAll();

        List<OptionData> niftyCeList = filter(optionDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> niftyPeList = filter(optionDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());
        List<OptionData> bankNiftyCeList = filter(optionDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> bankNiftyPeList = filter(optionDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());

        model.addAttribute("niftyCeList", niftyCeList);
        model.addAttribute("niftyPeList", niftyPeList);
        model.addAttribute("bankNiftyCeList", bankNiftyCeList);
        model.addAttribute("bankNiftyPeList", bankNiftyPeList);

        return "/core/home";
    }

    @GetMapping(value = {"/refresh"})
    public @ResponseBody Map<String, List<OptionData>> refresh() {
        Map<String, List<OptionData>> response = new HashMap<>();
        List<OptionData> optionDataList = optionDataService.findAll();

        List<OptionData> niftyCeList = filter(optionDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> niftyPeList = filter(optionDataList, OcSymbolEnum.NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());
        List<OptionData> bankNiftyCeList = filter(optionDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.CE.name());
        List<OptionData> bankNiftyPeList = filter(optionDataList, OcSymbolEnum.BANK_NIFTY.getOhlcSymbol(), OptionTypeEnum.PE.name());

        response.put("niftyCeList", niftyCeList);
        response.put("niftyPeList", niftyPeList);
        response.put("bankNiftyCeList", bankNiftyCeList);
        response.put("bankNiftyPeList", bankNiftyPeList);
        return response;
    }

    private List<OptionData> filter(List<OptionData> optionDataList, String symbol, String optionType) {
        return optionDataList.stream()
                .filter(n -> n.getSymbol().startsWith(symbol) && n.getOptionType().equals(optionType))
                .sorted(Comparator.comparing(OptionData::getId).reversed())
                .collect(Collectors.toList());
    }
}
