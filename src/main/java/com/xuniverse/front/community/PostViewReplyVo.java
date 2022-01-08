package com.xuniverse.front.community;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class PostViewReplyVo {
	private String replyId            ;
	private String postId             ;
	private String postSequence       ;
	private String bundleId           ;
	private String upperReplyId       ;
	private String upperWriterId      ;
	private String upperWriterNickname;
	private int replySequence         ;
	private String content            ;
	private String originalImage      ;
	private String resizedImage       ;
	private String writerId           ;
	private String nickname			  ;
	private String writeDate          ;
	private String modityDate         ;
	private String deleteFlag         ;
}
