package com.fabricio.crypto.robot.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fabricio.crypto.robot.model.Ohlc;
import com.fabricio.crypto.robot.util.Util;

@Service
public class MarketIndexService {
	
	private final Logger logger = LoggerFactory.getLogger(MarketIndexService.class);
	
	public BigDecimal calculaRSICandleStick(String symbol, List<Ohlc> csList, int periodos) {
		
		try {
			logger.debug("lista de candles {}", csList);
			logger.debug("lista de candles, size {}", csList.size());
			if(!CollectionUtils.isEmpty(csList) && csList.size() >= periodos) {
				
				BigDecimal altas = new BigDecimal(0);
				BigDecimal baixas = new BigDecimal(0);
				int totalAltas = 0;
				int totalBaixas = 0;
				
				for(int i = 0; i < periodos; i++) {
					Ohlc cs = csList.get(i);
					BigDecimal open = cs.getOpenPrice();
					BigDecimal close = cs.getClosePrice();
					//BigDecimal avg = open.add(close).divide(new BigDecimal(2), new MathContext(6, RoundingMode.HALF_UP));
					
					if(open.compareTo(close) < 0) {
						altas = altas.add(close.subtract(open));
						totalAltas++;
					}else if(open.compareTo(close) > 0) {
						baixas = baixas.add(open.subtract(close));
						totalBaixas++;
					}
				}
				
				logger.debug("open {} / close {}", Util.formatterDayAndHour.format(csList.get(0).getDtOpen()), Util.formatterDayAndHour.format(csList.get(0).getDtOpen()));
				
				BigDecimal mediaAltas = altas.divide(new BigDecimal(periodos), new MathContext(6, RoundingMode.HALF_UP));
				BigDecimal mediaBaixas = baixas.divide(new BigDecimal(periodos), new MathContext(6, RoundingMode.HALF_UP));
				
				BigDecimal result = new BigDecimal(1).add(mediaAltas.divide(mediaBaixas, new MathContext(6, RoundingMode.HALF_UP)));
				result = new BigDecimal(100).divide(result, new MathContext(6, RoundingMode.HALF_UP));
				result = new BigDecimal(100).subtract(result);
				return result;
				
			}else {
				logger.warn("COLEÇÃO VAZIA OU MENOR QUE O INDICE PASSADA PARA CALCULO DO RSI ({})", symbol);
			}
				
			
		}catch(Exception e) {
			logger.error("PROBLEMA AO CALCULAR RSI para {}: {}", symbol, e.toString());
		}
		
		return new BigDecimal(50);
	}
}
