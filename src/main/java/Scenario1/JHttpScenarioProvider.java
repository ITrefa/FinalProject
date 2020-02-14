package Scenario1;

import Scenario1.listeneres.ListenerByteSize;
import Scenario1.validators.JSONTypeValidator;
import Scenario1.validators.StatusCodeValidator;
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
                        .withLoadBalancer(JLoadBalancer.builder(JLoadBalancer.DefaultLoadBalancer.ONE_BY_ONE).withExclusiveAccess().build())
                        .withQueryProvider(new QueriesProvider())
                        .addValidator(new StatusCodeValidator())
                        .addValidator(new JSONTypeValidator())
                        .addListener(new ListenerByteSize())
                        .build();


        JLoadProfileUsers theFirstGroup = JLoadProfileUsers.builder(NumberOfUsers.of(2)).withStartDelayInSeconds(0).withLifeTimeInSeconds(20).build();

        JTerminationCriteria TerminationCriteriaFirstGroup = JTerminationCriteriaIterations
                .of(IterationsNumber.of(5), MaxDurationInSeconds.of(10));

        JLoadTest jLoadTest1 = JLoadTest
                .builder(Id.of("load_test1"), theFirstTestDefinition, JLoadProfileUserGroups.builder(theFirstGroup).build(), TerminationCriteriaFirstGroup)
                .build();

        JParallelTestsGroup jParallelTestsGroup = JParallelTestsGroup
                .builder(Id.of("test_group1"), jLoadTest1)
                .build();


        return JLoadScenario.builder(Id.of("load_scenario1"), jParallelTestsGroup)
                .build();
    }
}
