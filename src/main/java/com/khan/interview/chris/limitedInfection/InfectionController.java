package com.khan.interview.chris.limitedInfection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.khan.interview.chris.limitedInfection.model.KhanSiteFeature;
import com.khan.interview.chris.limitedInfection.model.User;

/**
 * The InfectionController class serves as the manager for the initial infection
 * and spread of an "infection", or feature. It allows for deployment of new
 * feature while maintaining consistency between coaches and students.
 * 
 * <p>
 * If A coaches B and we give A a new feature, B should also get the feature. If
 * B also coaches C then C should also get the feature. Because the infection is
 * transferred by both the "coaches" and "is coached by" relationship, if
 * instead we want to give C the feature, both B and A should still also get the
 * feature. The infection controller offers two different approaches to
 * infection.
 * </p>
 * <ul>
 * <li>Total Infection</li>
 * <li>Limited Infection</li>
 * </ul>
 * <p>
 * Total infection simply deploys the infection through the graph based on a
 * given list of initial target users. The problem with this, however, is that
 * the infection can spread without being able to control the ultimate number of
 * users that are infected. Limited infection then looks to infect close to a
 * certain number of users. Because consistency throughout classrooms (coach to
 * students) is the most important concern, if there's no way to provide an
 * infection starting from <strong> one </strong> (in current implementation) of
 * the users provided, no infection occurs.
 * </p>
 * 
 * @author Christopher Chen
 */
public class InfectionController {

	/**
	 * Constructor for the InfectionController that provides a graph in the form
	 * of a list of users. The look up userID to user map is also created at the
	 * same time.
	 */
	public InfectionController() {
		// empty constructor
	}

	/**
	 * Initiates total "infection" of a feature starting from users based on the
	 * list of users provided. The feature is deployed to all users connected in
	 * some manner (directly or through other nodes) from the provided initial
	 * starting points.
	 * 
	 * @param users
	 *            The list of users to serve as the initial infection point(s)
	 * @param feature
	 *            The KhanSiteFeature to act as the "virus" and be deployed
	 */
	public List<User> total_infection(List<User> users, KhanSiteFeature feature) {
		List<User> toInfect = new ArrayList<User>();
		for (User user : users) {
			List<User> connectedToUser = getConnectedUsers(user);
			for (User u : connectedToUser) {
				if (!toInfect.contains(u))
					toInfect.add(u);
			}
		}
		deployFeatureToUsers(feature, toInfect);
		return toInfect;
	}

	/**
	 * Initiate limited infection by infecting a path starting with the one User
	 * whose total_infection will be closest, but under, the limit. If none
	 * exist, then throws an error that "there are no infections that maintain
	 * consistency and are under the limit". This is in order to protect the
	 * consistency between coaches and students. Preventing inconsistencies
	 * within a classroom is more valuable than satisfying a quota.
	 * <p>
	 * Another interesting approach would be to follow same algorithms used to
	 * compute the sizes of total infections as done here, but to then create a
	 * subset sum-esque problem (dynamic programming!) where the goal is to find
	 * the subset of users out of the given users list to find the closest
	 * disjoint total infections to get closer to the limit in more cases.
	 * </p>
	 * 
	 * @param users
	 * @param limit
	 * @throws IllegalArgumentException
	 *             If there is no possible infection from a user that will be
	 *             under the provided limit, this will throw an appropriate
	 *             error.
	 */
	public List<User> limited_infection(List<User> users, int limit, KhanSiteFeature feature) {
		// this will deploy to that branch
		List<User> toInfect = new ArrayList<User>();
		toInfect = limitedInfectionRoute(users, limit);
		if (toInfect.isEmpty()) {
			throw new IllegalArgumentException(
					"There are no infections that maintain consistency and are under/equal to the limit");
		}
		deployFeatureToUsers(feature, toInfect);
		return toInfect;
	}

	/**
	 * Finds the entire list of Users (based on total infection) to infect
	 * closest to the limit starting from one of the users provided.
	 * 
	 * @param users
	 *            list of users to check for possible total infections from
	 * @param limit
	 *            cap to number of users to infect
	 * @return The list of users to ultimately deploy the feature to for
	 *         limited_infection
	 */
	private List<User> limitedInfectionRoute(List<User> users, int limit) {
		// creates map of all possible total infections
		Map<User, List<User>> possibleInfections = new HashMap<User, List<User>>();
		for (User u : users) {
			List<User> infectionRoute = getConnectedUsers(u);
			possibleInfections.put(u, infectionRoute);
		}
		// finds path with length closest to limit
		List<User> closestPath = new ArrayList<User>();
		for (User u : possibleInfections.keySet()) {
			List<User> infectionPath = possibleInfections.get(u);
			int infectionSize = infectionPath.size();
			if (infectionSize <= limit && infectionSize > closestPath.size())
				closestPath = infectionPath;
		}
		return closestPath;

	}

	// make private
	private List<User> getConnectedUsers(User user) {
		List<User> connectedUsers = new LinkedList<User>();
		Set<User> visited = new HashSet<User>();
		User rootNode = user;
		Queue<User> toVisit = new LinkedList<User>();
		toVisit.add(rootNode);
		connectedUsers.add(rootNode);
		while (!toVisit.isEmpty()) {
			User currentNode = toVisit.remove();
			visited.add(currentNode);
			List<User> childNodes = getUnvisitedAdjacent(visited, currentNode);
			for (User u : childNodes) {
				visited.add(u);
				connectedUsers.add(u);
				toVisit.add(u);
			}
		}

		return connectedUsers;

	}

	/**
	 * Finds all unvisited adjacent/related users from a given user and a set of
	 * already visited users.
	 * 
	 * @param visited
	 * @param user
	 */
	private List<User> getUnvisitedAdjacent(Set<User> visited, User user) {
		List<User> unvisitedAdjacentUsers = new LinkedList<User>();
		for (User u : user.getRelatedUsers()) {
			if (!visited.contains(u))
				unvisitedAdjacentUsers.add(u);
		}
		return unvisitedAdjacentUsers;
	}

	private void deployFeatureToUsers(KhanSiteFeature feature, List<User> users) {
		for (User u : users) {
			u.setCurrentFeature(feature);
		}
	}
}
