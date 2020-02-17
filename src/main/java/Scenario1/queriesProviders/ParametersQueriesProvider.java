package Scenario1.queriesProviders;

import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import Scenario1.util.CsvProvider;
import Scenario1.util.PropertiesProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ParametersQueriesProvider extends JHttpQuery implements Iterable {
    CsvProvider csvProvider = new CsvProvider();

    @Override
    public Iterator iterator() {
        List<JHttpQuery> queries = new ArrayList<>();
        PropertiesProvider propertiesProvider = new PropertiesProvider();
        List<String> values = csvProvider.CsvProvider(propertiesProvider.getPathToCsvFile());

        for (int i = 0; i < values.size() - 1; i++) {
            queries.add(new JHttpQuery()
                    .path(propertiesProvider.getPathToSearchPhrase())
                    .queryParam(values.get(i), values.get(i + 1))
                    .get()
                    .responseBodyType(String.class));
        }
        return queries.iterator();


    }
}


