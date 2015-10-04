package edu.asu.securebanking.beans;

import java.io.Serializable;

/**
 * @author Vikranth
 * 
 *  Application User
 *
 */
public class AppUser implements Serializable {
	
	private String username;
	
	private String password;

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

    @Override
    public String toString() {
        return "AppUser{" +
                "username='" + username + '\'' +
                '}';
    }
}
