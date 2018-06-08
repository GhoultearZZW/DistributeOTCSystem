package com.tradergateway.Tools;

import com.tradergateway.model.Blotter;
import com.tradergateway.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by homepppp on 2018/5/28.
 */
public class ConvertBlotter {
    public Blotter getBlotter(String Product, String Period, String Broker, Double Price, int Quantity, String InitialorTrader,
                              String InitialCompany, int InitialorSide, String CompleteTrader, String CompleteCompany, int CompleteSide) {
        Blotter blotter = new Blotter();

        blotter.setQuantity(Quantity);
        blotter.setBroker(Broker);
        blotter.setCompletionCompany(CompleteCompany);
        blotter.setCompletionSide(CompleteSide);
        blotter.setProduct(Product);
        blotter.setPeriod(Period);
        blotter.setPrice(Price);
        blotter.setInitiatorTrader(InitialorTrader);
        blotter.setInitiatorCompany(InitialCompany);
        blotter.setInitiatorSide(InitialorSide);
        blotter.setCompletionTrader(CompleteTrader);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        blotter.setDealTime(df.format(date));

        return blotter;
    }
}
