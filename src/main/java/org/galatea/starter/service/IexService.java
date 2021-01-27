package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexService {

  @NonNull
  private IexClient iexClient;
  private final String SPECIFIC_DATE_RANGE = "date";

  /**
   * Get all stock symbols from IEX.
   *
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols() {
    return iexClient.getAllSymbols();
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]));
    }
  }

  /**
   * Get the historical price for a stock symbol passed in for the given time range. See
   * https://iexcloud.io/docs/api/#historical-prices
   *
   * @param symbol single stock symbols to get historical price for.
   * @param range string from acceptable range choices for when to get price from
   * @param date [Optional] dateRange for if range value is "date"
   * @return a IexHistoricalPrice object for the given symbol
   */
  public List<IexHistoricalPrice> getHistoricalPricesForSymbol(
      final String symbol, final String range, final String date) {
    List<IexHistoricalPrice> historicalPrices;
    if (range.equals(SPECIFIC_DATE_RANGE)) {
      log.info("Making api request with symbol = "
          + symbol + " | range = " + range + " | date = " + date);
      historicalPrices = iexClient.getHistoricalPricesForSymbol(symbol, range, date);
    } else {
      log.info("Making api request with symbol = "
          + symbol + " | range = " + range);
      historicalPrices = iexClient.getHistoricalPricesForSymbol(symbol, range);
    }
    return historicalPrices;
  }

}



