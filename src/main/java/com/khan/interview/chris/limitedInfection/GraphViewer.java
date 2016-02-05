package com.khan.interview.chris.limitedInfection;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import com.khan.interview.chris.limitedInfection.model.KhanSiteFeature;
import com.khan.interview.chris.limitedInfection.model.User;

/**
 * Visualization of infection/deployment of a feature based on the
 * methods/algorithms in the InfectionController class using the
 * <a href = "http://graphstream-project.org/"> GraphStream </a> library. The
 * visualization is currently limited to do one of two things:
 * <ul>
 * <li>Visualization of a total infection starting with a single, given node
 * </li>
 * <li>Visualization of finding the best limited infection from an entire graph
 * </li>
 * </ul>
 * 
 * 
 * @author Christopher Chen
 *
 */
public class GraphViewer {
	// Run from providing commandline/console arguments
	public static void main(String args[]) {
		// Prints basic templates
		System.out.println("Limited Infection Visualize: [type_of_visualize = 0, size_of_graph, limit]");
		System.out.println("Total Infection Visualize: [type_of_visualize = 1, size_of_graph, start_node_index]");
		if (args.length != 3) {
			System.out.println(
					"commandline args in form [type_of_visualize(limited: 0, total : 1), size_of_graph, limit or start_node_index] are required");
			System.exit(1);
		}
		int limitedOrTotal = Integer.valueOf(args[0]);
		int graphSize = Integer.valueOf(args[1]);
		// generate random user graph
		List<User> users = generateRandomUserGraph(Integer.valueOf(graphSize));
		InfectionController controller = new InfectionController();

		if (limitedOrTotal != 0 && limitedOrTotal != 1) {
			System.out.println(
					"commandline args [type_of_visualize, size_of_graph, limit or start_node_index], type_of_visualize has to be 0 or 1");
			System.exit(1);
		}
		List<User> infectedUsers = new ArrayList<User>();
		if (limitedOrTotal == 1) {
			int startPoint = Integer.valueOf(args[2]);
			if (startPoint >= graphSize) {
				System.out.println(
						"commandline args [type_of_visualize,  size_of_graph, start_node_index], start_node_index has to be smaller than sizeOfGraph");
				System.exit(1);
			}
			// get the total infected users from startPointUser
			List<User> startPointList = new ArrayList<User>();
			startPointList.add(users.get(Integer.valueOf(startPoint)));
			KhanSiteFeature someFeature = new KhanSiteFeature("Cool Blue", new ArrayList<String>(), "version 1.0");
			infectedUsers = controller.total_infection(startPointList, someFeature);
			System.out.println("GraphSize: " + graphSize + " Starting node: " + startPoint);
			System.out.println("Number of infected users: " + infectedUsers.size());
		} else {
			int limit = Integer.valueOf(args[2]);
			List<User> startPointList = users;
			KhanSiteFeature someFeature = new KhanSiteFeature("Cool Blue", new ArrayList<String>(), "version 1.0");
			infectedUsers = controller.limited_infection(startPointList, limit, someFeature);
			System.out.println("GraphSize: " + graphSize + " Limit: " + limit);
			System.out.println("Size of best limited infection found: " + infectedUsers.size());
		}
		Graph graph = buildGraph(users, infectedUsers);
		graph.display();

	}

	/**
	 * Builds the according graph visualization based on the graph provided and
	 * the infected users.
	 * 
	 * @param allUsers
	 *            - provided graph in the form of a list of users
	 * @param infectedUsers
	 *            - all of the users that are infected
	 * @return
	 */
	public static Graph buildGraph(List<User> allUsers, List<User> infectedUsers) {
		Graph graphView = new SingleGraph("InfectionView");
		graphView.addAttribute("ui.stylesheet", stylesheet);
		graphView.addAttribute("ui.default.title", "InfectionGraph");
		graphView.setStrict(false);
		graphView.setAutoCreate(true);
		Node aNode = null;
		for (User u : allUsers) {
			aNode = graphView.addNode(u.getUID());
			aNode.addAttribute("ui.label", u.getUsername());
			if (infectedUsers.contains(u)) {
				aNode.addAttribute("ui.class", "infected");
			}
			// add edges to all adjacent nodes
			List<User> adjacentUsers = u.getRelatedUsers();
			for (User aU : adjacentUsers) {
				graphView.addEdge(u.getUID() + "-" + aU.getUID(), u.getUID(), aU.getUID());
			}
		}
		return graphView;
	}

	/**
	 * Generates a random graph of users using the GraphStream library and
	 * integrating the Users class
	 * 
	 * @param numberNodes
	 *            - the number of nodes desired in the graph
	 * @return
	 */
	public static List<User> generateRandomUserGraph(int numberNodes) {
		Graph graph = new SingleGraph("Random");
		Generator gen = new RandomGenerator(1);
		gen.addSink(graph);
		gen.begin();
		for (int i = 0; i < numberNodes; i++)
			gen.nextEvents();
		gen.end();

		int n = graph.getNodeCount();
		// convert node to user
		List<User> users = new ArrayList<User>(n);
		// build 2d adjacent matrix representation of the graph

		int[][] matrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			User u = makeUser(graph.getNode(i));
			users.add(u);

			for (int j = 0; j < n; j++) {
				matrix[i][j] = graph.getNode(i).hasEdgeBetween(j) ? 1 : 0;
			}
		}

		// build adjacent lists
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (matrix[i][j] == 1) {
					users.get(i).addRelatedUser(users.get(j));
				}
			}
		}
		return users;

	}

	private static User makeUser(Node n) {
		User u = new User();
		u.setUID(n.getId());
		String name = n.getAttribute("ui.label");
		if (name == null || name.isEmpty()) {
			name = "n-" + n.getId();
		}
		u.setUsername(name);
		return u;
	}

	static String stylesheet = "graph { fill-color: white;}"
			// + "node { size: 40px, 50px; shape: triangle; fill-color: black;
			// stroke-mode: plain; stroke-color: yellow;}"
			+ "node { text-alignment: under; text-offset: 0px, 4px; text-color: #444; }"
			// + "node { size-mode: fit; shape: rounded-box; fill-color: black;
			// stroke-mode: plain; padding: 3px, 2px; }"
			// + "node#A { fill-color: blue, shape: box;}"
			// + "node:clicked { fill-color: red; }";
			+ "node.infected { fill-color: red; size: 40px; }"
			+ "node { fill-color: black; size: 40px; stroke-mode: plain; stroke-color: #555; text-size: 15; } ";
}
