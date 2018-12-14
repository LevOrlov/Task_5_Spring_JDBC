package control;


import dao.UserDao;

import dao.daoImpl.UserDaoJDBCImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;

//показывает что в этмо классе будут бины
@Configuration
//включает веб версию, структурная.
@EnableWebMvc
//мы показываем где искать аннотированные класс
@ComponentScan("control")
// TODO без Environment PropertySource не работает ПОЧЕМУ?????
@PropertySource("classpath:main/resources/config.properties")
public class MvcConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    Environment env;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/WEB-INF/views/**").addResourceLocations("/views/");
    }

    //показывает что это метода возвращает конкретный объект, и показывает спрингу
// что это его компнент.Метод, аннотированный этой аннотацией, работает как идентификатор компонента,
    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driver"));
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }

    @Bean
    public UserDao getContactDAO() {
        return new UserDaoJDBCImpl();
    }

    @Bean
    public JdbcTemplate getJDBCTemplateDAO() {
        return new JdbcTemplate(getDataSource());
    }

}
