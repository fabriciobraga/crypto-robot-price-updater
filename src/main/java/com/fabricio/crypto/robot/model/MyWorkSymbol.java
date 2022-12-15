package com.fabricio.crypto.robot.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

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
public class MyWorkSymbol {
	
	@Id
	@GeneratedValue
	private long id;

	@NonNull
	@Column
	private String symbol;
	
	@Column(length = 50)
	private String minQty;
	
	@Column(length = 50)
	private String maxQty;
	
	@Column(length = 50)
	private String stepSize;
	
	private Integer scale;
	
	private Integer mediaMovelMenor;
	
	private Integer mediaMovelMaior;
	
	@Transient
	private BigDecimal tendenciaMACD;
	
	@Transient 
	private BigDecimal tendenciaMediaMovelMenor;
	
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Convert(disableConversion = true)
	private boolean enabled;
	
	@Column
	@JsonFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dtCreated;

	@Override
	public boolean equals(Object obj) {	
		return this.symbol.equals(((MyWorkSymbol)obj).getSymbol());
	}
	
	@PrePersist
    void fillPersistent() {
        if(dtCreated == null) {
        	this.dtCreated = new Date();
        	this.enabled = true;
        }
    }
	
}