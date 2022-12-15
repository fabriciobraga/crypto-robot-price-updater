package com.fabricio.crypto.robot.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OhlcId implements Serializable{

	private Date dtOpen;
	
	private String symbol;
	
	private String frequencyInterval;
}
