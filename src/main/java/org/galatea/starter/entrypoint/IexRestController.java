package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.service.IexService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@Validated
@RestController
@RequiredArgsConstructor
public class IexRestController {

  @NonNull
  private IexService iexService;

  /**
   * Exposes an endpoint to get all of the symbols available on IEX.
   *
   * @return a list of all IexStockSymbols.
   */
  @GetMapping(value = "${mvc.iex.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexSymbol> getAllStockSymbols() {
    return iexService.getAllSymbols();
  }

  /**
   * Get the last traded price for each of the symbols passed in.
   *
   * @param symbols list of symbols to get last traded price for.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.iex.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbols") final List<String> symbols) {
    return iexService.getLastTradedPriceForSymbols(symbols);
  }

  /**
   * Get the historical price for a stock symbol passed in for the given time range.
   * See https://iexcloud.io/docs/api/#historical-prices
   *
   * @param symbol single stock symbols to get historical price for.
   * @param range string from acceptable range choices for when to get price from
   * @param date [Optional] date for if range value is "date"
   * @return a IexHistoricalPrice object for the given symbol
   */
  @GetMapping(value = "${mvc.iex.getHistoricalPricesPath}/{symbol}/{range}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexHistoricalPrice> getHistoricalPricesForSymbol(
      @PathVariable final String symbol,
      @PathVariable final String range,
      @RequestParam(required = false) String date
  ){
    log.info("Received endpoint request: symbol = " + symbol + " | range = " + range +  " | date = " + date);
    return iexService.getHistoricalPricesForSymbol(symbol, range, date);
  }
}
