package com.fabricio.crypto.robot.feign.client;

import java.util.List;

import com.fabricio.crypto.robot.api.glassnode.model.Asset;
import com.fabricio.crypto.robot.api.glassnode.model.OHLCResponse;
import com.fabricio.crypto.robot.api.glassnode.model.Price;

import feign.Param;
import feign.RequestLine;

public interface GlassNodeClient {

	@RequestLine("GET /price_usd_ohlc?a={symbol}&s={startDate}&u={endDate}&i={frequency}&f=JSON&api_key={apiKey}")
    public List<OHLCResponse> getCandles(@Param("symbol") String symbol, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("frequency") String frequency, @Param("apiKey") String apiKey);

	@RequestLine("GET /assets?api_key={apiKey}")
    public List<Asset> getAllAssets(@Param("apiKey") String apiKey);
	
	@RequestLine("GET /price_usd_close?a={symbol}&s={startDate}&u={endDate}&i={frequency}&f=JSON&api_key={apiKey}")
    public List<Price> getPrices(@Param("symbol") String symbol, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("frequency") String frequency, @Param("apiKey") String apiKey);
}
