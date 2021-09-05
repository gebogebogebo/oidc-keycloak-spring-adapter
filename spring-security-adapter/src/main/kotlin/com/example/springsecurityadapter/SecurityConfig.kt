package com.example.springsecurityadapter

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.boot.web.servlet.FilterRegistrationBean

import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter
import org.keycloak.adapters.springsecurity.management.HttpSessionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean


// https://kazuhira-r.hatenablog.com/entry/20180710/1531235530

@KeycloakConfiguration
class SecurityConfig : KeycloakWebSecurityConfigurerAdapter() {

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()

        // roleを「ROLE_」としなかった場合
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())

        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        /*
        パブリックまたはコンフィデンシャルなアプリケーションの場合は RegisterSessionAuthenticationStrategy の、
        bearer-onlyアプリケーションの場合は NullAuthenticatedSessionStrategy のタイプの
        セッション認証ストラテジーBeanを提供する必要があります。
         */
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    fun keycloakConfigResolver() : KeycloakConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }

    override fun configure(http: HttpSecurity) {
        super.configure(http)

        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/secure/setting").hasRole("admin")
            .anyRequest().authenticated()
    }

    /*
    Beanの二重登録を避ける
    Spring Bootは、フィルターBeanをWebアプリケーション・コンテキストに登録しようとします。
    そのため、Spring Boot環境でKeycloak Spring Securityアダプターを実行する場合は、
    Keycloakフィルターが2回登録されないように、セキュリティー設定に FilterRegistrationBean を追加する必要があります。
     */
    @Bean
    fun keycloakAuthenticationProcessingFilterRegistrationBean(
        filter: KeycloakAuthenticationProcessingFilter
    ): FilterRegistrationBean<*> {
        val registrationBean = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    fun keycloakPreAuthActionsFilterRegistrationBean(
        filter: KeycloakPreAuthActionsFilter
    ): FilterRegistrationBean<*> {
        val registrationBean = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    fun keycloakAuthenticatedActionsFilterBean(
        filter: KeycloakAuthenticatedActionsFilter
    ): FilterRegistrationBean<*> {
        val registrationBean = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    fun keycloakSecurityContextRequestFilterBean(
        filter: KeycloakSecurityContextRequestFilter
    ): FilterRegistrationBean<*> {
        val registrationBean = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    /*
    Spring Boot 2.1はデフォルトで spring.main.allow-bean-definition-overriding を無効にしています。
    これは、もし KeycloakWebSecurityConfigurerAdapter を拡張した Configuration クラスが
    @ComponentScan によってすでに検出されているBeanを登録しようとすると、
    BeanDefinitionOverrideException が発生することを意味します。
    これについては以下の HttpSessionManager のように、
    @ConditionalOnMissingBean アノテーションを使用して登録を上書きすることで避けることができます。
     */
    @Bean
    @ConditionalOnMissingBean(HttpSessionManager::class)
    override fun httpSessionManager(): HttpSessionManager {
        return HttpSessionManager()
    }
}