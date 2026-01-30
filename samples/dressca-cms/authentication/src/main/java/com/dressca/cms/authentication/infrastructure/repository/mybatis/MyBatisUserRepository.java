package com.dressca.cms.authentication.infrastructure.repository.mybatis;

import com.dressca.cms.authentication.applicationcore.repository.UserRepository;
import com.dressca.cms.authentication.infrastructure.UserDetailsImpl;
import com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.entity.ApplicationUserEntity;
import com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.entity.ApplicationUserEntityExample;
import com.dressca.cms.authentication.infrastructure.repository.mybatis.generated.mapper.ApplicationUserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

/**
 * {@link UserRepository} を継承するクラス。 ユーザー情報を取得します。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisUserRepository implements UserRepository {

  private final ApplicationUserMapper applicationUserMapper;

  @Override
  public UserDetails findByEmail(String email) {
    ApplicationUserEntityExample example = new ApplicationUserEntityExample();
    example.createCriteria().andEmailEqualTo(email);

    List<ApplicationUserEntity> entities = applicationUserMapper.selectByExample(example);

    if (entities == null || entities.isEmpty()) {
      return null;
    }

    ApplicationUserEntity entity = entities.get(0);
    return new UserDetailsImpl(entity.getId(), entity.getName(), entity.getEmail(),
        entity.getPassword());
  }
}
