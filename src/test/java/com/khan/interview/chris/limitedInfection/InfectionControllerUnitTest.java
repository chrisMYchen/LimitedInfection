package com.khan.interview.chris.limitedInfection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.khan.interview.chris.limitedInfection.model.KhanSiteFeature;
import com.khan.interview.chris.limitedInfection.model.User;

import junit.framework.Assert;

public class InfectionControllerUnitTest {

	@Test
	public void randomInfectionTest() {
		List<User> randomGraph = generateRandomUsersGraph(30);
		List<User> startingList = new ArrayList<User>();
		List<String> pastVersions = new ArrayList<String>();
		startingList.addAll(randomGraph);
		InfectionController controller = new InfectionController();
		try {
			List<User> successInfect = controller.limited_infection(startingList, 40,
					new KhanSiteFeature("Cool Blue", pastVersions, "version 1.0"));
			assertEquals(30, successInfect.size());

		} catch (IllegalArgumentException e) {
			System.out.println(e);
			fail("shouldn't get here");
		}
	}

	@Test
	public void noLimitedInfectionTest() {
		// no possible infections under limit
		List<User> aCircleGraph = generateCircleOfUsers(5, 0);
		List<User> startingList = new ArrayList<User>();
		List<String> pastVersions = new ArrayList<String>();
		startingList.addAll(aCircleGraph);
		InfectionController controller = new InfectionController();
		try {
			controller.limited_infection(startingList, 4,
					new KhanSiteFeature("Cool Blue", pastVersions, "version 1.0"));
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			assertTrue(e != null);
		}
	}

	@Test
	public void happyLimitedInfectionTest() {
		List<User> aCircleGraph = generateCircleOfUsers(5, 0);
		List<User> startingList = new ArrayList<User>();
		List<String> pastVersions = new ArrayList<String>();
		// separate graph of size 2
		List<User> bCircleGraph = generateCircleOfUsers(2, 5);
		aCircleGraph.addAll(bCircleGraph);
		startingList.addAll(aCircleGraph);
		InfectionController controller = new InfectionController();
		try {
			List<User> successInfect = controller.limited_infection(startingList, 4,
					new KhanSiteFeature("Cool Blue", pastVersions, "version 1.0"));
			assertEquals(2, successInfect.size());
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			fail("shouldn't get here");
		}
	}

	@Test
	public void totalInfectionTest() {
		List<User> aCircleGraph = generateCircleOfUsers(6, 0);
		InfectionController controller = new InfectionController();
		List<User> startingList = new ArrayList<User>();
		startingList.add(aCircleGraph.get(0));
		List<String> pastVersions = new ArrayList<String>();
		for (User u : aCircleGraph) {
			assertNull(u.getCurrentFeature());
		}
		controller.total_infection(startingList, new KhanSiteFeature("Cool Blue", pastVersions, "version 1.0"));
		// infects all
		for (User u : aCircleGraph) {
			assertEquals(new KhanSiteFeature("Cool Blue", pastVersions, "version 1.0"), u.getCurrentFeature());
		}
	}

	@Test
	public void totalInfectionTestOddEven() {
		List<User> oddEvenGraph = generateEvenOddListOfUsers(5);
		InfectionController controller = new InfectionController();
		List<User> startingList = new ArrayList<User>();
		List<String> pastVersions = new ArrayList<String>();
		for (User u : oddEvenGraph) {
			assertNull(u.getCurrentFeature());
		}
		// add separate dangling nodes
		User danglingUser0 = new User("id0", "name0", null, null);
		User danglingUser1 = new User("id1", "name1", null, null);
		User danglingUser2 = new User("id2", "name2", null, null);
		oddEvenGraph.add(danglingUser0);
		oddEvenGraph.add(danglingUser1);
		oddEvenGraph.add(danglingUser2);
		startingList.add(oddEvenGraph.get(0));
		controller.total_infection(startingList, new KhanSiteFeature("Cool Blue", pastVersions, "version 1.0"));
		// infects all but danglingUsers
		for (User u : oddEvenGraph) {
			if (u.getUID() == "id0" || u.getUID() == "id1" || u.getUID() == "id2")
				assertNull(u.getCurrentFeature());
			else {
				assertEquals(new KhanSiteFeature("Cool Blue", pastVersions, "version 1.0"), u.getCurrentFeature());
			}
		}
	}

	@Test
	public void generateRandomUsersGraphTest() {
		List<User> generatedGraphTest = generateRandomUsersGraph(20);
		Assert.assertTrue(generatedGraphTest.size() == 20);
		System.out.println("random");
		for (User u : generatedGraphTest) {
			System.out.println(u);
		}
	}

