package Scenario1;

import Scenario1.listeneres.ListenerByteSize;
import Scenario1.queriesProviders.ParametersQueriesProvider;
import Scenario1.queriesProviders.SimpleQueriesProvider;
import Scenario1.util.PropertiesProvider;
import Scenario1.validators.UrlValidator;
import Scenario1.validators.JSONTypeValidator;
import Scenario1.validators.StatusCodeValidator;
import Scenario1.validators.XMLTypeValidator;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileInvocation;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.InvocationCount;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.ThreadCount;
import com.griddynamics.jagger.user.test.configurations.loadbalancer.JLoadBalancer;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaDuration;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.DurationInSeconds;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;

public class TestGroupsFactoryProvider {

    public JParallelTestsGroup twoUsersInParallel() {



        JTestDefinition twoUsersInParallelDefinition =
                JTestDefinition.builder(Id.of("2UsersInParallel"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new ParametersQueriesProvider())
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new JSONTypeValidator())
                        .addListener(new ListenerByteSize())
                        .build();


        JLoadTest oneUser15Delay = JLoadTest
                .builder(Id.of("1User15SecondsDelay"),
                        twoUsersInParallelDefinition,
                        jLoadProfileUserGroupsProvider(1, 15000, 0),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(180)))
                .build();

        JLoadTest oneUser20Delay = JLoadTest
                .builder(Id.of("1User20SecondsDelay"),
                        twoUsersInParallelDefinition,
                        jLoadProfileUserGroupsProvider(1, 20000, 0),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(180)))
                .build();


        return JParallelTestsGroup
                .builder(Id.of("twoUsersInParallel"), oneUser15Delay, oneUser20Delay)
                .build();

    }

    public JParallelTestsGroup twoUsers5Iterations() {
        JTestDefinition twoUsers5IterationsDefinition =
                JTestDefinition.builder(Id.of("2Users5Iterations"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new SimpleQueriesProvider(new PropertiesProvider().getEndPoint1()))
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new JSONTypeValidator())
                        .addValidator(new UrlValidator())
                        .addListener(new ListenerByteSize())
                        .build();

        JLoadProfile jLoadProfileRps =
                JLoadProfileInvocation.builder(InvocationCount.of(5), ThreadCount.of(2)).
                        build();

        JLoadTest twoUsers5IterationsTest = JLoadTest
                .builder(Id.of("2UsersFor5Iterations"),
                        twoUsers5IterationsDefinition,
                        jLoadProfileRps,
                        JTerminationCriteriaIterations.of(IterationsNumber.of(1), MaxDurationInSeconds.of(40)))
                .build();




        return JParallelTestsGroup
                .builder(Id.of("twoUsers5Iterations"), twoUsers5IterationsTest)
                .build();
    }


    public JParallelTestsGroup threeUsers2MinDelay() {

        JTestDefinition threeUsers2MinDelayDefinition =
                JTestDefinition.builder(Id.of("3Users2MinDelay"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new SimpleQueriesProvider(new PropertiesProvider().getEndPoint2()))
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new XMLTypeValidator())
                        .addListener(new ListenerByteSize())
                        .build();

        JLoadTest oneUserEach20SecondsTest = JLoadTest
                .builder(Id.of("oneUserEach20Seconds"),
                        threeUsers2MinDelayDefinition,
                        jLoadProfileUserGroupsProvider(1, 20000, 0),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(120)))
                .build();

        JLoadTest twoUsersEach15SecondsTest = JLoadTest
                .builder(Id.of("twoUsersEach15Seconds"),
                        threeUsers2MinDelayDefinition,
                        jLoadProfileUserGroupsProvider(2, 15000, 20),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(100)))
                .build();

        return JParallelTestsGroup
                .builder(Id.of("threeUsersStartingWithOne"), oneUserEach20SecondsTest, twoUsersEach15SecondsTest)
                .build();
    }


    private JLoadProfile jLoadProfileUserGroupsProvider(int numberOfUsers, int delayBetweenInvocationsMill, int startDelay) {
        return JLoadProfileUserGroups
                .builder(JLoadProfileUsers
                        .builder(NumberOfUsers.of(numberOfUsers))
                        .withStartDelayInSeconds(startDelay)
                        .build())
                .withDelayBetweenInvocationsInMilliseconds(delayBetweenInvocationsMill)
                .build();
    }

}
