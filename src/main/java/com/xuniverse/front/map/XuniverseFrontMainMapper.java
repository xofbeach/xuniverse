package com.xuniverse.front.map;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.xuniverse.commons.GeocodeXyVo;

@Repository
@Mapper
public interface XuniverseFrontMainMapper {
	List<RealTransactionAptTradeVo> getAptRealTransData();
	Map<String, String> getGeocode(Map<String, String> param);
	List<RealTransactionAptTradeVo> getAllGeocode();

	List<RealTransactionAptTradeVo> getMarkerPointAptTradeLev1(Map<String, String> param);
	List<RealTransactionAptTradeVo> getMarkerPointAptTradeLev2(Map<String, String> param);
	List<RealTransactionAptTradeVo> getMarkerPointAptTradeLev3(Map<String, String> param);
	List<RealTransactionAptTradeVo> getMarkerPointAptTradeLev4(Map<String, String> param);
	List<RealTransactionAptRentalVo> getMarkerPointAptRentalLev1(Map<String, String> param);
	List<RealTransactionAptRentalVo> getMarkerPointAptRentalLev2(Map<String, String> param);
	List<RealTransactionAptRentalVo> getMarkerPointAptRentalLev3(Map<String, String> param);
	List<RealTransactionAptRentalVo> getMarkerPointAptRentalLev4(Map<String, String> param);

	List<GeocodeXyVo> getGeocodeLev1(Map<String, String> param);
	List<GeocodeXyVo> getGeocodeLev2(Map<String, String> param);
	List<GeocodeXyVo> getGeocodeLev3(Map<String, String> param);
	List<GeocodeXyVo> getGeocodeLev4(Map<String, String> param);
	List<RealTransactionAptTradeVo> getMarkerDetail(Map<String, String> param);

}
