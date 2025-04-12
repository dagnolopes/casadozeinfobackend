package br.com.casadozeinfo.casadozeinfo_base.config;

import br.com.casadozeinfo.casadozeinfo_base.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> loggingFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter); // Usa a instância gerenciada pelo Spring
        registrationBean.addUrlPatterns("/api/private/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
