# resourcescheduler
Resource Scheduler responsible for managing the interaction with an expensive external resource.
 * The Schedule manage the interaction based on the availability of the external resource.
 * It can be configured with the number of resources, as resources became available it sends the next task.

# Requirement
	1 - Java SE 7
	2 - Maven 3+

# To test the project please:

	1 - Compile:
		mvn clean install
		
	2 - Execute:
		mvn exec:java
