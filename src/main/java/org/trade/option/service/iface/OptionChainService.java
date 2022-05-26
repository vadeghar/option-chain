package org.trade.option.service.iface;

import org.trade.option.client.grow.OcSymbolEnum;

public interface OptionChainService {
    public void saveOptionData(OcSymbolEnum symbol);
}
