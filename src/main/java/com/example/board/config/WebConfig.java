//package com.example.board.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.DispatcherServlet;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public ServletRegistrationBean<DispatcherServlet> dispatcherServlet() {
//        DispatcherServlet dispatcherServlet = new DispatcherServlet();
//        
//        // 서블릿 등록 시 비동기 지원 활성화
//        ServletRegistrationBean<DispatcherServlet> servletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet, "/");
//        servletRegistrationBean.setAsyncSupported(true); // 비동기 지원 활성화
//        
//        return servletRegistrationBean;
//    }
//}
