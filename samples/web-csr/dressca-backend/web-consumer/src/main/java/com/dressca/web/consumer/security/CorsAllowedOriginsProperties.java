package com.dressca.web.consumer.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CORS の許可 Origin 設定を保持します。
 */
@ConfigurationProperties(prefix = "cors.allowed")
public class CorsAllowedOriginsProperties {

  private List<String> origins = new ArrayList<>();

  public List<String> getOrigins() {
    return origins;
  }

  public void setOrigins(List<String> origins) {
    this.origins = origins == null ? new ArrayList<>() : new ArrayList<>(origins);
  }
}
