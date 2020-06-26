package com.mboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */

@Component
@Data
@ConfigurationProperties(prefix = "db2")
public class Db2DataSourceProperties implements Serializable {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private Integer initialSize;

    private Integer minIdle;

    private Integer maxActive;

    private String platform;
}