	@Test
	public void addUserTest() {
		User aUser = new User("userID1", "username", new ArrayList<User>(), new KhanSiteFeature());
		aUser.addRelatedUser(new User("userID2", "username2", new ArrayList<User>(), new KhanSiteFeature()));
		System.out.println(aUser);
		// testSadRandomGraph();
	}
	/*
	 * public void testSadRandomGraph() { List<User> randomGraph =
	 * generateRandomUsersGraph(5); InfectionController randomController = new
	 * InfectionController(randomGraph); for (int i = 0; i < 5; i++) {
	 * List<User> connected =
	 * randomController.getConnectedUsers(randomGraph.get(i));
	 * System.out.println("Connected for random user :" + i); for (User u :
	 * connected) { System.out.println(u); } } }
	 * 
	 * @Test public void testGetConnectedUser() { List<User> aUserGraph =
	 * generateEvenOddListOfUsers(6); InfectionController controller = new
	 * InfectionController(aUserGraph); for (int i = 0; i < 6; i++) {
	 * assertEquals(6, controller.getConnectedUsers(aUserGraph.get(i)).size());
	 * System.out.println(""); }
	 * 
	 * List<User> randomGraph = generateRandomUsersGraph(5); InfectionController
	 * randomController = new InfectionController(randomGraph); for (int i = 0;
	 * i < 5; i++) { System.out.println("Node " + i + ": " +
	 * randomController.getConnectedUsers(randomGraph.get(i)).size()); } }
	 * 
	 * @Test public void testGetCircularConnectedUsers2() { // create some test
	 * data List<User> aUserGraph = generateCircleOfUsers(10, 0);
	 * InfectionController controller = new InfectionController(aUserGraph); for
	 * (int i = 0; i < 10; i++) { List<User> connected =
	 * controller.getConnectedUsers(aUserGraph.get(i)); System.out.println(
	 * "connected users for user: " + aUserGraph.get(i)); for (User u :
	 * connected) { System.out.println(u); } System.out.println(""); } }
	 */

	/**
	 * Creates a graph of number of users The users will be connected to a
	 * generated number of other users in the graph
	 * 
	 * @param count
	 *            - the number of users to create for the graph
	 * @return
	 */
	private static List<User> generateRandomUsersGraph(int count) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < count; i++) {
			String uid = "id-" + i;
			String uname = "username-" + i;
			User aUser = new User();
			aUser.setUID(uid);
			aUser.setUsername(uname);
			users.add(aUser);
		}
		for (int j = 0; j < count; j++) {
			// generates a random number from 0 - (count - 1)
			int numConnectedUsers = (int) (Math.random() * count);
			// list of users to connect one user to
			boolean[] UsersArray = new boolean[count];
			UsersArray[j] = true;
			for (int k = 0; k < numConnectedUsers; k++) {
				int randUserIDNumber = (int) (Math.random() * count);
				while (UsersArray[randUserIDNumber]) {
					randUserIDNumber = (int) (Math.random() * count);
				}
				User tempUser = users.get(randUserIDNumber);
				UsersArray[randUserIDNumber] = true;
				users.get(j).addRelatedUser(tempUser);
			}
		}
		return users;
	}

	private static List<User> generateEvenOddListOfUsers(int count) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < count; i++) {
			String uid = "id-" + i;
			String uname = "username-" + i;

			User aUser = new User();
			aUser.setUID(uid);
			aUser.setUsername(uname);
			users.add(aUser);
		}

		// let's make even number i node as a teacher of odd nodes
		// 0->1,3,5 2->1,3,5, 4->1,3,5
		for (int i = 0; i < count; i++) {
			if (i == 0 || i % 2 == 0) {
				for (int j = 0; j < count; j++) {
					if (j % 2 > 0) {
						users.get(i).addRelatedUser(users.get(j));
					}
				}
			}
		}

		return users;
	}

	private static List<User> generateCircleOfUsers(int number, int startingIndex) {
		List<User> users = new ArrayList<User>();
		for (int i = 0 + startingIndex; i < number + startingIndex; i++) {
			String uid = "id-" + i;
			String uname = "test-user-" + i;

			User aUser = new User();
			aUser.setUID(uid);
			aUser.setUsername(uname);
			users.add(aUser);
		}

		// let's make even number i node as a teacher of odd nodes
		// 0->1->2->3->4->5->6->....number->0
		for (int i = 0; i < number; i++) {
			if (i < number - 1) {
				users.get(i).addRelatedUser(users.get(i + 1));
			} else {
				users.get(i).addRelatedUser(users.get(0));
			}
		}

		return users;
	}

}
