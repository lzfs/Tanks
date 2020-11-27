package pp.tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pp.tanks.model.Model;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class ModelTest {
    private static final String FILE_NAME = "pp/tanks/maps/map0.xml";
    private Model model;

    @BeforeEach
    public void setUp(){
        model = new Model(new Properties());
    }

    @Test
    public void loadMapTest() throws URISyntaxException {
        String test = "map0.xml";
        model.loadMap(test);
    }

    @Disabled
    public void loadMapTest2() throws IOException, XMLStreamException, URISyntaxException {
        System.out.println(getClass());
        System.out.println(getClass().getResource(FILE_NAME));
        final String path = getClass().getResource(FILE_NAME).toURI().getPath();
        System.out.println(path);
        model.loadMap2(new File(path));
    }
}
