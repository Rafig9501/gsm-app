package org.gsmapp.util;

public class Constants {

    public static final String[] ALLOWED_PATHS = {
            "/user/**",
            "/gsm-ms/user/**",
            "/swagger-ui/**",
            "/api-docs/**",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs"
    };

    public static final String[] MONITORING_PATHS = {
            "/actuator/**"
    };

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

}
