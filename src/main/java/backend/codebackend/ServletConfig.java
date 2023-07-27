//package backend.codebackend;
//
//import org.springframework.boot.web.servlet.ServletComponentScan;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@ServletComponentScan
//@Configuration
//public class ServletConfig {
//    @Bean
//    public ServletRegistrationBean<SomeServlet> getServletRegistrationBean() {
//
//        ServletRegistrationBean<SomeServlet> registrationBean = new ServletRegistrationBean<>(new SomeServlet());
//        registrationBean.addUrlMappings("/SomeServlet/*");
//        registrationBean.addInitParameter("isAbsolutePath", "true");
//        registrationBean.addInitParameter("propertyPath", "src/main/resources/public/rd/");
//
//        return registrationBean;
//    }
//}
