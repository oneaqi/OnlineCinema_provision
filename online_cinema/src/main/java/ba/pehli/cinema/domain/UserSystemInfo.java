package ba.pehli.cinema.domain;

/**
 * Utility class that contains informations about user environment.
 * 
 * @author almir
 *
 */
public class UserSystemInfo {
	private String osName;
	private String region;
	
	public String getOsName() {
		return osName;
	}
	public void setOsName(String os) {
		this.osName = os;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	
}
