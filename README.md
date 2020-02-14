# FinalTask
Final project for Performance QA course.


## Getting started
To use this application you need to set up jagger:
* [jagger](https://github.com/griddynamics/jagger) - Jagger project;
* [jagger](http://griddynamics.github.io/jagger/doc/index.html) - Jagger user manual;


## Part 1
You have the following situation

New customer requests for performance testing on his project.
He knows almost nothing about performance testing. 

Project description: eShop with classic functionality - browse, search.

Checkout will be provided by 3d party and it's out of scope at the moment. 

According to business expectations we will have about 200 user actions per second in normal days and up to 3 times more during black friday.

Please provide your plan for performance testing including:

Scenarios
Type of tests
Key metrics to monitor
Provide complete plan with explanations for all points.


## Part 2 
Create scenario from 3 tests groups with the following parameters:

1. 2 users 5 iterations.
2. 3 user for 2 minutes with 15 seconds delay between invocations starting by 1 user each 20 seconds.
3. 2 users in parallel: 1 user for 3 minutes with 20 seconds delay between invocation, second with 15 seconds delay working in background.


Use the following site as test target: http://httpbin.org/ with the following endpoints:
1. /get
2. /xml
2. /response-headers?key=value

Use unique end-point for each test group.

For last end-point generate file with key and value pairs and read test data from it.

Create meaningful response validator and metric calculator for each endpoint.

At least one validator and calculator should be related to response body.

Provide project with results from one run.
Explain meaning of validators and metric calculators.

#### Scenario
ScenarioID = load_scenario1. 

To launch a load scenario, set 'jagger.load.scenario.id.to.execute' property's value equal to the load scenario id.
You can do it via system properties or in the 'environment.properties' file.

```
cd ./target/{artifactdId}-{version}-full/
./start.sh profiles/basic/environment.properties 
```
or 
```
cd ./target/{artifactdId}-{version}-full/
./start.sh profiles/basic/environment.properties -Djagger.load.scenario.id.to.execute = scenarioId
```






