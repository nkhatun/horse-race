package nkhatun.demo.harrykart.java.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable().authorizeRequests()
	                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                .anyRequest()
	                .authenticated().and()
	                .httpBasic();
	    }
	    @Override
	    public void configure(WebSecurity web) throws Exception {
	        web.ignoring().antMatchers("/v2/api-docs",
	                "/configuration/ui",
	                "/swagger-resources/**",
	                "/configuration/security",
	                "/swagger-ui.html", 
	                "/swagger-ui/**",
	                "/webjars/**");
	    }
	    @Bean
	    PasswordEncoder passwordEncoder() {
	        return NoOpPasswordEncoder.getInstance() ;

	    }
}
