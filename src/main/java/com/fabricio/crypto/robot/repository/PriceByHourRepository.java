package com.fabricio.crypto.robot.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.fabricio.crypto.robot.model.MyWorkSymbol;
import com.fabricio.crypto.robot.model.PriceByHour;

public interface PriceByHourRepository extends JpaRepository<PriceByHour, Long>{

	@Query("select u from PriceByHour u where u.symbol = ?1 order by u.dtPrice desc")
    public List<PriceByHour> findAllBySymbol(MyWorkSymbol symbol);
	
	@Query(value = "select pbh.price from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 order BY pbh.dtPrice desc limit 1", nativeQuery = true)
	public BigDecimal findLastPrice(String symbol);
	
	@Query(value = "select pbh.dtPrice from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 order BY pbh.dtPrice limit 1", nativeQuery = true)
	public Date findFirstPriceDate(String symbol);
	
	@Query(value = "select max(pbh.dtPrice) from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1", nativeQuery = true)
	public Date findLastPriceUpdateDate(String symbol);
	
	@Query(value = "SELECT sum(pbh.volumeBuy) as totalVolumeBuy from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 and pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - (?2 - 1) HOUR) and pbh.volumeBuy is not null and pbh.volumeSell is not null group BY mws.symbol, DATE(pbh.dtPrice), HOUR(pbh.dtPrice) order by pbh.dtPrice", nativeQuery = true)
	public List<BigDecimal> findLastVolumesBuy(String symbol, int hours);
	
	@Query(value = "SELECT sum(pbh.volumeSell) as totalVolumeSell from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 and pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - (?2 - 1) HOUR) and pbh.volumeBuy is not null and pbh.volumeSell is not null group BY mws.symbol, DATE(pbh.dtPrice), HOUR(pbh.dtPrice) order by pbh.dtPrice", nativeQuery = true)
	public List<BigDecimal> findLastVolumesSell(String symbol, int hours);
	
	/**
	 * Retorna a sequencia de volume de compra, minuto a minuto, no intervalo de horas passado comop parametro.
	 * 
	 * @param symbol
	 * @param hours
	 * @return
	 */
	@Query(value = "SELECT pbh.volumeBuy from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 and pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - ?2 HOUR) and pbh.volumeBuy is not null and pbh.volumeSell is not null order by pbh.dtPrice", nativeQuery = true)
	public List<BigDecimal> findVolumeBuyValuesSequence(String symbol, int hours);
	
	/**
	 * Retorna a sequencia de volume de compra, minuto a minuto, no intervalo de horas passado comop parametro.
	 * 
	 * @param symbol
	 * @param hours
	 * @return
	 */
	@Query(value = "SELECT pbh.volumeSell from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 and pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - ?2 HOUR) and pbh.volumeBuy is not null and pbh.volumeSell is not null order by pbh.dtPrice", nativeQuery = true)
	public List<BigDecimal> findVolumeSellValuesSequence(String symbol, int hours);
	
	@Transactional
	@Modifying
	@Query(value = "delete from PriceByHour where dtPrice  < DATE_ADD(NOW() , INTERVAL - ?1 DAY)", nativeQuery = true)
	public void cleanPriceByHour(int days);
	
	@Query(value = "select 100 - (((SELECT pbh.price from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 and pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - ?2 HOUR) limit 1) / (SELECT pbh.price from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 and pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - ?2 HOUR) order by pbh.dtPrice desc limit 1))* 100)", nativeQuery = true)
	public BigDecimal findPriceVariation(String symbol, int hours); 
	
	/**
	 * Retorna uma lista de preços do ativo nos minutos passados como parametro
	 * 
	 * @param symbolId
	 * @param minutes
	 * @return
	 */
	@Query(value = "select pbh.price from PriceByHour pbh where pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - ?2 MINUTE) and pbh.symbol_id = ?1 order by pbh.dtPrice", nativeQuery = true)
	public List<BigDecimal>findPriceListByMinutes(Long symbolId, int minutes);
	
	@Query(value = "select count(id) from MyBinanceTransaction mbt where mbt.user_id = ?1 and mbt.symbol = ?2 and mbt.dtTransactionSell is not NULL and mbt.commissionAssetSell is not null and mbt.dtTransactionSell > DATE_ADD(NOW(), INTERVAL - ?3 MINUTE) and price > mbt.priceSell", nativeQuery = true)
	public int countNegativeTransactions(Long userId, String symbol, int minutes);
	
	@Query(value = "select pbh.forceIndexSoften from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - ?2 MINUTE) and mws.symbol = ?1 order by pbh.dtPrice", nativeQuery = true)
	public List<BigDecimal> getForceIndexIndexList(String symbol, int hours);
	
	@Query(value = "select min(pbh.forceIndexSoften) from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1", nativeQuery = true)
	public BigDecimal getLowestHistoricalForceIndex(String symbol);
	
	@Query(value = "select max(pbh.forceIndexSoften) from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1", nativeQuery = true)
	public BigDecimal getHighestHistoricalForceIndex(String symbol);
	
	@Query(value = "select pbh.forceIndexSoften from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 order by pbh.dtPrice desc limit 1", nativeQuery = true)
	public BigDecimal getCurrentForceIndex(String symbol);
	
	@Query(value = "select pbh.rsi from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 order by pbh.dtPrice desc limit 1", nativeQuery = true)
	public BigDecimal getCurrentRSI(String symbol);
	
	@Query(value = "select pbh.forceIndexSoften from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where pbh.dtPrice > DATE_ADD(NOW(), INTERVAL - ?2 MINUTE) and mws.symbol = ?1 order by pbh.dtPrice", nativeQuery = true)
	public List<BigDecimal> getLastForceIndexList(String symbol, int minutos);
	
	@Query(value = "select avg(pbh.forceIndexSoften) from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1", nativeQuery = true)
	public BigDecimal getMediaForceIndex(String symbol);
	
	@Query(value = "select min(x.price) from(select DATE_FORMAT(pbh.dtPrice, '%d/%m/%Y %H') as 'dtPrice', avg(pbh.price) as 'price' from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1 group by mws.symbol, DATE(pbh.dtPrice), HOUR(pbh.dtPrice))x", nativeQuery = true)
	public BigDecimal getLowestPrice(String symbol);
	
	@Query(value = "select max(price) from PriceByHour pbh inner join MyWorkSymbol mws on pbh.symbol_id = mws.id where mws.symbol = ?1", nativeQuery = true)
	public BigDecimal getHightestPrice(String symbol);
}
