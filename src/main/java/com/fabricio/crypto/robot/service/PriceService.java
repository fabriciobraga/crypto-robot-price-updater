package com.fabricio.crypto.robot.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabricio.crypto.robot.api.glassnode.model.Asset;
import com.fabricio.crypto.robot.api.glassnode.model.OHLCResponse;
import com.fabricio.crypto.robot.api.glassnode.model.Price;
import com.fabricio.crypto.robot.feign.client.GlassNodeClient;
import com.fabricio.crypto.robot.model.MyWorkSymbol;
import com.fabricio.crypto.robot.model.PriceByHour;
import com.fabricio.crypto.robot.repository.MyWorkSymbolRepository;
import com.fabricio.crypto.robot.repository.OhlcRepository;
import com.fabricio.crypto.robot.repository.PriceByHourRepository;
import com.fabricio.crypto.robot.util.Constants;
import com.fabricio.crypto.robot.util.Util;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

@Service
public class PriceService extends BaseService{
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private PriceByHourRepository priceByHourRepository;
	
	@Autowired
	private MyWorkSymbolRepository myWorkSymbolRepository;
	
	@Autowired
	private OhlcRepository ohlcRepository;
	
	@Autowired
	private MarketIndexService marketIndexService;
	
	private final Logger logger = LoggerFactory.getLogger(OHLCPriceService.class);

	public void updatePrice() {
		
		try {
			
			GlassNodeClient feignClient = Feign.builder()
					.client(new OkHttpClient())
					.encoder(new GsonEncoder())
					.decoder(new GsonDecoder())
					.logger(new Slf4jLogger(OHLCResponse.class))
					.target(GlassNodeClient.class, "https://api.glassnode.com/v1/metrics/market");
			
			for(String asset : assetService.getAllSymbols()) {
				
				List<Price> priceList = feignClient.getPrices(asset, (Util.subtrairMinutos(new Date(), 20).getTime()/1000), (new Date().getTime()/1000), Constants.GLASSNODE_CANDLESTICK_INTERVAL_10M, apiKeyGlassnode);
//				for(Price price : priceList) {
//					logger.debug("{}: ", asset);
//					logger.debug("\tTime : {}", Util.formatterDateAndHour.format(new Date(price.getT()*1000)));
//					logger.debug("\tPrice: {}", price.getV());
//				}
				if(!priceList.isEmpty()) {
					logger.debug("{}: ", asset);
					logger.debug("\tTime : {}", Util.formatterDateAndHour.format(new Date(priceList.get(priceList.size() - 1).getT()*1000)));
					logger.debug("\tPrice: {}", priceList.get(priceList.size() - 1).getV());
					priceByHourRepository.save(buildPriceObject(asset, priceList.get(priceList.size() - 1)));
				}else {
					logger.warn("ATENÇÃO, PREÇO NÃO RETORNADO PARA MOEDA {}", asset);
				}
			}
			
		}catch(Exception e) {
			logger.error("PROBLEM UPDATING PRICE : {}", e.toString());
		}
	}
	
	private PriceByHour buildPriceObject(String asset, Price price) {
		
		try {
		
			MyWorkSymbol symbol = myWorkSymbolRepository.findByName(asset.toUpperCase());
			PriceByHour pbh = new PriceByHour();
			pbh.setSymbol(symbol);
			pbh.setAmmount(new BigDecimal(1000));
			pbh.setDtPrice(new Date(price.getT()*1000));
			pbh.setForceIndex(new BigDecimal(10));
			pbh.setForceIndexSoften(new BigDecimal(10));
			pbh.setMacd(new BigDecimal(10)); // TODO calcular depois
			pbh.setPrice(price.getV());
			pbh.setVariation24Hours(new BigDecimal(2));
			pbh.setVolumeBuy(new BigDecimal(1000));
			pbh.setVolumeSell(new BigDecimal(1000));
			pbh.setWeightedAvgPrice(price.getV());
			
			//RSI:
			pbh.setRsi(marketIndexService.calculaRSICandleStick(symbol.getSymbol(), ohlcRepository.findLastCandles(symbol.getSymbol(), Constants.CANDLE_INTERVAL_1H, 14), 14));
			
			return pbh;
			
		}catch(Exception e) {
			logger.error("PROBLEM BUILDING PRICE OBJECT : {}", e.toString());
		}
		
		return null;
	}
}
