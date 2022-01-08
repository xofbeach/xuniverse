package com.xuniverse.batch;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class XuniverseBatchController {

	@Autowired
	private XuniverseBatchService xuniverseBatchService;

	/**
	 * 법정동 코드(5자리) 조회 후 해당 법정동별 아파트 실거래가 조회
	 * @param request
	 * @return
	 */
    @PostMapping("/getRTMSDataSvcAptTradeDev")
    public ResponseEntity<String> getRTMSDataSvcAptTradeDev(HttpServletRequest request) {
    	System.out.println("in getRTMSDataSvcAptTradeDev");
        String getRTMSDataSvcAptTradeDev = xuniverseBatchService.getRTMSDataSvcAptTradeDev();
        System.out.println("axiosTestService-getRTMSDataSvcAptTradeDev: "+getRTMSDataSvcAptTradeDev);
        return ResponseEntity.ok(request.getRemoteAddr());
    }

    /**
     * 도로명주소로 네이버 지도 API Geocode 조회
     * @param mmp
     * @return
     * @throws Exception
     */
    @PostMapping("/searchGeocode")
	public ResponseEntity<String> searchGeocode(@RequestBody HashMap<String, String> mmp) throws Exception {
  	System.out.println("[ CALL: /api/searchGeocode ]");
		String searchNaverMap = xuniverseBatchService.searchGeocodeNaverApi();
		return ResponseEntity.ok(searchNaverMap);
  }
}
