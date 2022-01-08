package com.xuniverse.front.community;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityBoardVo {
	private int postSequence        ;
	private String postId           ;
	private String categoryId       ;
	private String title            ;
	private String content          ;
	private String writerId         ;
	private String nickname			;
	private String writeDate        ;
	private String modityDate       ;
	private String readCount        ;
	private String likeCount        ;
	private String badCount         ;
	private String deleteFlag       ;
}
