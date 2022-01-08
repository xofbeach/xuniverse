package com.xuniverse.batch;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.xuniverse.commons.GeocodeXyVo;
import com.xuniverse.general.AptRealTransactionDetailVo;

@Repository
@Mapper
public interface XuniverseBatchMapper {
	List<LegalDongCodeVo> selectLegalDongCodeList();
	void insertAptRealTransactionDetail(AptRealTransactionDetailVo vo);
	void insertAptRealTransactionDetail_forEach(List<AptRealTransactionDetailVo> aptRealTransactionDetailVoList);

	List<GeocodeXyVo> getSearchGeocodeTarget();
	void insertGeocodeXY(GeocodeXyVo geocodeXyVo);

}
