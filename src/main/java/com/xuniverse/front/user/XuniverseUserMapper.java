package com.xuniverse.front.user;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface XuniverseUserMapper {
	String checkLoginUser(Map<String, String> param);

	void registerUser(Map<String, String> param);

	void updateLogin(Map<String, String> param);
}
