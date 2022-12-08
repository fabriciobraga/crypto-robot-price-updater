package com.fabricio.crypto.robot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fabricio.crypto.robot.model.Ohlc;
import com.fabricio.crypto.robot.model.OhlcId;

public interface OhlcRepository extends JpaRepository<Ohlc, OhlcId>{

	@Query(value = "select * from(select * from Ohlc o where symbol = :symbol and frequencyInterval = :frequencyInterval order by dtOpen desc limit :numberOfCandles) as sub order by dtOpen asc", nativeQuery = true)
	public List<Ohlc> findLastCandles(String symbol, String frequencyInterval, int numberOfCandles);
}
