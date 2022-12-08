package com.fabricio.crypto.robot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fabricio.crypto.robot.model.MyWorkSymbol;

public interface MyWorkSymbolRepository extends JpaRepository<MyWorkSymbol, Long>{
	
	@Query("select u from MyWorkSymbol u where u.symbol = ?1")
    public MyWorkSymbol findByName(String symbolName);

	@Query("select distinct u.symbol from MyWorkSymbol u where u.symbol in (?1) order by u.symbol")
	public List<String> getAllSymbolsByList(List<String> symbolList);
}
