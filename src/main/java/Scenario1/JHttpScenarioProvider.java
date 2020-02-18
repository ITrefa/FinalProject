package Scenario1;

import Scenario1.listeneres.ListenerByteSize;
import Scenario1.queriesProviders.ParametersQueriesProvider;
import Scenario1.queriesProviders.SimpleQueriesProvider;
import Scenario1.validators.JSONTypeValidator;
import Scenario1.validators.StatusCodeValidator;
import Scenario1.validators.XMLTypeValidator;
import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.loadbalancer.JLoadBalancer;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteria;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import Scenario1.util.PropertiesProvider;


@Configuration
public class JHttpScenarioProvider {
    @Bean
    public JLoadScenario loadScenario() {

        JTestDefinition theFirstTestDefinition =
                JTestDefinition.builder(Id.of("theFirstTestDefinition"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new SimpleQueriesProvider(new PropertiesProvider().getEndPoint1()))
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new JSONTypeValidator())
                        .addListener(new ListenerByteSize())
                        .build();


        JLoadProfileUsers theFirstGroup = JLoadProfileUsers.builder(NumberOfUsers.of(2)).withStartDelayInSeconds(0).build();

        JTerminationCriteria terminationCriteriaFirstGroup = JTerminationCriteriaIterations
                .of(IterationsNumber.of(5), MaxDurationInSeconds.of(10));

        JLoadTest jLoadTest1 = JLoadTest
                .builder(Id.of("load_test1"), theFirstTestDefinition, JLoadProfileUserGroups.builder(theFirstGroup).build(), terminationCriteriaFirstGroup)
                .build();





        JTestDefinition theSecondTestDefinition =
                JTestDefinition.builder(Id.of("theSecondTestDefinition"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new SimpleQueriesProvider(new PropertiesProvider().getEndPoint2()))
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new XMLTypeValidator())
                        .addListener(new ListenerByteSize())
                        .build();

        JLoadProfileUsers theSecondGroup1 = JLoadProfileUsers.builder(NumberOfUsers.of(1)).build();

        JLoadProfileUsers theSecondGroup2 = JLoadProfileUsers.builder(NumberOfUsers.of(2)).withStartDelayInSeconds(60).build();

        JTerminationCriteria terminationCriteriaSecondGroup = JTerminationCriteriaIterations
                .of(IterationsNumber.of(9), MaxDurationInSeconds.of(120));

        JLoadTest jLoadTest2 = JLoadTest
                .builder(Id.of("load_test2"), theSecondTestDefinition,
                        JLoadProfileUserGroups.builder(theSecondGroup2, theSecondGroup1).withDelayBetweenInvocationsInMilliseconds(15000).build(), terminationCriteriaSecondGroup)
                .build();



        JTestDefinition theThirdTestDefinition =
                JTestDefinition.builder(Id.of("theThirdTestDefinition"), new EndpointProvider(new PropertiesProvider().getGlobalEndpoint()))
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).build())
                        .withQueryProvider(new ParametersQueriesProvider())
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new JSONTypeValidator())
                        .addListener(new ListenerByteSize())
                        .build();


        JLoadProfileUsers theThirdGroup1 = JLoadProfileUsers.builder(NumberOfUsers.of(1)).build();

        JLoadProfileUsers theThirdGroup2 = JLoadProfileUsers.builder(NumberOfUsers.of(1)).withStartDelayInSeconds(15).build();

        JTerminationCriteria terminationCriteriaThirdGroup = JTerminationCriteriaIterations
                .of(IterationsNumber.of(15), MaxDurationInSeconds.of(180));


        JLoadTest jLoadTest3 = JLoadTest
                .builder(Id.of("load_test3"), theThirdTestDefinition,
                        JLoadProfileUserGroups.builder(theThirdGroup1, theThirdGroup2).withDelayBetweenInvocationsInMilliseconds(20000).build(), terminationCriteriaThirdGroup)
                .build();


        JParallelTestsGroup jParallelTestsGroup = JParallelTestsGroup
                .builder(Id.of("test_group1"), jLoadTest3, jLoadTest1, jLoadTest2)
                .build();


        return JLoadScenario.builder(Id.of("load_scenario1"), jParallelTestsGroup)
                .build();
    }
}
