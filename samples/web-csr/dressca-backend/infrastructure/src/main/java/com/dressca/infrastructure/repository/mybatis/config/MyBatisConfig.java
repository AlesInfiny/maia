package com.dressca.infrastructure.repository.mybatis.config;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

/**
 * MyBatis用の設定クラス。
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.dressca.infrastructure.repository.mybatis", sqlSessionTemplateRef = "sqlSessionTemplate")
public class MyBatisConfig {

  /**
   * MyBatis設定のカスタマイズ。
   * 
   * @return カスタマイズされたMyBatis設定
   */
  @Bean
  ConfigurationCustomizer mybatisConfigurationCustomizer() {
    return new ConfigurationCustomizer() {
      @Override
      public void customize(org.apache.ibatis.session.Configuration configuration) {
        configuration.setMapUnderscoreToCamelCase(true);
      }
    };
  }

  /**
   * データベースのトランザクション管理。
   * 
   * @param dataSource データソース
   * @return トランザクション管理オブジェクト
   */
  @Bean
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
    transactionManager.setDataSource(dataSource);

    return transactionManager;
  }

  /**
   * Mybatisのデータベースのセッションを作成（Native Image用）。
   * マッパークラスのxmlファイルを読込む。
   * 
   * @param dataSource データソース
   * @return セッションオブジェクト
   * @throws Exception 例外
   */
  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setVfs(SpringBootVFS.class);

    org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
    config.setMapUnderscoreToCamelCase(true);
    config.setAutoMappingBehavior(AutoMappingBehavior.FULL);
    VFS.addImplClass(SpringBootVFS.class);
    config.getTypeAliasRegistry().registerAliases("package.to.entities");
    sessionFactory.setConfiguration(config);

    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sessionFactory.setMapperLocations(resolver.getResources("classpath:/**/*.xml"));

    return sessionFactory.getObject();
  }
}
