package com.fabricio.crypto.robot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fabricio.crypto.robot.service.AssetService;
import com.fabricio.crypto.robot.service.OHLCPriceService;

@Component
public class PriceScheduler {

	@Autowired
	private OHLCPriceService ohlcPriceService;
	
	@Autowired
	private AssetService assetService;
	
	@Scheduled(cron = "0 5,10,15,20,23,25,30,35,40,45,50,55 * * * *")
	@Async
	public void runOHLCUdpate() {
		
		ohlcPriceService.updatePriceGlassNode();
	}
	
//	@Scheduled(cron = "0 5,10,15,20,23,25,30,35,40,45,50,55 * * * *")
//	@Async
//	public void test() {
//		
//		assetService.getAllSymbols();
//	}
}
