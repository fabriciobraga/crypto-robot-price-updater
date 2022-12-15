package com.fabricio.crypto.robot.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fabricio.crypto.robot.service.AssetService;
import com.fabricio.crypto.robot.service.OHLCPriceService;
import com.fabricio.crypto.robot.service.PriceService;

@Component
public class PriceScheduler {

	@Autowired
	private OHLCPriceService ohlcPriceService;
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private PriceService priceService;
	
	@Scheduled(cron = "0 10,30,50 * * * *")
	@Async
	public void runOHLCUdpate() {
		
		ohlcPriceService.updatePriceGlassNode();
	}
	
	@Scheduled(cron = "0 6,16,26,36,46,56 * * * *")
	@Async
	public void test() {
		
		priceService.updatePrice();
	}
}
