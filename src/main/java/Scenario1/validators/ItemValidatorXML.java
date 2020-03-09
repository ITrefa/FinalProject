package Scenario1.validators;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemValidatorXML implements ResponseValidatorProvider {

    //TODO make it different
    private static Logger log = LoggerFactory.getLogger(ItemValidatorXML.class);

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String taskId, String sessionId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {
            @Override
            public String getName() {
                return "Not null item validator";
            }

            @Override
            public boolean validate(JHttpQuery jHttpQuery, JHttpEndpoint endpoint, JHttpResponse jHttpResponse, long l) {
                if (!jHttpResponse.getBody().toString().contains("item")) {
                    log.error("Null item" + " for endpoint " + endpoint);
                    return false;
                }
                return true;
            }
        };
    }
}