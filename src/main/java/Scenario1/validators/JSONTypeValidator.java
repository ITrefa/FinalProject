package Scenario1.validators;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONTypeValidator implements ResponseValidatorProvider {

    private static Logger log = LoggerFactory.getLogger(JSONTypeValidator.class);

    @Override
        public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String taskId, String sessionId, NodeContext kernelContext) {
            return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {
                @Override
                public String getName() {
                    return "Successful json type validator";
                }

                @Override
                public boolean validate(JHttpQuery jHttpQuery, JHttpEndpoint endpoint, JHttpResponse jHttpResponse, long l) {
                    if (!jHttpResponse.getHeaders().get("Content-Type").contains("application/json")) {
                        log.error("Invalid content type" + jHttpResponse.getHeaders().get("Content-Type") + " for endpoint " + endpoint);
                        return false;
                    }
                    return true;
                }
            };
        }
}
