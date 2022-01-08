package com.xuniverse.front.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class XuniverseUserService {

	@Autowired
	private XuniverseUserMapper xuniverseUserMapper;

	public String getUserId(Map<String, String> param) {
		String userId = xuniverseUserMapper.checkLoginUser(param);
		if (userId == null) {
			xuniverseUserMapper.registerUser(param);
		}
		param.put("userId", userId);
		xuniverseUserMapper.updateLogin(param);

		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("userId", userId);
		return gson.toJson(jsonObject);
	}
}
