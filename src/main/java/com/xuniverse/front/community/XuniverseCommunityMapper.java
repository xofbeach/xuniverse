package com.xuniverse.front.community;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface XuniverseCommunityMapper {

	List<CommunityBoardVo> getPostList(Map<String, String> param, Pagination pagination);
	int getPostCount(Map<String, String> param);

	CommunityBoardVo getPostById(Map<String, String> param);
	List<PostViewReplyVo> getPostReplyById(Map<String, String> param);
	int deletePostById(Map<String, String> param);
	int registerReply(Map<String, String> param);
	int registerReReply(Map<String, String> param);
	int modifyReReply(Map<String, String> param);
	int deleteReReply(Map<String, String> param);
	int registerWirtePost(Map<String, String> param);
	int modifyPostById(Map<String, String> param);
}
