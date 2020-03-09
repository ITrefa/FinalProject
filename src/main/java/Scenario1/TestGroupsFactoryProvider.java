package Scenario1;

import Scenario1.listeneres.ByteSizeMetric;
import Scenario1.listeneres.ItemsCountMetric;
import Scenario1.queriesProviders.ParametersQueriesProvider;
import Scenario1.queriesProviders.SimpleQueriesProvider;
import Scenario1.util.PropertiesProvider;
import Scenario1.validators.*;
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
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.DurationInSeconds;



public class TestGroupsFactoryProvider {

    public JParallelTestsGroup twoUsersInParallel() {

        JTestDefinition twoUsersInParallelDefinition =
                JTestDefinition.builder(Id.of("2UsersInParallel"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new ParametersQueriesProvider())
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new JSONTypeValidator())
                        .addValidator(new ItemValidatorJSON())
                       // .addListener(new ItemsCountMetric())
                        .build();

        JLoadTest oneUser15Delay = JLoadTest
                .builder(Id.of("1User15SecondsDelay"),
                        twoUsersInParallelDefinition,
                        JLoadProfileUserGroups
                                .builder(JLoadProfileUsers
                                        .builder(NumberOfUsers.of(1))
                                        .build())
                                .withDelayBetweenInvocationsInMilliseconds(15000)
                                .build(),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(180)))
                .build();

        JLoadTest oneUser20Delay = JLoadTest
                .builder(Id.of("1User20SecondsDelay"),
                        twoUsersInParallelDefinition,
                        JLoadProfileUserGroups
                                .builder(JLoadProfileUsers
                                        .builder(NumberOfUsers.of(1))
                                        .build())
                                .withDelayBetweenInvocationsInMilliseconds(20000)
                                .build(),
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
                        .addListener(new ByteSizeMetric())
                        .build();

        JLoadProfile jLoadProfileRps =
                JLoadProfileInvocation.builder(InvocationCount.of(5), ThreadCount.of(2)).
                        build();

        JLoadTest twoUsers5IterationsTest = JLoadTest
                .builder(Id.of("2UsersFor5Iterations"),
                        twoUsers5IterationsDefinition,
                        jLoadProfileRps,
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(100)))
                .build();

        return JParallelTestsGroup
                .builder(Id.of("twoUsers5Iterations"), twoUsers5IterationsTest)
                .build();
    }


    public JParallelTestsGroup threeUsersStartByOne20Seconds() {

        JTestDefinition threeUsers2MinDelayDefinition =
                JTestDefinition.builder(Id.of("3UsersStartByOne20Seconds"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new SimpleQueriesProvider(new PropertiesProvider().getEndPoint2()))
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new XMLTypeValidator())
                        .addValidator(new TitleValidatorXML())
                        .addListener(new ByteSizeMetric())
                        .build();


        JLoadTest oneUserEach20SecondsTest = JLoadTest
                .builder(Id.of("oneUserEach20SecondsWithoutDelay"),
                        threeUsers2MinDelayDefinition,
                        jLoadProfileUserGroupsProvider(1, 15000, 0 ),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(180)))
                .build();


        JLoadTest oneUserEach20SecondsDelayTest = JLoadTest
                .builder(Id.of("oneUserEach20SecondsWithDelay"),
                        threeUsers2MinDelayDefinition,
                        jLoadProfileUserGroupsProvider(1, 15000, 20),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(160)))
                .build();

        JLoadTest oneMoreUserEach20SecondsDelayTest = JLoadTest
                .builder(Id.of("oneMoreUserEach20SecondsWithDelay"),
                        threeUsers2MinDelayDefinition,
                        jLoadProfileUserGroupsProvider(1, 15000, 40),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(140)))
                .build();

        return JParallelTestsGroup
                .builder(Id.of("threeUsersStartingWithOne"), oneUserEach20SecondsTest, oneUserEach20SecondsDelayTest, oneMoreUserEach20SecondsDelayTest)
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
