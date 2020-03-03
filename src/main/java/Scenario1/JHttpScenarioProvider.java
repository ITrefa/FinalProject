package Scenario1;

import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//NotNullInvocation listener
//defaultresponsevalidatorprovider


@Configuration
public class JHttpScenarioProvider {
    @Bean
    public JLoadScenario loadScenario() {

        JParallelTestsGroup theFirstTask = new TestGroupsFactoryProvider().twoUsers5Iterations();
        JParallelTestsGroup theSecondTask = new TestGroupsFactoryProvider().threeUsers2MinDelay();
        JParallelTestsGroup theThirdTask = new TestGroupsFactoryProvider().twoUsersInParallel();

        return JLoadScenario.builder(Id.of("scenarioForFinalTask"), theFirstTask)
                .build();
    }


}
