package com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.mapper;

import com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.entity.ApplicationUserEntity;
import com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.entity.ApplicationUserEntityExample;
import jakarta.annotation.Generated;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplicationUserMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    long countByExample(ApplicationUserEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int deleteByExample(ApplicationUserEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int deleteByPrimaryKey(Integer id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int insert(ApplicationUserEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int insertSelective(ApplicationUserEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    List<ApplicationUserEntity> selectByExample(ApplicationUserEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    ApplicationUserEntity selectByPrimaryKey(Integer id);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int updateByExampleSelective(@Param("row") ApplicationUserEntity row, @Param("example") ApplicationUserEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int updateByExample(@Param("row") ApplicationUserEntity row, @Param("example") ApplicationUserEntityExample example);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int updateByPrimaryKeySelective(ApplicationUserEntity row);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: application_users")
    int updateByPrimaryKey(ApplicationUserEntity row);
}