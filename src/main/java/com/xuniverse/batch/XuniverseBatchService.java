package com.xuniverse.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xuniverse.commons.GeocodeXyVo;
import com.xuniverse.general.AptRealTransactionDetailVo;

@Service
public class XuniverseBatchService {

	@Autowired
	private XuniverseBatchMapper xuniverseBatchMapper;

	/**
	 * 법정동코드 조회 후 ............ 아파트 실거래가 조회
	 * @return
	 */
	public String getRTMSDataSvcAptTradeDev() {
		// 법정동코드 조회
		List<LegalDongCodeVo> legalDongCodeList= xuniverseBatchMapper.selectLegalDongCodeList();
		// 법정동코드 순회하며 부동산거래내역 조회
		System.out.println(legalDongCodeList.size());

//		Stream<LegalDongCodeVo> parallelStream = legalDongCodeList.parallelStream();
//		parallelStream.map(vo -> {
//			try {
//				return getAptRealTransactionDetail(vo.getLegalDongCodeFront());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return 1;
//		}).count();

		legalDongCodeList.stream().forEach(vo -> {
			try {
//				getAptRealTransactionDetail(vo.getLegalDongCodeFront());
				getAptRealTransactionDetail(vo.getLegalDongCodeFront());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});


		return "a";
	}

	/* 아파트 실거래가 조회 및 적재
	 *
	 *
	 */
	public int getAptRealTransactionDetail(String legalDongCodeFront) throws Exception {
    	//http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?LAWD_CD=11110&DEAL_YMD=201512&serviceKey=서비스키
        StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=MvP5mStvWZx5qmz9h0LauodR5vQUFAy7rfouQ9nYEEVBLztEcZdr2Ul6FZn7LT2SdKwhSq9sggfB%2B%2F4qFyraKw%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("9999999", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("LAWD_CD","UTF-8") + "=" + URLEncoder.encode(legalDongCodeFront, "UTF-8")); /*지역코드*/
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD","UTF-8") + "=" + URLEncoder.encode("202006", "UTF-8")); /*계약월*/
        URL url = new URL(urlBuilder.toString());
        System.out.println("url: "+url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        BufferedReader br = null;
        //DocumentBuilderFactory 생성
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;


        // xml 파싱하기
        InputSource is = new InputSource(new StringReader(sb.toString()));
        builder = factory.newDocumentBuilder();
        doc = builder.parse(is);
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        // XPathExpression expr = xpath.compile("/response/body/items/item");
        XPathExpression expr = xpath.compile("//items/item");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        AptRealTransactionDetailVo vo = new AptRealTransactionDetailVo();
        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList child = nodeList.item(i).getChildNodes();
            for (int j = 0; j < child.getLength(); j++) {
                Node node = child.item(j);
                System.out.println("j: "+j);
                System.out.println("현재 노드 이름 : " + node.getNodeName());
//                System.out.println("현재 노드 타입 : " + node.getNodeType());
                System.out.println("현재 노드 값 : " + node.getTextContent());
//                System.out.println("현재 노드 네임스페이스 : " + node.getPrefix());
//                System.out.println("현재 노드의 다음 노드 : " + node.getNextSibling());
                System.out.println("");


                if(node.getNodeName().equals("거래금액"))vo.setTransactionAmount(node.getTextContent().trim());
                if(node.getNodeName().equals("건축년도"))vo.setConstructionYeae(node.getTextContent().trim());
                if(node.getNodeName().equals("년"))vo.setDealYear(node.getTextContent().trim());
                if(node.getNodeName().equals("도로명"))vo.setRoadName(node.getTextContent().trim());
                if(node.getNodeName().equals("도로명건물본번호코드"))vo.setRoadNameBuildingMainNumber(node.getTextContent().trim());
                if(node.getNodeName().equals("도로명건물부번호코드"))vo.setRoadNameBuildingSubNumber(node.getTextContent().trim());
                if(node.getNodeName().equals("도로명시군구코드"))vo.setRoadNameDivisionCode(node.getTextContent().trim());
                if(node.getNodeName().equals("도로명일련번호코드"))vo.setRoadNameSerialNumberCode(node.getTextContent().trim());
                if(node.getNodeName().equals("도로명지상지하코드"))vo.setRoadNameGroundBasementCode(node.getTextContent().trim());
                if(node.getNodeName().equals("도로명코드"))vo.setRoadNameCode(node.getTextContent().trim());
                if(node.getNodeName().equals("법정동"))vo.setLegalDong(node.getTextContent().trim());
                if(node.getNodeName().equals("법정동본번코드"))vo.setLegalDongMainCode(node.getTextContent().trim());
                if(node.getNodeName().equals("법정동부번코드"))vo.setLegalDongSubCode(node.getTextContent().trim());
                if(node.getNodeName().equals("법정동시군구코드"))vo.setLegalDongDivisionCode1(node.getTextContent().trim());
                if(node.getNodeName().equals("법정동읍면동코드"))vo.setLegalDongDivisionCode2(node.getTextContent().trim());
                if(node.getNodeName().equals("법정동지번코드"))vo.setLegalDongLotNumberCode(node.getTextContent().trim());
                if(node.getNodeName().equals("아파트"))vo.setApartmentName(node.getTextContent().trim());
                if(node.getNodeName().equals("월"))vo.setDealMonth(node.getTextContent().trim());
                if(node.getNodeName().equals("일"))vo.setDealDay(node.getTextContent().trim());
                // 19 일련번호 제외
                if(node.getNodeName().equals("전용면적"))vo.setExclusiveArea(node.getTextContent().trim());
                if(node.getNodeName().equals("지번"))vo.setLotNumber(node.getTextContent().trim());
                if(node.getNodeName().equals("지역코드"))vo.setRegionCode(node.getTextContent().trim());
                if(node.getNodeName().equals("층"))vo.setFloor(node.getTextContent().trim());
            }
            vo.setTransactionId(UUID.randomUUID().toString());
            if (!vo.getTransactionAmount().isEmpty()) {
            	xuniverseBatchMapper.insertAptRealTransactionDetail(vo);
			}
//            vo = null;
        }
		return 1;
	}

	/* 아파트 실거래가 조회 및 적재 sql문에서 foreach................메모리부족
	 *
	 *
	 */
	public int getAptRealTransactionDetail_forEach(String legalDongCodeFront) throws Exception {
    	//http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?LAWD_CD=11110&DEAL_YMD=201512&serviceKey=서비스키
        StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=MvP5mStvWZx5qmz9h0LauodR5vQUFAy7rfouQ9nYEEVBLztEcZdr2Ul6FZn7LT2SdKwhSq9sggfB%2B%2F4qFyraKw%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("9999999", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("LAWD_CD","UTF-8") + "=" + URLEncoder.encode(legalDongCodeFront, "UTF-8")); /*지역코드*/
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD","UTF-8") + "=" + URLEncoder.encode("202005", "UTF-8")); /*계약월*/
        URL url = new URL(urlBuilder.toString());
        System.out.println("url: "+url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        BufferedReader br = null;
        //DocumentBuilderFactory 생성
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;


        // xml 파싱하기
        InputSource is = new InputSource(new StringReader(sb.toString()));
        builder = factory.newDocumentBuilder();
        doc = builder.parse(is);
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        // XPathExpression expr = xpath.compile("/response/body/items/item");
        XPathExpression expr = xpath.compile("//items/item");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        List<AptRealTransactionDetailVo> aptRealTransactionDetailVoList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList child = nodeList.item(i).getChildNodes();

            AptRealTransactionDetailVo vo = new AptRealTransactionDetailVo();
            for (int j = 0; j < child.getLength(); j++) {
                Node node = child.item(j);
                if (node.getNodeName().equals("거래금액")) 			vo.setTransactionAmount(node.getTextContent().trim());
                if (node.getNodeName().equals("건축년도")) 			vo.setConstructionYeae(node.getTextContent().trim());
                if (node.getNodeName().equals("년")) 				vo.setDealYear(node.getTextContent().trim());
                if (node.getNodeName().equals("도로명")) 				vo.setRoadName(node.getTextContent().trim());
                if (node.getNodeName().equals("도로명건물본번호코드")) 	vo.setRoadNameBuildingMainNumber(node.getTextContent().trim());
                if (node.getNodeName().equals("도로명건물부번호코드")) 	vo.setRoadNameBuildingSubNumber(node.getTextContent().trim());
                if (node.getNodeName().equals("도로명시군구코드")) 		vo.setRoadNameDivisionCode(node.getTextContent().trim());
                if (node.getNodeName().equals("도로명일련번호코드")) 		vo.setRoadNameSerialNumberCode(node.getTextContent().trim());
                if (node.getNodeName().equals("도로명지상지하코드")) 		vo.setRoadNameGroundBasementCode(node.getTextContent().trim());
                if (node.getNodeName().equals("도로명코드")) 			vo.setRoadNameCode(node.getTextContent().trim());
                if (node.getNodeName().equals("법정동")) 				vo.setLegalDong(node.getTextContent().trim());
                if (node.getNodeName().equals("법정동본번코드")) 		vo.setLegalDongMainCode(node.getTextContent().trim());
                if (node.getNodeName().equals("법정동부번코드")) 		vo.setLegalDongSubCode(node.getTextContent().trim());
                if (node.getNodeName().equals("법정동시군구코드")) 		vo.setLegalDongDivisionCode1(node.getTextContent().trim());
                if (node.getNodeName().equals("법정동읍면동코드")) 		vo.setLegalDongDivisionCode2(node.getTextContent().trim());
                if (node.getNodeName().equals("법정동지번코드")) 		vo.setLegalDongLotNumberCode(node.getTextContent().trim());
                if (node.getNodeName().equals("아파트")) 				vo.setApartmentName(node.getTextContent().trim());
                if (node.getNodeName().equals("월")) 				vo.setDealMonth(node.getTextContent().trim());
                if (node.getNodeName().equals("일")) 				vo.setDealDay(node.getTextContent().trim());
                // 19 일련번호 제외
                if (node.getNodeName().equals("전용면적")) 			vo.setExclusiveArea(node.getTextContent().trim());
                if (node.getNodeName().equals("지번")) 				vo.setLotNumber(node.getTextContent().trim());
                if (node.getNodeName().equals("지역코드")) 			vo.setRegionCode(node.getTextContent().trim());
                if (node.getNodeName().equals("층")) 				vo.setFloor(node.getTextContent().trim());
            }
            vo.setTransactionId(UUID.randomUUID().toString());
            aptRealTransactionDetailVoList.add(vo);
//            vo = null;
            if (i%100 == 0) {
            	xuniverseBatchMapper.insertAptRealTransactionDetail_forEach(aptRealTransactionDetailVoList);
    		} else if (i == nodeList.getLength()-1) {
    			xuniverseBatchMapper.insertAptRealTransactionDetail_forEach(aptRealTransactionDetailVoList);
    		}
        }


		return 1;
	}

	/* Naver Openapi Geocode 조회
	 *
	 *
	 */
	public String searchGeocodeNaverApi() throws Exception {
		System.out.println("CALL: searchGeocodeNaverApi");
		String clientId = "8yviwlp4q3";
		String clientKey = "I2wuVO9ykytd7k69eC530wtnPxMhLg80yxO2Z9jV";

		List<GeocodeXyVo> geocodeXyVos = getSearchGeocodeTarget();
		for(GeocodeXyVo geocodeXyVo: geocodeXyVos) {
			StringBuilder urlBuilder = new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode");
			urlBuilder.append("?" + URLEncoder.encode("query","UTF-8") + "=" + URLEncoder.encode(geocodeXyVo.getRoadName(), "UTF-8"));

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "8yviwlp4q3");
			conn.setRequestProperty("X-NCP-APIGW-API-KEY", "I2wuVO9ykytd7k69eC530wtnPxMhLg80yxO2Z9jV");

			BufferedReader rd;
			if(conn.getResponseCode() == 200) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			conn.disconnect();
//			System.out.println(sb.toString());

			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(sb.toString());
			if(jsonElement.isJsonObject()) {
				JsonObject jsonObj = jsonElement.getAsJsonObject();
				JsonArray jsonArray = jsonObj.getAsJsonArray("addresses");
				if (jsonArray.size() > 0) {

					JsonObject object = (JsonObject) jsonArray.get(0);
					String x = object.get("x").getAsString();
					String y = object.get("y").getAsString();
					geocodeXyVo.setX(x);
					geocodeXyVo.setY(y);
					xuniverseBatchMapper.insertGeocodeXY(geocodeXyVo);
				}
			}
		}
		System.out.println("searchGeocodeNaverApi finished");
		return "2";
	}

	private List<GeocodeXyVo> getSearchGeocodeTarget() {
		System.out.println("CALL : getSearchGeocodeTarget");
		return xuniverseBatchMapper.getSearchGeocodeTarget();
	}

}
