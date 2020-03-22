package Scenario1;

import Scenario1.listeneres.ByteSizeMetric;
import Scenario1.listeneres.ItemsCountMetric;
import Scenario1.listeneres.ItemsCountMetricXML;
import Scenario1.queriesProviders.ParametersQueriesProvider;
import Scenario1.queriesProviders.SimpleQueriesProvider;
import Scenario1.util.PropertiesProvider;
import Scenario1.validators.*;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.load.*;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.InvocationCount;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.ThreadCount;
import com.griddynamics.jagger.user.test.configurations.loadbalancer.JLoadBalancer;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaBackground;
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
                        .addListener(new ItemsCountMetric())
                        .build();


        JLoadTest oneUser20SecondsDelayTest = JLoadTest
                .builder(Id.of("1Users20SecondsDelay"),
                        twoUsersInParallelDefinition,
                        jLoadProfileUserGroupsProvider(1, 20000),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(180)))
                .build();


        JLoadTest oneUserBackgroundTest = JLoadTest
                .builder(Id.of("1User20SecondsDelay"),
                        twoUsersInParallelDefinition,
                        jLoadProfileUserGroupsProvider(1, 15000),
                        JTerminationCriteriaBackground.getInstance())
                .build();

        return JParallelTestsGroup
                .builder(Id.of("twoUsersInParallel"), oneUser20SecondsDelayTest, oneUserBackgroundTest)
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

        JLoadProfile jLoadProfileInvocation =
                JLoadProfileInvocation.builder(InvocationCount.of(5), ThreadCount.of(2)).
                        build();

        JLoadTest twoUsers5IterationsTest = JLoadTest
                .builder(Id.of("2UsersFor5Iterations"),
                        twoUsers5IterationsDefinition,
                        jLoadProfileInvocation,
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
                        .addListener(new ItemsCountMetricXML())
                        .build();

        JLoadProfileUsers jLoadProfileUsers = JLoadProfileUsers
                .builder(NumberOfUsers.of(1)).withLifeTimeInSeconds(140)
                .withStartDelayInSeconds(40).build();

        JLoadProfileUsers jLoadProfileUsers1 = JLoadProfileUsers
                .builder(NumberOfUsers.of(1)).withLifeTimeInSeconds(160)
                .withStartDelayInSeconds(20).build();

        JLoadProfileUsers jLoadProfileUsers2 = JLoadProfileUsers
                .builder(NumberOfUsers.of(1)).build();

        JLoadTest threeUsersTest = JLoadTest
                .builder(Id.of("oneUserEach20SecondsWithoutDelay"),
                        threeUsers2MinDelayDefinition,
                        JLoadProfileUserGroups
                                .builder(jLoadProfileUsers, jLoadProfileUsers1, jLoadProfileUsers2)
                                .withDelayBetweenInvocationsInMilliseconds(15000)
                                .build(),
                        JTerminationCriteriaDuration.of(DurationInSeconds.of(180)))
                .build();

        return JParallelTestsGroup
                .builder(Id.of("threeUsersStartingWithOne"), threeUsersTest)
                .build();
    }

    private JLoadProfile jLoadProfileUserGroupsProvider(int numberOfUsers, int delayBetweenInvocationsMill) {
        return JLoadProfileUserGroups
                .builder(JLoadProfileUsers
                        .builder(NumberOfUsers.of(numberOfUsers))
                        .build())
                .withDelayBetweenInvocationsInMilliseconds(delayBetweenInvocationsMill)
                .build();
    }


}
