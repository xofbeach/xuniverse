package com.xuniverse.front.map;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class XuniverseFrontMainController {

	@Autowired
	private XuniverseFrontMainService xuniverseFrontMainService;

    /**
     * 아파트 실거래가데이터 조회
     * @param mmp
     * @return
     */
    @PostMapping("/getAptRealTransData")
//    public ResponseEntity<String> getAptRealTransactionDetail(@RequestParam(value = "dealYear", required = false, defaultValue = "1999") String dealYear) {
	public ResponseEntity<String> getAptRealTransData(@RequestBody Map<String, String> mmp) {
//    	System.out.println("dealYear: "+dealYear);
    	System.out.println("[ CALL: /api/getAptRealTransData ]");
        String aptRealTransData = xuniverseFrontMainService.getAptRealTransData();
        return ResponseEntity.ok(aptRealTransData);
    }

    /**
     * Geocode X,Y 조회
     * @param TransactonId
     * @return Geocode X,Y
     */
    @PostMapping("/getGeocode")
	public ResponseEntity<String> getGeocodeXY(@RequestBody Map<String, String> param) {
      String geocode = xuniverseFrontMainService.getGeocode(param);
      return ResponseEntity.ok(geocode);
    }

    /**
     * Zoom 에 따른 Marker 위치를 조회한다.
     * @param param
     * @return
     */
    @PostMapping("/getMarkerPoint")
	public ResponseEntity<String> getMarkerPoint(@RequestBody Map<String, String> param) {
    	System.out.println("CALL-getMarkerPoint");
      String geocode = xuniverseFrontMainService.getMarkerPoint(param);
      return ResponseEntity.ok(geocode);
    }

    /**
     * 아파트 실거래 데이터 조회
     * @param {Map<String, String>} param 조회할 아파트 정보(시도, 시군구, 읍면동, 도로명, 단지명)
     * @return {json} 아파트 실거래 데이터
     */
    @PostMapping("/getMarkerDetail")
	public ResponseEntity<String> getMarkerDetail(@RequestBody Map<String, String> param, HttpServletRequest request) {
    	String sessionId = request.getSession(true).getId();
    	System.out.println(">>>>>SESSION ID : "+ sessionId);
    	System.out.println(">>>>COOKIES: "+request.getCookies());
      String aptTransData = xuniverseFrontMainService.getMarkerDetail(param);
      return ResponseEntity.ok(aptTransData);
    }
}
