package Scenario1.queriesProviders;

import com.griddynamics.jagger.invoker.v2.JHttpQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleQueriesProvider extends JHttpQuery implements Iterable {
    private final String path;

    public SimpleQueriesProvider(String path) {
        this.path = path;
    }

    @Override
    public Iterator iterator() {
        List<JHttpQuery> queries = new ArrayList<>();
            queries.add(new JHttpQuery()
                    .path(path)
                    .get()
                    .responseBodyType(String.class));

        return queries.iterator();
    }
}
