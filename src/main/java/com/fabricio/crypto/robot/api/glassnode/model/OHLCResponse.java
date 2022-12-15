package com.fabricio.crypto.robot.api.glassnode.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OHLCResponse {

	@JsonAlias("t")
	private long t;
	
	@JsonAlias("o")
	private OHLCDetail o;
}
