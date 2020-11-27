package pp.tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.tanks.model.Model;

import java.util.Properties;

public class ModelTest {
    private static final String FILE_NAME = "pp/tanks/maps/map0.xml";
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new Model(new Properties());
    }

    @Test
    public void loadMapTest() {
        String test = "map0.xml";
        model.loadMap(test);
    }
}
