package com.fabricio.crypto.robot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

	@Value("${api.key.glassnode}")
	protected String apiKeyGlassnode;
}
