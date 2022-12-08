package com.fabricio.crypto.robot.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fabricio.crypto.robot.api.glassnode.model.OHLCResponse;
import com.fabricio.crypto.robot.feign.client.GlassNodeClient;
import com.fabricio.crypto.robot.model.Ohlc;
import com.fabricio.crypto.robot.model.OhlcId;
import com.fabricio.crypto.robot.repository.MyWorkSymbolRepository;
import com.fabricio.crypto.robot.repository.OhlcRepository;
import com.fabricio.crypto.robot.util.Constants;
import com.fabricio.crypto.robot.util.Util;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

@Service
public class OHLCPriceService extends BaseService{
	
	@Autowired
	private OhlcRepository ohlcRepository;
	
	@Autowired
	private AssetService assetService;

	@Autowired
	private MyWorkSymbolRepository myWorkSymbolRepository;
	
	private final Logger logger = LoggerFactory.getLogger(OHLCPriceService.class);
	
	@Async
	public void updatePriceGlassNode() {
		
		try {
			
			int candelsUpdated = 0;
			GlassNodeClient feignClient = Feign.builder()
					.client(new OkHttpClient())
					.encoder(new GsonEncoder())
					.decoder(new GsonDecoder())
					.logger(new Slf4jLogger(OHLCResponse.class))
					.target(GlassNodeClient.class, "https://api.glassnode.com/v1/metrics/market");
			
			for(String symbol : assetService.getAllSymbols()) {
				
				List<OHLCResponse> ohlcList = feignClient.getCandles(symbol.toUpperCase(), (Util.subtrairHoras(new Date(), 20).getTime()/1000), (new Date().getTime()/1000), Constants.GLASSNODE_CANDLESTICK_INTERVAL_1H, apiKeyGlassnode);
				for(OHLCResponse ohlc : ohlcList) {
					logger.debug("Time : {}", Util.formatterDateAndHour.format(new Date(ohlc.getT()*1000)));
					logger.debug("Open Price : {}", ohlc.getO().getO());
					logger.debug("-----------------");
					
					if(!ohlcRepository.findById(new OhlcId(new Date(ohlc.getT()*1000), symbol, Constants.CANDLE_INTERVAL_1H)).isPresent()) {
						ohlcRepository.save(new Ohlc(ohlc, symbol.toUpperCase(), Constants.CANDLE_INTERVAL_1H));
						candelsUpdated++;
					}
				}
				sleep(10000);
			}
			if(candelsUpdated > 0) {
				logger.info("Updating price...");
				logger.info("{} candles updated", candelsUpdated);
			}
			
			
		}catch (Exception e) {
			logger.error("PROBLEM UPDATING OHLCP PRICE VIA GLASSNODE: {}", e.toString());
		}
	}
	
	
}
