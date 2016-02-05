package com.khan.interview.chris.limitedInfection.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User is the class that represents a typical Khan Academy user. This class
 * contains:
 * <ul>
 * <li>the user's ID</li>
 * <li>username</li>
 * <li>coaching and student relationships</li>
 * <li>current KhanSiteFeature deployed</li>
 * </ul>
 * <p>
 * A coaching and student relationship aren't separate as in the scheme of
 * creating/deploying an infection of a feature as ascribed, features are
 * transmitted in both directions.
 * </p>
 * 
 * @author Christopher Chen
 */
public class User {

	private String UID;
	private String username;
	// list of directly adjacent users (both students and coaches)
	private List<User> relatedUsers = new ArrayList<User>();
	private KhanSiteFeature currentFeature;

	/**
	 * default empty constructor
	 */
	public User() {
		// empty
	}

	/**
	 * Constructor for user that sets the UID, username, relatedUsers, and
	 * currentFeature
	 * 
	 * @param id
	 *            : new user's ID (UUID)
	 * @param name
	 *            : new user's username
	 * @param adjUsers
	 *            : new user's directly related users (coaches + students)
	 * @param feature
	 *            : new user's set feature
	 */
	public User(String id, String name, List<User> adjUsers, KhanSiteFeature feature) {
		this.UID = id;
		this.username = name;
		this.relatedUsers = adjUsers;
		this.currentFeature = feature;
	}

	/**
	 * @return user ID
	 */
	public String getUID() {
		return UID;
	}

	/**
	 * @param newID
	 *            : new userID to set as uID
	 */
	public void setUID(String newID) {
		this.UID = newID;
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param newUsername
	 *            : new Username to set as username
	 */
	public void setUsername(String newUsername) {
		this.username = newUsername;
	}

	/**
	 * @return relatedUsers
	 */
	public List<User> getRelatedUsers() {
		return relatedUsers;
	}

	/**
	 * If the relatedUsers list doesn't already contain aUser, add aUser to
	 * relatedUsers, and add in the other direction as well. This should be used
	 * rather than a setRelatedUsers
	 * 
	 * @param aUser
	 *            the new user to add to relatedUsers
	 */
	public void addRelatedUser(User aUser) {
		if (!relatedUsers.contains(aUser)) {
			relatedUsers.add(aUser);
			// other side of relation
			aUser.addRelatedUser(this);
		}
	}

	/**
	 * @return currentFeature
	 */
	public KhanSiteFeature getCurrentFeature() {
		return currentFeature;
	}

	/**
	 * @param newFeature
	 *            : new feature to deploy/set
	 */
	public void setCurrentFeature(KhanSiteFeature newFeature) {
		currentFeature = newFeature;
	}

	@Override
	public String toString() {
		return "User [UID=" + UID + ", username=" + username + ", number of adjacent users=" + relatedUsers.size()
				+ ", currentFeature=" + currentFeature + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(currentFeature, UID, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (UID == null) {
			if (other.UID != null)
				return false;
		} else if (!UID.equals(other.UID))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
