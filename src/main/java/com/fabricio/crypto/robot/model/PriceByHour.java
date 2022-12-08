package com.fabricio.crypto.robot.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class PriceByHour {

	@Id
	@GeneratedValue
	private long id;

	@NonNull
	@JsonFormat(pattern="dd/MMM HH:mm:ss")
	@Column
	private Date dtPrice;
	
	@NonNull
	@Column
	private BigDecimal price;
	
	@NonNull
	@Column
	private BigDecimal ammount;
	
	@NonNull
	@Column
	private BigDecimal macd;
	
	@NonNull
	@Column
	private BigDecimal forceIndex;
	
	@Column
	private BigDecimal forceIndexSoften;
	
	@NonNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
	private MyWorkSymbol symbol;
	
	@NonNull
	@Column
	private BigDecimal variation24Hours;
	
	@NonNull
	@Column
	private BigDecimal rsi;
	
	@NonNull
	@Column
	private BigDecimal weightedAvgPrice;
	
	@NonNull
	@Column
	private BigDecimal volumeBuy;
	
	@NonNull
	@Column
	private BigDecimal volumeSell;
}
