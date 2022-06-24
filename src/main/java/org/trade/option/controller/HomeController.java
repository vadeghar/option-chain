package org.trade.option.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.trade.option.client.grow.OcSymbolEnum;
import org.trade.option.entity.BankNifty;
import org.trade.option.entity.Nifty;
import org.trade.option.entity.SpotPrice;
import org.trade.option.service.iface.BankNiftyService;
import org.trade.option.service.iface.NiftyService;
import org.trade.option.service.iface.OptionDataService;
import org.trade.option.service.iface.SpotPriceService;
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
    private final BankNiftyService bankNiftyService;
    private static final Integer noOfStrikesPricesInEachCompartment = 3;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    public HomeController(OptionDataService optionDataService, SpotPriceService spotPriceService, NiftyService niftyService, BankNiftyService bankNiftyService) {
        this.optionDataService = optionDataService;
        this.spotPriceService = spotPriceService;
        this.niftyService = niftyService;
        this.bankNiftyService = bankNiftyService;
    }
    @GetMapping(value = { "/","/index"})
    public String home(Model model) {

        model.addAttribute("df", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        model.addAttribute("active", 0);
        return "core/index";
    }
    @GetMapping(value = { "/home" })
    public String homeViewPost(Model model) {
        SpotPrice niftySpot = spotPriceService.getLastInserted(OcSymbolEnum.NIFTY.getOhlcSymbol());
        String inputDay = LocalDate.now(ZoneId.of("Asia/Kolkata")).format(formatter);
        List<Nifty> niftyList = niftyService.findAll(inputDay, Sort.by("id").descending());
        List<Nifty> niftyCeList = filter(niftyList, OptionTypeEnum.CE.name());
        List<Nifty> niftyPeList = filter(niftyList, OptionTypeEnum.PE.name());

        SpotPrice bnfSpot = spotPriceService.getLastInserted(OcSymbolEnum.BANK_NIFTY.getOhlcSymbol());
        List<BankNifty> bnfList = bankNiftyService.findAll(inputDay, Sort.by("id").descending());
        List<BankNifty> bnfCeList = filterBnf(bnfList, OptionTypeEnum.CE.name());
        List<BankNifty> bnfPeList = filterBnf(bnfList, OptionTypeEnum.PE.name());

        Double niftySpotPrice = niftySpot.getLastPrice();
        String expDate = niftyCeList.get(0).getExpiry();
        model.addAttribute("niftyCeList", niftyCeList);
        model.addAttribute("niftyPeList", niftyPeList);

        model.addAttribute("bankNiftyCeList", bnfCeList);
        model.addAttribute("bankNiftyPeList", bnfPeList);


        model.addAttribute("niftySpotPrice", bnfSpot);
        model.addAttribute("bankNiftySpotPrice", niftySpotPrice);
        model.addAttribute("expDate", expDate);
        model.addAttribute("df", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        model.addAttribute("active", 1);
        return "core/home";
    }

    private List<Nifty> filter(List<Nifty> optionDataList, String optionType) {
        return optionDataList.stream()
                .filter(n -> n.getOptionType().equals(optionType))
                .sorted(Comparator.comparing(Nifty::getId).reversed())
                .collect(Collectors.toList());
    }

    private List<BankNifty> filterBnf(List<BankNifty> optionDataList, String optionType) {
        return optionDataList.stream()
                .filter(n -> n.getOptionType().equals(optionType))
                .sorted(Comparator.comparing(BankNifty::getId).reversed())
                .collect(Collectors.toList());
    }


}
