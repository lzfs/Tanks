package pp.tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pp.tanks.model.Model;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTest {
    private static final String FILE_NAME = "pp/tanks/maps/map0.xml";
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new Model(new Properties());
    }

    @Test
    public void test() {
        model.loadMap("map0.xml");
        assertEquals(71, model.getTanksMap().getBlocks().size());
    }
}
