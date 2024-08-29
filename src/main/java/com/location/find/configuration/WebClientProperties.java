package com.location.find.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class WebClientProperties {

  private Integer baseHttpClientTotalMax;
  private Integer baseHttpClientTotalPerRoute;
  private Integer connectTimeout;
  private Integer readTimeout;
  private Integer writeTimeout;
  private String host;
  private String proxyHost;
  private int proxyPort;
  private String proxyUsername;
  private String proxyPassword;
  private int proxyUse;
  private String logLevel;
  private String selfUrl;

}
