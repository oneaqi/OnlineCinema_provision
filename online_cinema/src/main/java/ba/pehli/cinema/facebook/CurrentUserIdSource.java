package ba.pehli.cinema.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.UserIdSource;
import org.springframework.stereotype.Component;

import ba.pehli.cinema.service.UserDao;

/**
 * Class for generating key which will determine Facebook connection; every user will have
 * separate connection so we put here his username
 * @author almir
 *
 */
@Component
public class CurrentUserIdSource implements UserIdSource{
	@Autowired
	private UserDao userDao;
	
	private String userId = "anonymous"; 
	@Override
	public String getUserId() {
		return userDao.getAuthenticatedUser().getUsername();
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}
