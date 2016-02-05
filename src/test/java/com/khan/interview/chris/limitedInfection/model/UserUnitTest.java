package com.khan.interview.chris.limitedInfection.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UserUnitTest {
	@Test
	public void test() {
		List<User> users = createListOfUsers(6);
		// print the list

		assertEquals("id-0", users.get(0).getUID());
		assertEquals(3, users.get(0).getRelatedUsers().size()); // node 0 should
																// have
		// 3 students

	}

	private List<User> createListOfUsers(int number) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < number; i++) {
			String uid = "id-" + i;
			String uname = "test-user-" + i;

			User aUser = new User();
			aUser.setUID(uid);
			aUser.setUsername(uname);
			users.add(aUser);
		}

		// let's make even number i node as a teacher of odd nodes
		// 0->1,3,5 2->1,3,5, 4->1,3,5
		for (int i = 0; i < number; i++) {
			if (i == 0 || i % 2 == 0) {
				for (int j = 0; j < number; j++) {
					if (j % 2 > 0) {
						users.get(i).addRelatedUser(users.get(j));
					}
				}
			}
		}
		return users;
	}
}
