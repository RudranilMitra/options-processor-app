package com.optionsprocessor.helper;

import com.optionsprocessor.model.OptionEvent;
import com.optionsprocessor.model.StrikePrice;
import lombok.SneakyThrows;
import org.apache.tomcat.util.json.JSONParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class OptionEventTransformer {
    @SneakyThrows
    public static OptionEvent stringToJsonObject(String message) {
        JSONParser jsonParser = new JSONParser(message);
        LinkedHashMap map = (LinkedHashMap) jsonParser.parse();

        OptionEvent optionEvent = OptionEvent
                .builder()
                .symbol((String) map.get("symbol"))
                .status((String) map.get("status"))
                .strategy((String) map.get("strategy"))
                .isDelayed((Boolean) map.get("isDelayed"))
                .isIndex((Boolean) map.get("isIndex"))
                .underlyingPrice((BigDecimal) map.get("underlyingPrice")).build();
        optionEvent.setCallStrikePrices(getCallKeys(map));
        optionEvent.setPutStrikePrices(getPutKeys(map));

        return optionEvent;
    }

    private static List<StrikePrice> getCallKeys(LinkedHashMap optionEventMap) {
        LinkedHashMap optionTypeMap = ((LinkedHashMap) optionEventMap.get("callExpDateMap"));
        return getExpirationDateKeys(optionTypeMap);
    }

    private static List<StrikePrice> getPutKeys(LinkedHashMap optionEventMap) {
        LinkedHashMap optionTypeMap = ((LinkedHashMap) optionEventMap.get("putExpDateMap"));
        return getExpirationDateKeys(optionTypeMap);
    }

    private static List<StrikePrice> getExpirationDateKeys(LinkedHashMap optionTypeMap) {
        List<StrikePrice> strikePrices = null;
        Set expirationDates = optionTypeMap.keySet();
        Iterator i = expirationDates.iterator();
        while (i.hasNext()) {
            if (strikePrices == null)
                strikePrices = new LinkedList<>();
            String expirationDateKey = (String) i.next();
            getStrikePrice((LinkedHashMap) optionTypeMap.get(expirationDateKey), strikePrices);
        }
        return strikePrices;
    }

    private static void getStrikePrice(LinkedHashMap expirationDateMap, List<StrikePrice> strikePriceList) {
        StrikePrice strikePrice = null;
        Set strikePrices = expirationDateMap.keySet();
        Iterator i = strikePrices.iterator();
        while (i.hasNext()) {
            String strikePriceKey = (String) i.next();
            LinkedHashMap optionDetail = (LinkedHashMap) ((ArrayList) expirationDateMap.get(strikePriceKey)).get(0);
            strikePrice = StrikePrice
                    .builder()
                    .putCall((String) optionDetail.get("putCall"))
                    .symbol((String) optionDetail.get("symbol"))
                    .description((String) optionDetail.get("description"))
                    .exchangeName((String) optionDetail.get("exchangeName"))
                    .bid((BigDecimal) optionDetail.get("bid"))
                    .ask((BigDecimal) optionDetail.get("ask"))
                    .last((BigDecimal) optionDetail.get("last"))
                    .mark((BigDecimal) optionDetail.get("mark"))
                    .bidSize((BigInteger) optionDetail.get("bidSize"))
                    .askSize((BigInteger) optionDetail.get("askSize"))
                    .bidAskSize((String) optionDetail.get("bidAskSize"))
                    .lastSize((BigInteger) optionDetail.get("lastSize"))
                    .highPrice((BigDecimal) optionDetail.get("highPrice"))
                    .lowPrice((BigDecimal) optionDetail.get("lowPrice"))
                    .openPrice((BigDecimal) optionDetail.get("openPrice"))
                    .closePrice((BigDecimal) optionDetail.get("closePrice"))
                    .totalVolume((BigInteger) optionDetail.get("totalVolume"))
                    .tradeDate(optionDetail.get("tradeDate"))
                    .tradeTimeInLong((BigInteger) optionDetail.get("tradeTimeInLong"))
                    .quoteTimeInLong((BigInteger) optionDetail.get("quoteTimeInLong"))
                    .netChange((BigDecimal) optionDetail.get("netChange"))
                    .volatility(
                            optionDetail.get("volatility") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("volatility")
                    )
                    .delta(
                            optionDetail.get("delta") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("delta")
                    )
                    .gamma(
                            optionDetail.get("gamma") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("gamma")
                    )
                    .theta(
                            optionDetail.get("theta") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("theta")
                    )
                    .vega(
                            optionDetail.get("vega") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("vega")
                    )
                    .rho(
                            optionDetail.get("rho") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("rho")
                    )
                    .openInterest((BigInteger) optionDetail.get("openInterest"))
                    .timeValue((BigDecimal) optionDetail.get("timeValue"))
                    .theoreticalOptionValue(
                            optionDetail.get("theoreticalOptionValue") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("theoreticalOptionValue")
                    )
                    .theoreticalVolatility(
                            optionDetail.get("theoreticalVolatility") instanceof String ? new BigDecimal(0) : (BigDecimal) optionDetail.get("theoreticalVolatility")
                    )
                    .optionDeliverablesList(optionDetail.get("optionDeliverablesList"))
                    .strikePrice((BigDecimal) optionDetail.get("strikePrice"))
                    .expirationDate((BigInteger) optionDetail.get("expirationDate"))
                    .daysToExpiration((BigInteger) optionDetail.get("daysToExpiration"))
                    .expirationType((String) optionDetail.get("expirationType"))
                    .lastTradingDay((BigInteger) optionDetail.get("lastTradingDay"))
                    .multiplier((BigDecimal) optionDetail.get("multiplier"))
                    .settlementType((String) optionDetail.get("settlementType"))
                    .deliverableNote((String) optionDetail.get("deliverableNote"))
                    .isIndexOption((Boolean) optionDetail.get("isIndexOption"))
                    .percentChange((BigDecimal) optionDetail.get("percentChange"))
                    .markChange((BigDecimal) optionDetail.get("markChange"))
                    .markPercentChange((BigDecimal) optionDetail.get("markPercentChange"))
                    .nonStandard((Boolean) optionDetail.get("nonStandard"))
                    .inTheMoney((Boolean) optionDetail.get("inTheMoney"))
                    .mini((Boolean) optionDetail.get("mini"))
                    .build();
            strikePriceList.add(strikePrice);
        }
    }
}
