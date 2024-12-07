package systems.bdev.tools;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class DataSourceLoader {
    public Instances loadData(String address) throws Exception {
        InputStream inputStream = null;
        try {
            URL url = URI.create(address).toURL();
            inputStream = url.openStream();

            CSVLoader loader = new CSVLoader();
            loader.setSource(inputStream);
            loader.setFieldSeparator(";");
            return loader.getDataSet();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
