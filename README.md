LimitedInfection

Requires Java 1.8
Maven 3.x

LimitedInfection serves as the manager for the initial infection
and spread of an "infection", or feature. It allows for deployment of new
feature while maintaining consistency between coaches and students.  

If A coaches B and we give A a new feature, B should also get the feature. If
B also coaches C then C should also get the feature. Because the infection is
transferred by both the "coaches" and "is coached by" relationship, if
instead we want to give C the feature, both B and A should still also get the
feature. The infection controller offers two different approaches to
infection.
 
Assumptions:
For the limitedInfection case, I prioritized maintaining consistency within
a classroom. All coaches and students MUST have the same feature, or none of
them should have the new feature if there is no path satisfying the limit condition.

I didn't combine multiple disjointed paths to get the best infection (closest to
the given limit). My limitedInfection implementation finds the one connected path
with size closest to the given limit. 

To improve in the future, I could implement a subset-sum algorithm to find the
best combination of starting points to find the true closest infection(s) to the
limit.

How to build/run project:
Go to directory limitedInfection and run mvn clean install

How to run visualization:
java -jar target/limitedInfection-0.0.1-SNAPSHOT-jar-with-dependencies.jar [type_of_visualize(limited: 0, total : 1), size_of_graph, limit or start_node_index]

Limited Infection Visualize: [type_of_visualize = 0, size_of_graph, limit]
Total Infection Visualize: [type_of_visualize = 1, size_of_graph, start_node_index]

e.g. java -jar target/limitedInfection-0.0.1-SNAPSHOT-jar-with-dependencies.jar 0 50 20
This will visualize a limited infection on a graph of 50 users and a limit of 20 users.
e.g. java -jar target/limitedInfection-0.0.1-SNAPSHOT-jar-with-dependencies.jar 1 50 6
This will visualize a total infection on a graph of 50 users starting from user/node #6.

Red represents infected, while black represents not infected.



