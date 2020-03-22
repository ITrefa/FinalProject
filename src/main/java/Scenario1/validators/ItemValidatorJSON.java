package Scenario1.validators;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ItemValidatorJSON implements ResponseValidatorProvider {

    private static final int MIN_LENGTH_FOR_CORRECT_RESPONSE = 68;

    private static Logger log = LoggerFactory.getLogger(ItemValidatorJSON.class);

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String taskId, String sessionId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {
            @Override
            public String getName() {
                return "Not null item validator";
            }

            @Override
            public boolean validate(JHttpQuery jHttpQuery, JHttpEndpoint endpoint, JHttpResponse jHttpResponse, long l) {
                String json = jHttpResponse.getBody().toString();
                JSONObject obj = new JSONObject(json);
                String contentLength = obj.get("Content-Length").toString();
                if (Integer.parseInt(contentLength) < MIN_LENGTH_FOR_CORRECT_RESPONSE) {
                    log.error("Null item" + " for endpoint " + endpoint);
                    return false;
                }
                return true;
            }
        };
    }
}
