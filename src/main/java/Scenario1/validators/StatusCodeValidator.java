package Scenario1.validators;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.springframework.http.HttpStatus;

public class StatusCodeValidator implements ResponseValidatorProvider {

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String taskId, String sessionId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {
            @Override
            public String getName() {
                return "Successful status code validator";
            }

            @Override
            public boolean validate(JHttpQuery jHttpQuery, JHttpEndpoint endpoint, JHttpResponse jHttpResponse, long l) {
                return jHttpResponse.getStatus() == HttpStatus.OK;
            }
        };
    }
}
