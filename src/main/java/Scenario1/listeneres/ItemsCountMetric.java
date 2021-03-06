package Scenario1.listeneres;

import com.griddynamics.jagger.engine.e1.Provider;
import com.griddynamics.jagger.engine.e1.collector.AvgMetricAggregatorProvider;
import com.griddynamics.jagger.engine.e1.collector.MaxMetricAggregatorProvider;
import com.griddynamics.jagger.engine.e1.collector.MetricDescription;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationInfo;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationListener;
import com.griddynamics.jagger.engine.e1.services.ServicesAware;
import com.griddynamics.jagger.invoker.InvocationException;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemsCountMetric extends ServicesAware implements Provider<InvocationListener> {

    private static Logger log = LoggerFactory.getLogger(ItemsCountMetric.class);

    private static int count = 0;

    private static final int OFFSET_INDEX = 5;

    private final String metricName = "count-of-items";

    @Override
    protected void init() {
        getMetricService().createMetric(new MetricDescription(metricName)
                .displayName("The amount of items")
                .showSummary(true)
                .plotData(true)
                .addAggregator(new AvgMetricAggregatorProvider())
                .addAggregator(new MaxMetricAggregatorProvider()));
    }

    @Override
    public InvocationListener provide() {
        return new InvocationListener() {
            @Override
            public void onStart(InvocationInfo invocationInfo) {
            }

            @Override
            public void onSuccess(InvocationInfo invocationInfo) {
                if (invocationInfo.getResult() != null) {
                    JHttpResponse jHttpResponse = (JHttpResponse) invocationInfo.getResult();
                    JHttpQuery jHttpQuery = (JHttpQuery) invocationInfo.getQuery();
                    JHttpEndpoint jHttpEndpoint = (JHttpEndpoint) invocationInfo.getEndpoint();
                    log.info("Response: " + jHttpResponse);
                    log.info("Query: " + jHttpQuery);
                    log.info("Endpoint: " + jHttpEndpoint);
                    String[] array = jHttpResponse.getHeaders().toString().split(",");
                    log.info("RESPONSE ITEM: " + array[OFFSET_INDEX]);
                    count++;
                    getMetricService().saveValue(metricName, count);
                }
            }

            @Override
            public void onFail(InvocationInfo invocationInfo, InvocationException e) {
            }

            @Override
            public void onError(InvocationInfo invocationInfo, Throwable error) {
            }
        };
    }
}
