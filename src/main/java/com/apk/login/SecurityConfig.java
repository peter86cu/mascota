package com.apk.login;

import java.nio.charset.Charset;

import javax.sql.DataSource;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.apk.login.service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	 @Autowired
	    private UserService userService;
	 
	 @Autowired
	  private DataSource dataSource;

	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        //auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	    	String pw1=new BCryptPasswordEncoder().encode("admin");
			System.out.println("INICIO "+pw1);

			auth.jdbcAuthentication().dataSource(dataSource)
	        	.usersByUsernameQuery("select username, password, state"
	            	+ " from user where  username=?")
	        	.authoritiesByUsernameQuery("select u.name,rol.descripcion from user u JOIN user_roles ur ON (ur.userid=u.userid)  join roles rol  on(rol.id=ur.rolid) where u.username=?");
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	            .authorizeRequests()
	            .antMatchers("/ws").permitAll()
	            .antMatchers("/login").permitAll()
	            .antMatchers("/check-token").permitAll()
	            .antMatchers("/peso-mascota").permitAll()
	            .antMatchers("/events/*").permitAll()
	            .antMatchers("/event-update").permitAll()
	            .antMatchers("/add-mascota").permitAll()
	            .antMatchers("/create-user").permitAll()
	            .antMatchers("/validar-google/*").permitAll()
	            .antMatchers("/login-google").permitAll()
	            .antMatchers("/confirmar-usuario").permitAll()
	            .antMatchers("/validar-username-phone").permitAll()
	            .antMatchers("/resetear-password").permitAll()
	            .antMatchers("/confirmar-reseteo-password").permitAll()
	            .antMatchers("/formulario-reseteo-password").permitAll()
	            .antMatchers("/listado-tipo-raza/listado-tipo-raza").permitAll()
	            .antMatchers("/add-peso-mascota").permitAll()
	            .antMatchers("/api/parameters/listado-vacunas").permitAll()
	            .antMatchers("/api/parameters/*").permitAll()
	            .antMatchers("/add-vacuna-mascota").permitAll()
	            .antMatchers("/activity-estilista").permitAll()
	            .antMatchers("/api/parameters/workSchedule").permitAll()
	            .antMatchers("/api/parameters/calendario-word-user").permitAll()
	            .antMatchers("/api/pet/*").permitAll()
	            .antMatchers("/api/pet/albums/*/photos/*").permitAll()
	            .antMatchers("/api/pet/albums/*/isLiked").permitAll()
	            .antMatchers("/api/pet/albums/*").permitAll()
	            .antMatchers("/api/pet/albums/photos").permitAll()
	            .antMatchers("/activity-mascota").permitAll()
	            .antMatchers("/add-evento").permitAll()
	            .antMatchers("/add-businesses").permitAll()
	            .antMatchers("/api/businesses/list-businesses").permitAll()
	            .antMatchers("/api/businesses/*").permitAll()
	            .antMatchers("/api/businesses/list-activity-businesses").permitAll()
	            .antMatchers("/api/businesses/add-activity-businesses").permitAll()
	            .antMatchers("/api/comments/business/*").permitAll()
	            .antMatchers("/api/comments/comment/*").permitAll()
	            .antMatchers("/api/responses/comment/*").permitAll()
	            .antMatchers("/api/parameters/activities").permitAll()
	            .anyRequest().authenticated();
	    }
	    
	    HttpHeaders createHeaders(String username, String password){
			   return new HttpHeaders() {{
			         String auth = username + ":" + password;
			         byte[] encodedAuth = Base64.encodeBase64( 
			            auth.getBytes(Charset.forName("US-ASCII")) );
			         String authHeader = "Basic " + new String( encodedAuth );
			         set( "Authorization", authHeader );
			      }};
			}

	    @Override
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

	    @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
}
