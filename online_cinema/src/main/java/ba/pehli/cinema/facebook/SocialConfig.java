package ba.pehli.cinema.facebook;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

/**
 * Main Facebook configuration.
 * @author almir
 *
 */
@Configuration
@EnableSocial
@PropertySource("classpath:/facebook/facebook-app.properties")
public class SocialConfig extends SocialConfigurerAdapter{

	@Override
	public UserIdSource getUserIdSource() {
		return new CurrentUserIdSource();
	}
	
	@Configuration
	public static class FacebookConfiguration extends SocialConfigurerAdapter{

		@Override
		public void addConnectionFactories(
				ConnectionFactoryConfigurer connectionFactoryConfigurer,Environment environment) {
			connectionFactoryConfigurer.addConnectionFactory(new FacebookConnectionFactory(
					environment.getRequiredProperty("facebook.appKey"),environment.getRequiredProperty("facebook.appSecret")));
		}
		
		@Bean
		@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
		public Facebook facebookTemplate(ConnectionRepository connectionRepository) {
			Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
			return connection != null ? connection.getApi() : null;
		}
		
	}
	
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator,ConnectionRepository connectionRepository) {
		return new ConnectController(connectionFactoryLocator, connectionRepository);
	}
	
	
	
}
