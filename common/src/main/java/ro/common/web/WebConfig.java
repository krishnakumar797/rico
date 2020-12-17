/* Licensed under Apache-2.0 */
package ro.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Default configuration class for Web
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn({"log"})
public class WebConfig implements WebMvcConfigurer {

  @Autowired protected ApplicationContext applicationContext;

  /**
   * Message source
   *
   * @return
   */
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    return messageSource;
  }

  /**
   * Thymeleaf Template resolver
   *
   * @return
   */
  @Bean
  public SpringResourceTemplateResolver thymeleafTemplateResolver() {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("/WEB-INF/views/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);
    templateResolver.setOrder(0);
    return templateResolver;
  }

  /**
   * SpringTemplateEngine bean
   *
   * @param thymeleafTemplateResolver
   * @return
   */
  @Bean
  public SpringTemplateEngine templateEngine(
      SpringResourceTemplateResolver thymeleafTemplateResolver,
      ResourceBundleMessageSource messageSource) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(thymeleafTemplateResolver);
    templateEngine.setTemplateEngineMessageSource(messageSource);
    return templateEngine;
  }

  /**
   * Thymeleaf view resolver
   *
   * @return
   */
  @Bean
  public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
    final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setViewNames(new String[] {"thyme/*"});
    viewResolver.setExcludedViewNames(new String[] {"jsp/*"});
    viewResolver.setTemplateEngine(templateEngine);
    viewResolver.setCharacterEncoding("UTF-8");
    return viewResolver;
  }

  /**
   * Jsp view resolver
   *
   * @return
   */
  @Bean
  public InternalResourceViewResolver jspViewResolver() {
    final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setViewClass(JstlView.class);
    viewResolver.setPrefix("/WEB-INF/views/");
    viewResolver.setSuffix(".jsp");
    viewResolver.setViewNames("jsp/*");
    return viewResolver;
  }

  @Bean
  public SpringResourceTemplateResolver jspTemplateResolver() {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("/WEB-INF/views/");
    templateResolver.setSuffix(".jsp");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);
    templateResolver.setOrder(1);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  /**
   * For serving external files, change the file path as required. linux file path eg
   * "file:/opt/files/"
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/files/**").addResourceLocations("file:///C:/opt/files/");
  }
}
