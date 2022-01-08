package com.xuniverse.front.community;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

@Service
public class XuniverseCommunityService {

	@Autowired
	private XuniverseCommunityMapper xuniverseCommunityMapper;

	public String getPostList(Map<String, String> param) {
		int postCount = xuniverseCommunityMapper.getPostCount(param);
		Pagination pagination = new Pagination(postCount, Integer.parseInt(param.get("curPage")));
		List<CommunityBoardVo> postList = xuniverseCommunityMapper.getPostList(param, pagination);

		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();

		JsonElement jsonElementPostList = gson.toJsonTree(postList, new TypeToken<List<CommunityBoardVo>>() {}.getType());
		JsonElement jsonElementPostCount = gson.toJsonTree(postCount, new TypeToken<Integer>() {}.getType());
		JsonElement jsonElementPagination = gson.toJsonTree(pagination, new TypeToken<Pagination>() {}.getType());

		jsonObject.add("postList", jsonElementPostList);
		jsonObject.add("postCount", jsonElementPostCount);
		jsonObject.add("pagination", jsonElementPagination);
		return gson.toJson(jsonObject);
	}

	public String getPostById(Map<String, String> param) {
		CommunityBoardVo post = xuniverseCommunityMapper.getPostById(param);
		List<PostViewReplyVo> postReplyList = xuniverseCommunityMapper.getPostReplyById(param);

		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();

		JsonElement jsonElementPost = gson.toJsonTree(post, new TypeToken<CommunityBoardVo>() {}.getType());
		JsonElement jsonElementPostReplyList = gson.toJsonTree(postReplyList, new TypeToken<List<PostViewReplyVo>>() {}.getType());

		jsonObject.add("post", jsonElementPost);
		jsonObject.add("replyList", jsonElementPostReplyList);
		return gson.toJson(jsonObject);
	}

	public String deletePostById(Map<String, String> param) {
		int rssultCnt = xuniverseCommunityMapper.deletePostById(param);

		if (rssultCnt == 1) {

		}
		Gson gson = new Gson();
		return gson.toJson("{RESULT: SUCCESS}");
	}

	public String registerReply(Map<String, String> param) {
		int rssultCnt = xuniverseCommunityMapper.registerReply(param);
		Gson gson = new Gson();
		return gson.toJson("{RESULT: SUCCESS}");
	}

	public String registerReReply(Map<String, String> param) {
		int rssultCnt = xuniverseCommunityMapper.registerReReply(param);
		Gson gson = new Gson();
		return gson.toJson("{RESULT: SUCCESS}");
	}

	public String modifyReReply(Map<String, String> param) {
		int rssultCnt = xuniverseCommunityMapper.modifyReReply(param);
		Gson gson = new Gson();
		return gson.toJson("{RESULT: SUCCESS}");
	}

	public String deleteReReply(Map<String, String> param) {
		int rssultCnt = xuniverseCommunityMapper.deleteReReply(param);
		Gson gson = new Gson();
		return gson.toJson("{RESULT: SUCCESS}");
	}

	public String registerWirtePost(Map<String, String> param) throws ParserConfigurationException, SAXException, IOException {
		/**
		 * img 태그 src의 base64 데이터를 file로 변환,저장하고 src를 저장한 file url로 치환합니다.
		 * before : <p><img src="data:image/png;base64,iVBORw...ggg==" alt="image"></p>
		 * after : <p><img src="https://cafeptthumb-phinf.pstatic.net/MjA.../R_DSC00811.JPG?type=w1600"></p>
		 */
		if(param.get("postId") == null) {
			int rssultCnt = xuniverseCommunityMapper.registerWirtePost(param);
		} else {
			int rssultCnt = xuniverseCommunityMapper.modifyPostById(param);
		}
		Gson gson = new Gson();
		return gson.toJson("{RESULT: SUCCESS}");
	}

	public void getImageByBlob(String base64EncodedBlob) throws ParserConfigurationException, SAXException, IOException {
		base64EncodedBlob.replaceAll("<br>", "<br/>");
		// XML 문서 파싱
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(new InputSource(new StringReader(base64EncodedBlob)));

		// root 구하기
		Element root = document.getDocumentElement();

		// root의 속성
		System.out.println("class name: " + root.getAttribute("name"));

		NodeList childeren = root.getChildNodes(); // 자식 노드 목록 get
		for(int i = 0; i < childeren.getLength(); i++){
			Node node = childeren.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){ // 해당 노드의 종류 판정(Element일 때)
				Element ele = (Element)node;
				String nodeName = ele.getNodeName();
				System.out.println("node name: " + nodeName);
				if(nodeName.equals("teacher")){
					System.out.println("node attribute: " + ele.getAttribute("name"));
				}
				else if(nodeName.equals("student")){
					// 이름이 student인 노드는 자식노드가 더 존재함
					NodeList childeren2 = ele.getChildNodes();
					for(int a = 0; a < childeren2.getLength(); a++){
						Node node2 = childeren2.item(a);
						if(node2.getNodeType() == Node.ELEMENT_NODE){
							Element ele2 = (Element)node2;
							String nodeName2 = ele2.getNodeName();
							System.out.println("node name2: " + nodeName2);
							System.out.println("node attribute2: " + ele2.getAttribute("num"));
						}
					}
				}
			}
		}
	}

	public String uploadFile(MultipartFile multipartFile) {
//		File file = param.get("file");
		System.out.println("file");
		System.out.println("file");
		System.out.println("file");

		return null;
	}

}
