package com.xuniverse.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AptRealTransactionDetailVo {

	// tb_apt_real_transaction_detail
    private String transactionId             ;
    private String transactionAmount         ;
    private String constructionYeae          ;
    private String dealYear                  ;
    private String roadName                  ;
    private String roadNameBuildingMainNumber;
    private String roadNameBuildingSubNumber ;
    private String roadNameDivisionCode      ;
    private String roadNameSerialNumberCode  ;
    private String roadNameGroundBasementCode;
    private String roadNameCode              ;
    private String legalDong                 ;
    private String legalDongMainCode         ;
    private String legalDongSubCode          ;
    private String legalDongDivisionCode1    ;
    private String legalDongDivisionCode2    ;
    private String legalDongLotNumberCode    ;
    private String apartmentName             ;
    private String dealMonth                 ;
    private String dealDay                   ;
    private String exclusiveArea             ;
    private String lotNumber                 ;
    private String regionCode                ;
    private String floor                     ;

    // tb_legal_dong_code
    private String legalCity                 ;
    private String legalDistrict             ;

}
