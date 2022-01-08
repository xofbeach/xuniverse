package com.xuniverse.front.user;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class XuniverseUserController {

	@Autowired
	private XuniverseUserService xuniverseUserService;

    /**
     * 유저 정보를 확인한다.
     * @param param
     * @return
     */
    @PostMapping("/getUserId")
	public ResponseEntity<String> getUserId(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response) {
    	HttpSession sessioin = request.getSession(true);
    	String sessionId = sessioin.getId();
//    	sessioin.setAttribute("session", sessioin);
//    	sessioin.setAttribute("sessionId", sessionId);


    	// 쿠키는 이름과 값을 받아 생성하고, response에 담아서 클라이언트에게 전송
//    	Cookie cookie = new Cookie("sesionId", sessionId);
//    	response.addCookie(cookie);


    	param.put("sessionId", sessionId);
		String result = xuniverseUserService.getUserId(param);
		return ResponseEntity.ok(result);
    }
}
