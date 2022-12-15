package com.fabricio.crypto.robot.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;

import com.fabricio.crypto.robot.api.glassnode.model.OHLCResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(OhlcId.class)
public class Ohlc {

	@Id
	private Date dtOpen;
	
	@Id
	@Column(length = 10)
	private String symbol;
	
	@Id
	@Column(length = 10)
	private String frequencyInterval;
	
	@Column
	private BigDecimal openPrice;
	
	@Column
	private BigDecimal closePrice;
	
	@Column
	private BigDecimal highPrice;
	
	@Column
	private BigDecimal lowPrice;
	
	@Column
	private Date dtCreated;
	
	public Ohlc(OHLCResponse ohlcResponse, String symbol, String frequencyInterval) {
		
		this.setSymbol(symbol);
		this.setFrequencyInterval(frequencyInterval);
		this.setDtOpen(new Date(ohlcResponse.getT()*1000));
		this.setClosePrice(ohlcResponse.getO().getC());
		this.setHighPrice(ohlcResponse.getO().getH());
		this.setLowPrice(ohlcResponse.getO().getL());
		this.setOpenPrice(ohlcResponse.getO().getO());
	}
	
	@PrePersist
    void fillPersistent() {
        if(dtCreated == null) {
        	this.dtCreated = new Date();
        }
    }
	
}
