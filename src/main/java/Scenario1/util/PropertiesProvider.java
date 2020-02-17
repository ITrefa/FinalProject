package Scenario1.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesProvider {

    public Properties getTestProperties() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("test.properties");
        Properties prop = new Properties();
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public String getEndPoint1() {
        return getTestProperties().getProperty("endpoint1");
    }

    public String getEndPoint2() {
        return getTestProperties().getProperty("endpoint2");
    }

    public String getGlobalEndpoint() {
        return getTestProperties().getProperty("globalEndPoint");
    }

    public String getPathToCsvFile() {
        File file = new File(getTestProperties().getProperty("pathToFile"));
        return file.getAbsolutePath();
    }

    public String getPathToSearchPhrase() {
        return getTestProperties().getProperty("searchPhrase");
    }


}
