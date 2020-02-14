package Scenario1;

import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EndpointProvider implements Iterable {
    private List<JHttpEndpoint> endpoints = new ArrayList<>();

    public EndpointProvider(String endpoint) {

        JHttpEndpoint httpEndpoint = new JHttpEndpoint(URI.create(endpoint));
        endpoints.add(httpEndpoint);
    }

    @Override
    public Iterator<JHttpEndpoint> iterator() {
        return endpoints.iterator();
    }
}
