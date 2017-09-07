#  认证模块
### spring security 
[Spring Security：相关类以及 Authentication 认证流程（基于SpringBoot）](https://github.com/pzxwhc/MineKnowContainer/issues/58)


我们可以重写该类的几个方法来决定究竟拦截什么url，设置什么权限，等一些安全控制：

public void configure(WebSecurity web) throws Exception {...}
protected void configure(HttpSecurity http) throws Exception {...}
protected void configure(AuthenticationManagerBuilder auth) {...}
例如：

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .httpBasic()
            .and()
            .csrf().disable();
    }
}
上面的3个方法的区别就是参数的不同，这些参数WebSecurity，HttpSecurity，AuthenticationManagerBuilder 又有什么区别呢？这里有一个解释很好了：HttpSecurity, WebSecurity and AuthenticationManagerBuilder，简单来说就是：

HttpSecurity：一般用它来具体控制权限，角色，url等安全的东西。
AuthenticationManagerBuilder：用来做登录认证的。具体的注释，看org.springframework.security.config.annotation.web.configuration 包的 WebSecurityConfigurerAdapter 类的 protected void configure(AuthenticationManagerBuilder auth) throws Exception {...}方法的注释，很清楚，注释也教了怎么用这个东西。
WebSecurity：For example, if you wish to ignore certain requests.