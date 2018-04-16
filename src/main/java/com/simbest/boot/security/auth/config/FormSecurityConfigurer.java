/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.config;

import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.security.auth.filter.CaptchaAuthenticationFilter;
import com.simbest.boot.security.auth.filter.SsoAuthenticationFilter;
import com.simbest.boot.security.auth.handle.FailedLoginHandler;
import com.simbest.boot.security.auth.handle.SsoSuccessLoginHandler;
import com.simbest.boot.security.auth.handle.SuccessLoginHandler;
import com.simbest.boot.security.auth.handle.SuccessLogoutHandler;
import com.simbest.boot.security.auth.provider.SsoAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 用途：通用Web请求安全配置
 * 作者: lishuyi
 * 时间: 2018/1/20  11:24
 */
@EnableWebSecurity
@Order(10)
public class FormSecurityConfigurer extends AbstractSecurityConfigurer {

    @Autowired
    private SsoAuthenticationService ssoAuthenticationService;

    @Autowired
    private SuccessLoginHandler successLoginHandler;

    @Autowired
    private FailedLoginHandler failedLoginHandler;

    @Autowired
    private SuccessLogoutHandler successLogoutHandler;

    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * 配置匹配路径
     *
     * @param web WebSecurity
     * @throws Exception 异常
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/fonts/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("/h2-console/**");
        web.ignoring().antMatchers("/captcha/**");
        //allow Swagger URL to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs", //swagger api json
                "/swagger-resources/configuration/ui", //用来获取支持的动作
                "/swagger-resources", //用来获取api-docs的URI
                "/swagger-resources/configuration/security", //安全选项
                "/swagger-ui.html");
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/webjars/**",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
        );
    }

    /**
     * 表单安全验证器
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(ssoAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(captchaUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/error", "/login", "/logout").permitAll()  // 都可以访问
                .antMatchers("/h2-console/**").permitAll()  // 都可以访问
                .antMatchers("/sys/user/**").hasRole("USER")   // 需要相应的角色才能访问
                .antMatchers("/admins/**").hasAnyRole("ADMIN", "SUPERVISOR")   // 需要相应的角色才能访问
                .anyRequest().authenticated()
//                .anyRequest().permitAll()
                .and().formLogin().successHandler(successLoginHandler) // 成功登入后，重定向到首页
                .loginPage(ApplicationConstants.LOGIN_PAGE).failureUrl(ApplicationConstants.LOGIN_ERROR_PAGE) // 自定义登录界面
                .failureHandler(failedLoginHandler) //记录登录错误日志，并自定义登录错误提示信息
                .and().logout().logoutSuccessHandler(successLogoutHandler) // 成功登出后，重定向到登陆页
                .and().exceptionHandling().accessDeniedPage("/403")// 处理异常，拒绝访问就重定向到 403 页面
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable()
                .sessionManagement().invalidSessionUrl(ApplicationConstants.LOGIN_PAGE).maximumSessions(1)
                .sessionRegistry(sessionRegistry).expiredUrl(ApplicationConstants.LOGIN_PAGE);
    }

    @Bean
    public SsoAuthenticationFilter ssoAuthenticationFilter() throws Exception {
        SsoAuthenticationFilter ssoFilter = new SsoAuthenticationFilter(new AntPathRequestMatcher("/sso/**"));
        ssoFilter.setAuthenticationManager(authenticationManagerBean());
        ssoFilter.setSsoAuthenticationService(ssoAuthenticationService);
        // 不跳回首页
        ssoFilter.setAuthenticationSuccessHandler(new SsoSuccessLoginHandler());
        ssoFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login"));
        return ssoFilter;
    }

    @Bean
    public CaptchaAuthenticationFilter captchaUsernamePasswordAuthenticationFilter() throws Exception {
        CaptchaAuthenticationFilter usernamePasswordAuthenticationFilter =
                new CaptchaAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return usernamePasswordAuthenticationFilter;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SessionRegistry getSessionRegistry() {
        return new SessionRegistryImpl();
    }

}
