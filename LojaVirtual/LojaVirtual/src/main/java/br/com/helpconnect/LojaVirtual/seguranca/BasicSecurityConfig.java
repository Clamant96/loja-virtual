package br.com.helpconnect.LojaVirtual.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService);
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/**").permitAll()
		.antMatchers("/clientes/logar").permitAll()
		.antMatchers("/clientes/cadastrar").permitAll()
		.antMatchers(HttpMethod.GET, "/clientes").permitAll()
		.antMatchers(HttpMethod.GET, "/categorias").permitAll()
		.antMatchers(HttpMethod.GET, "/pedidos").permitAll()
		.antMatchers(HttpMethod.GET, "/produtos").permitAll()
		.antMatchers(HttpMethod.GET, "/listadesejo").permitAll()
		.antMatchers(HttpMethod.GET, "/correios").permitAll()
		.antMatchers(HttpMethod.GET, "/compras").permitAll()
		
		/*.antMatchers("/clientes/{id}").permitAll()
		.antMatchers("/categorias").permitAll()
		.antMatchers("/categorias/{id}").permitAll()
		.antMatchers("/pedidos").permitAll()
		.antMatchers("/pedidos/{id}").permitAll()
		.antMatchers("/produtos").permitAll()
		.antMatchers("/produtos/{id}").permitAll()
		.antMatchers("/listadesejo").permitAll()
		.antMatchers("/listadesejo/{id}").permitAll()
		.antMatchers("/listadesejo/listaDeDesejo/{id}").permitAll()
		.antMatchers("/listadesejo/listaDeDesejo/{idListaDeDesejo}").permitAll()
		.antMatchers("/listadesejo/produto_lista/produtos/{idProduto}/listaDesejos/{idListaDeDesejo}").permitAll()
		.antMatchers("/produtos/produto_lista").permitAll()
		.antMatchers("/produtos/produto_lista/{id}").permitAll()
		.antMatchers("/produtos/produto_lista/produtos/{idProduto}/listaDesejos/{idListaDeDesejo}").permitAll()
		.antMatchers("/produtos/meuspedidos/{idPedido}").permitAll()
		.antMatchers("/produtos/produto_pedido/produtos/{idProduto}/pedidos/{idPedido}").permitAll()*/
		.anyRequest().authenticated()
		.and().httpBasic()
		.and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().cors()
		.and().csrf().disable();
		
	}
}
