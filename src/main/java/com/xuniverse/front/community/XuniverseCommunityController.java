package com.xuniverse.front.community;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@RestController
@RequestMapping("/api/community")
public class XuniverseCommunityController {

	@Autowired
	private XuniverseCommunityService xuniverseCommunityService;

    /**
     * 게시판 목록을 조회한다.
     * @param param
     * @return
     */
    @PostMapping("/getPostList")
	public ResponseEntity<String> getPostList(@RequestBody Map<String, String> param, HttpServletRequest request) {
    	System.out.println("CALL-getPostList");
//    	HttpSession session = request.getSession(true);
    	String sessionId = request.getSession(true).getId();
    	System.out.println(">>>>>SESSION ID : "+ sessionId);

      String postList = xuniverseCommunityService.getPostList(param);
      return ResponseEntity.ok(postList);
    }

    /**
     * Id로 컨텐츠를 조회한다.
     * @param param
     * @return
     */
    @PostMapping("/getPostById")
	public ResponseEntity<String> getPostById(@RequestBody Map<String, String> param) {
    	System.out.println("CALL-getPostById");
      String post = xuniverseCommunityService.getPostById(param);
      return ResponseEntity.ok(post);
    }

    /**
     * Id로 컨텐츠를 삭제한다.
     * @param param
     * @return
     */
    @PostMapping("/deletePostById")
	public ResponseEntity<String> deletePostById(@RequestBody Map<String, String> param) {
      String result = xuniverseCommunityService.deletePostById(param);
      return ResponseEntity.ok(result);
    }

    /**
     * 댓글을 등록한다.
     * @param param
     * @return
     */
    @PostMapping("/registerReply")
	public ResponseEntity<String> registerReply(@RequestBody Map<String, String> param) {
      String result = xuniverseCommunityService.registerReply(param);
      return ResponseEntity.ok(result);
    }

    /**
     * 대댓글을 등록한다.
     * @param param
     * @return
     */
    @PostMapping("/registerReReply")
	public ResponseEntity<String> registerReReply(@RequestBody Map<String, String> param) {
      String result = xuniverseCommunityService.registerReReply(param);
      return ResponseEntity.ok(result);
    }

    /**
     * 대댓글을 수정한다.
     * @param param
     * @return
     */
    @PostMapping("/modifyReReply")
	public ResponseEntity<String> modifyReReply(@RequestBody Map<String, String> param) {
      String result = xuniverseCommunityService.modifyReReply(param);
      return ResponseEntity.ok(result);
    }

    /**
     * 대댓글을 삭제한다.
     * @param param
     * @return
     */
    @PostMapping("/deleteReReply")
	public ResponseEntity<String> deleteReReply(@RequestBody Map<String, String> param) {
      String result = xuniverseCommunityService.deleteReReply(param);
      return ResponseEntity.ok(result);
    }

    /**
     * 게시글을 등록한다.
     * @param param
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @PostMapping("/registerPostWrite")
	public ResponseEntity<String> registerWirtePost(@RequestBody Map<String, String> param) throws ParserConfigurationException, SAXException, IOException {
      String result = xuniverseCommunityService.registerWirtePost(param);
      return ResponseEntity.ok(result);
    }

    @PostMapping("/uploadFile")
//	public ResponseEntity<String> uploadFile(@RequestBody Map<String, String> param) {
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
      String result = xuniverseCommunityService.uploadFile(multipartFile);
      return ResponseEntity.ok(result);
    }
}
