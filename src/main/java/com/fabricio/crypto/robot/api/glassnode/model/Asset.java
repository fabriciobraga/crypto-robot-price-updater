package com.fabricio.crypto.robot.api.glassnode.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

	private String symbol;
	private String name;
	private Boolean isERC20;
	private List<String> exchanges;
	
	
	
}
