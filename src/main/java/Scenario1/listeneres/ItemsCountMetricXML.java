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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
                    XPathFactory xpathFactory = XPathFactory.newInstance();
                    XPath xpath = xpathFactory.newXPath();

                    if (getItem(document, xpath, 1).contains("Why ") && getItem(document, xpath, 1).contains(" are great")) {
                        count++;
                    }
                    if (getItem(document, xpath, 3).contains("Who ") && getItem(document, xpath, 3).contains(" WonderWidgets")) {
                        count++;
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

    private static List<String> getItem(Document doc, XPath xpath, int number) {
        List<String> list = new ArrayList<>();
        try {
            XPathExpression expr =
                    xpath.compile("/slideshow/slide/item[" + number + "]/text()");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

}
