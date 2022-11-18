package com.fabricio.crypto.robot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fabricio.crypto.robot.model.Ohlc;
import com.fabricio.crypto.robot.model.OhlcId;

public interface OhlcRepository extends JpaRepository<Ohlc, OhlcId>{

}
