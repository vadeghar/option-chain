package org.trade.option.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;





@Configuration
public class ThymeleafConfig {

//	@Bean
//	public SpringSecurityDialect securityDialect() {
//		return new SpringSecurityDialect();
//	}

	@Bean
	public AppThymeleafDialect myDialect() {
		return new AppThymeleafDialect();
	}
	
	@Bean
	public ViewResolver viewResolver() {
		final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setProducePartialOutputWhileProcessing(false);
		resolver.setTemplateEngine(getTemplateEngine());
		resolver.setCache(false);
		resolver.setOrder(1);
		resolver.setExcludedViewNames(new String[]{"documentView"});
		return resolver;
	}
	
	
	
	@Bean
	@Primary
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false);
		resolver.setOrder(2);
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
	@Bean
	@Qualifier("templateEngine")
	public SpringTemplateEngine getTemplateEngine(){
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(new LayoutDialect());
		engine.addDialect(myDialect());
//		engine.addDialect(securityDialect());
		engine.setEnableSpringELCompiler(true);
		return engine;
	}
}