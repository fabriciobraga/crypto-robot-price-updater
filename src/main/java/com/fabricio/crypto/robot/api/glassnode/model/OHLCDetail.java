package com.fabricio.crypto.robot.api.glassnode.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OHLCDetail {

	@JsonAlias("c")
	private BigDecimal c;
	
	@JsonAlias("h")
	private BigDecimal h;
	
	@JsonAlias("l")
	private BigDecimal l;
	
	@JsonAlias("o")
	private BigDecimal o;
}
