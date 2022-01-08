package com.xuniverse.front.map;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xuniverse.commons.GeocodeXyVo;

@Service
public class XuniverseFrontMainService {
	@Autowired
	private XuniverseFrontMainMapper xuniverseFrontMainMapper;

	public String getAptRealTransData() {
		List<RealTransactionAptTradeVo> aptRealTransData = xuniverseFrontMainMapper.getAptRealTransData(); // 서울특별시 강남구
		List<RealTransactionAptTradeVo> geocodeList = xuniverseFrontMainMapper.getAllGeocode();

//		for (AptRealTransDataVo outerVo : aptRealTransData) {
//			for (AptRealTransDataVo innerVo : geocodeList) {
//				if (outerVo.getAdministrativeDistrictLev1().equals(innerVo.getAdministrativeDistrictLev1())
//						&& outerVo.getAdministrativeDistrictLev2().equals(innerVo.getAdministrativeDistrictLev1())
//						&& outerVo.getAdministrativeDistrictLev2().equals(innerVo.getAdministrativeDistrictLev1())
//						&& outerVo.getRoadName().equals(innerVo.getRoadName())
//						) {
//					outerVo.setX(innerVo.getX());
//					outerVo.setY(innerVo.getY());
//				}
//			}
//		}

//		JOOQ
//		List<AptRealTransDataVo> result = aptRealTransData.parallelStream()
//				.flatMap(v1 -> geocodeList.parallelStream().map(v2 -> tuple(v1, v2)))
//				.filter(t -> t.v1.getAdministrativeDistrictLev1().equals(t.v2.getAdministrativeDistrictLev1())
//							&& t.v1.getAdministrativeDistrictLev2().equals(t.v2.getAdministrativeDistrictLev2())
//							&& t.v1.getAdministrativeDistrictLev3().equals(t.v2.getAdministrativeDistrictLev3())
//							&& t.v1.getRoadName().equals(t.v2.getRoadName())) // join 조건
//				.map(t -> t.v1) // t.v1 = userList, t.v2 = userRoleList
//				.distinct() // 중복 제거
//				.collect(Collectors.toList());

//		inner join
//		List<AptRealTransDataVo> result = aptRealTransData.stream()
//	            .filter(e -> geocodeList.stream()
//	                         .anyMatch(f -> e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
//	                        		 && e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())
//	                        		 && e.getAdministrativeDistrictLev3().equals(f.getAdministrativeDistrictLev3())
//	                        		 && e.getRoadName().equals(f.getRoadName()))
//	            		)
//	            .collect(Collectors.toList());

		List<RealTransactionAptTradeVo> result = aptRealTransData.parallelStream()
				.map(e -> {
					for (RealTransactionAptTradeVo f : geocodeList) {
						if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
								&&e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())
								&&e.getAdministrativeDistrictLev3().equals(f.getAdministrativeDistrictLev3())
								&&e.getRoadName().equals(f.getRoadName())) {
							e.setX(f.getX());
							e.setY(f.getY());
						}
					}
					return e;
				})
				.collect(Collectors.toList());

		Gson gson = new Gson();
		return gson.toJson(aptRealTransData);
	}

	public String getGeocode(Map<String, String> param) {
		Map<String, String> geocode = xuniverseFrontMainMapper.getGeocode(param);
		Gson gson = new Gson();
		return gson.toJson(geocode);
	}

	public String getMarkerPoint(Map<String, String> param) {
		List<RealTransactionAptTradeVo> markerPointAptTradeData = null;
		List<RealTransactionAptRentalVo> markerPointAptRentalData = null;
		List<GeocodeXyVo> geocodeList = null;
		int lev = 0;

		// ~10	시도	1
		// 11	구	2
		// 12	구	2
		// 13	동	3
		// 14	동	3
		// 15	동	3
		// 16~	개별	4
		if (Integer.parseInt(param.get("zoom")) >= 16) {
			markerPointAptTradeData = xuniverseFrontMainMapper.getMarkerPointAptTradeLev4(param);
			markerPointAptRentalData = xuniverseFrontMainMapper.getMarkerPointAptRentalLev4(param);
			geocodeList = xuniverseFrontMainMapper.getGeocodeLev4(param);
			lev = 4;
		} else if (Integer.parseInt(param.get("zoom")) >= 13 && Integer.parseInt(param.get("zoom")) <= 15) {
			markerPointAptTradeData = xuniverseFrontMainMapper.getMarkerPointAptTradeLev3(param);
			markerPointAptRentalData = xuniverseFrontMainMapper.getMarkerPointAptRentalLev3(param);
			geocodeList = xuniverseFrontMainMapper.getGeocodeLev3(param);
			lev = 3;
		} else if (Integer.parseInt(param.get("zoom")) >= 11 && Integer.parseInt(param.get("zoom")) <= 12) {
			markerPointAptTradeData = xuniverseFrontMainMapper.getMarkerPointAptTradeLev2(param);
			markerPointAptRentalData = xuniverseFrontMainMapper.getMarkerPointAptRentalLev2(param);
			geocodeList = xuniverseFrontMainMapper.getGeocodeLev2(param);
			lev = 2;
		} else if (Integer.parseInt(param.get("zoom")) <= 10) {
			markerPointAptTradeData = xuniverseFrontMainMapper.getMarkerPointAptTradeLev1(param);
			markerPointAptRentalData = xuniverseFrontMainMapper.getMarkerPointAptRentalLev1(param);
			geocodeList = xuniverseFrontMainMapper.getGeocodeLev1(param);
			lev = 1;
		}

		List<GeocodeXyVo> nGeocodeList = geocodeList;
		int nLev = lev;

		List<RealTransactionAptTradeVo> mergedMarkerPointAptTradeData = getMergedListAptTrade(markerPointAptTradeData, nGeocodeList, nLev);
		List<RealTransactionAptRentalVo> mergedMarkerPointAptRentalData = getMergedListAptRental(markerPointAptRentalData, nGeocodeList, nLev);

		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();



		JsonElement jsonElementTradeApt = gson.toJsonTree(mergedMarkerPointAptTradeData, new TypeToken<List<RealTransactionAptTradeVo>>() {}.getType());
		JsonArray jsonArrayTradeApt = jsonElementTradeApt.getAsJsonArray();
		JsonArray jsonArrayTradeRowHouse = new JsonArray();
		JsonArray jsonArrayTradeDetachedHouse = new JsonArray();
		JsonArray jsonArrayTradeOfficetel = new JsonArray();


//        // 아파트
//        apt : [],
//        // 연립 다세대
//        rowHouse : [],
//        // 단독 다가구
//        detachedHouse : [],
//        // 오피스텔
//        officetel : [],
		JsonObject jsonObjectTrade = new JsonObject();
		jsonObjectTrade.add("apt", jsonArrayTradeApt);
		jsonObjectTrade.add("rowHouse", jsonArrayTradeRowHouse);
		jsonObjectTrade.add("detachedHouse", jsonArrayTradeDetachedHouse);
		jsonObjectTrade.add("officetel", jsonArrayTradeOfficetel);

		JsonElement jsonElementRentalApt = gson.toJsonTree(mergedMarkerPointAptRentalData, new TypeToken<List<RealTransactionAptTradeVo>>() {}.getType());
		JsonArray jsonArrayRentalApt = jsonElementRentalApt.getAsJsonArray();
		JsonArray jsonArrayRentalRowHouse = new JsonArray();
		JsonArray jsonArrayRentalDetachedHouse = new JsonArray();
		JsonArray jsonArrayRentalOfficetel = new JsonArray();

		JsonObject jsonObjectRental = new JsonObject();
		jsonObjectRental.add("apt", jsonArrayRentalApt);
		jsonObjectRental.add("rowHouse", jsonArrayRentalRowHouse);
		jsonObjectRental.add("detachedHouse", jsonArrayRentalDetachedHouse);
		jsonObjectRental.add("officetel", jsonArrayRentalOfficetel);



		jsonObject.add("trade", jsonObjectTrade);
		jsonObject.add("rental", jsonObjectRental);

		return gson.toJson(jsonObject);
	}

	public String getMarkerDetail(Map<String, String> param) {
		List<RealTransactionAptTradeVo> aptTransData = xuniverseFrontMainMapper.getMarkerDetail(param);
		Gson gson = new Gson();
		return gson.toJson(aptTransData);
	}

	private List<RealTransactionAptTradeVo> getMergedListAptTrade(List<RealTransactionAptTradeVo> list1, List<GeocodeXyVo> list2, int nLev) {
		List<RealTransactionAptTradeVo> result = list1.parallelStream()
				.map(e -> {
					for (GeocodeXyVo f : list2) {
						if (nLev == 1) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						} else if (nLev == 2) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
									&&e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						} else if (nLev == 3) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
									&&e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())
									&&e.getAdministrativeDistrictLev3().equals(f.getAdministrativeDistrictLev3())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						} else if (nLev == 4) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
									&&e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())
									&&e.getAdministrativeDistrictLev3().equals(f.getAdministrativeDistrictLev3())
									&&e.getRoadName().equals(f.getRoadName())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						}
					}
					return e;
				})
				.collect(Collectors.toList());

		return result;
	}

	private List<RealTransactionAptRentalVo> getMergedListAptRental(List<RealTransactionAptRentalVo> list1, List<GeocodeXyVo> list2, int nLev) {
		List<RealTransactionAptRentalVo> result = list1.parallelStream()
				.map(e -> {
					for (GeocodeXyVo f : list2) {
						if (nLev == 1) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						} else if (nLev == 2) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
									&&e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						} else if (nLev == 3) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
									&&e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())
									&&e.getAdministrativeDistrictLev3().equals(f.getAdministrativeDistrictLev3())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						} else if (nLev == 4) {
							if (e.getAdministrativeDistrictLev1().equals(f.getAdministrativeDistrictLev1())
									&&e.getAdministrativeDistrictLev2().equals(f.getAdministrativeDistrictLev2())
									&&e.getAdministrativeDistrictLev3().equals(f.getAdministrativeDistrictLev3())
									&&e.getRoadName().equals(f.getRoadName())) {
								e.setX(f.getX());
								e.setY(f.getY());
							}
						}
					}
					return e;
				})
				.collect(Collectors.toList());

		return result;
	}
}
