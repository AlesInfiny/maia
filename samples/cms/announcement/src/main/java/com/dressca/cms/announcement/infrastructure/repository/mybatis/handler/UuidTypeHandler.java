package com.dressca.cms.announcement.infrastructure.repository.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * MyBatis の UUID 型を解決するハンドラークラスです。
 */
public class UuidTypeHandler extends BaseTypeHandler<UUID> {

  @Override
  public void setNonNullParameter(PreparedStatement preparedStatement, int i, UUID parameter, JdbcType jdbcType)
      throws SQLException {
    preparedStatement.setString(i, parameter.toString());
  }

  @Override
  public UUID getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
    String value = resultSet.getString(columnName);
    return value != null ? UUID.fromString(value) : null;
  }

  @Override
  public UUID getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
    String value = resultSet.getString(columnIndex);
    return value != null ? UUID.fromString(value) : null;
  }

  @Override
  public UUID getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
    String value = callableStatement.getString(columnIndex);
    return value != null ? UUID.fromString(value) : null;
  }
}
