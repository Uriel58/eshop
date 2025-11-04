package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;


@Configuration // 標記此類為 Spring 的設定類
@EnableWebMvc  // 啟用 Spring MVC 的預設設定（等同於 <mvc:annotation-driven />）
@ComponentScan(basePackages = "com.example.demo") // 掃描指定包下的 @Component、@Service、@Controller 等 Bean
public class WebMvcConfig implements WebMvcConfigurer {

    // ------------------------------
    // Thymeleaf 模板解析器
    // ------------------------------
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/"); // HTML 模板位置
        templateResolver.setSuffix(".html");           // HTML 文件後綴
        templateResolver.setTemplateMode(TemplateMode.HTML); // 模板模式為 HTML
        templateResolver.setCharacterEncoding("UTF-8"); // 編碼設定
        templateResolver.setCacheable(false);          // 是否快取模板 (開發階段關閉)
        return templateResolver;
    }

    // ------------------------------
    // Thymeleaf 模板引擎
    // ------------------------------
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver()); // 設定模板解析器
        templateEngine.setEnableSpringELCompiler(true);         // 啟用 Spring 表達式語言編譯
        return templateEngine;
    }

    // ------------------------------
    // Thymeleaf View Resolver
    // ------------------------------
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine()); // 綁定模板引擎
        viewResolver.setCharacterEncoding("UTF-8");       // 編碼設定
        return viewResolver;
    }

    // ------------------------------
    // 靜態資源設定 (CSS, JS, Images)
    // ------------------------------
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")  // URL 前綴
                .addResourceLocations("/resources/"); // 對應專案目錄
    }

    // ------------------------------
    // 國際化 (i18n) 設定
    // ------------------------------

    // 訊息資源
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages"); // 對應 messages.properties 或 messages_zh_TW.properties
        source.setDefaultEncoding("UTF-8"); // 編碼
        return source;
    }

    // 預設語系解析器
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH); // 預設語言
        resolver.setCookieName("lang");            // 存放語言資訊的 Cookie 名稱
        resolver.setCookieMaxAge(4800);            // Cookie 有效時間 (秒)
        return resolver;
    }

    // 改變語系的攔截器
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // URL 參數 ?lang=zh_TW 可以切換語系
        return interceptor;
    }

    // 註冊攔截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()); // 註冊語系切換攔截器
    }

}