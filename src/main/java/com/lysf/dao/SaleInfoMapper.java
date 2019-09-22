package com.lysf.dao;

import com.lysf.Vo.SaleInfoVo;
import com.lysf.entity.SaleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaleInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SaleInfo record);

    int insertSelective(SaleInfo record);

    SaleInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SaleInfo record);

    int updateByPrimaryKey(SaleInfo record);

    int selectByName(String productName);

    List<SaleInfoVo> selectByNameAndDate(@Param("productName") String productName, @Param("datebefore")String datebefore,@Param("dateNow") String dateNow);

    List<SaleInfoVo> selectAllByDate(@Param("datebefore")String datebefore,@Param("dateNow") String dateNow);
}