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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class ItemsCountMetricXML extends ServicesAware implements Provider<InvocationListener> {

    private static Logger log = LoggerFactory.getLogger(ItemsCountMetricXML.class);

    private static int count = 0;


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
                    String body = jHttpResponse.getBody().toString();

                    Document document = toXmlDocument(body);

                    Element root = document.getDocumentElement();
                    NodeList nodes = root.getChildNodes();

                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node node = nodes.item(i);
                        if (node instanceof Element) {
                            Element child = (Element) node;
                            if (child.hasChildNodes()) {
                                NodeList nodeList = child.getChildNodes();
                                for (int j = 0; j < nodeList.getLength(); j++) {
                                    Node node1 = nodeList.item(j);
                                   if (node1.getNodeValue().equals("Why")) {
                                       count = count + 1;
                                   }
                                }
                            }
                        }
                    }


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

    private static Document toXmlDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
