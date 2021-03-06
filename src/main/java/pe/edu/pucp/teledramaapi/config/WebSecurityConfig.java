package pe.edu.pucp.teledramaapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/sala/**").hasAuthority("administrador")
                .antMatchers("/teatro/**").hasAuthority("administrador")
                .antMatchers("/funcion/**").hasAuthority("administrador")
                .antMatchers("/dashboard/**").hasAuthority("administrador")
                .antMatchers("/card/**").hasAuthority("administrador")
                .antMatchers("/calificacion/**").hasAuthority("administrador")
                .anyRequest().permitAll();
        http.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select * from (select correo, contrasena, enabled from cliente \n" +
                        "union all\n" +
                        "select correo, contrasena, enabled from empleado) usersCredentials\n" +
                        "where correo = ?")
                .authoritiesByUsernameQuery("select * from (select correo, 'cliente' as rol from cliente\n" +
                        "union all\n" +
                        "select correo, rol from empleado) usersAuth\n" +
                        "where correo = ?");
    }

}
