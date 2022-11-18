package com.fabricio.crypto.robot.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabricio.crypto.robot.api.glassnode.model.Asset;
import com.fabricio.crypto.robot.api.glassnode.model.OHLCResponse;
import com.fabricio.crypto.robot.feign.client.GlassNodeClient;
import com.fabricio.crypto.robot.repository.MyWorkSymbolRepository;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

@Service
public class AssetService extends BaseService{
	
	@Autowired
	private MyWorkSymbolRepository myWorkSymbolRepository;

	private final Logger logger = LoggerFactory.getLogger(AssetService.class);
	
	public List<String> getAllSymbols() {
		
		try {
			
			GlassNodeClient feignClient = Feign.builder()
					.client(new OkHttpClient())
					.encoder(new GsonEncoder())
					.decoder(new GsonDecoder())
					.logger(new Slf4jLogger(OHLCResponse.class))
					.target(GlassNodeClient.class, "https://api.glassnode.com/v1/metrics");
			
			List<Asset> assetList = feignClient.getAllAssets(apiKeyGlassnode);
			List<String> symbolList = new ArrayList<String>();
			for(Asset asset : assetList) {
				logger.debug("Asset : {}, {}", asset.getSymbol(), asset.getName());
				symbolList.add(asset.getSymbol().toUpperCase());
			}
			
			List<String> commonSymbolList = myWorkSymbolRepository.getAllSymbolsByList(symbolList);
			for(String s : commonSymbolList) {
				logger.debug("\tCommon symbol: {}", s);
			}
			return commonSymbolList;
			
		}catch (Exception e) {
			logger.error("PROBLEM UPDATING OHLCP PRICE VIA GLASSNODE: {}", e.toString());
		}
		return new ArrayList<String>();
	}
}
