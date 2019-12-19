package ba.pehli.cinema.facebook;

import org.springframework.social.UserIdSource;

public class StaticUserIdSource implements UserIdSource{
	private String userId = "anonymous"; 
	@Override
	public String getUserId() {
		return "anonymous";
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}
