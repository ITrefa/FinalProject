package Scenario1.validators;

import Scenario1.listeneres.ListenerByteSize;
import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class StatusCodeValidator implements ResponseValidatorProvider {

    private static Logger log = LoggerFactory.getLogger(StatusCodeValidator.class);

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String taskId, String sessionId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {
            @Override
            public String getName() {
                return "Successful status code validator";
            }

            @Override
            public boolean validate(JHttpQuery jHttpQuery, JHttpEndpoint endpoint, JHttpResponse jHttpResponse, long l) {
                if (jHttpResponse.getStatus() != HttpStatus.OK) {
                    log.error("Invalid response code " + jHttpResponse.getStatus() + " for endpoint " + endpoint);
                    return false;
                }
                return true;
            }
        };
    }
}
