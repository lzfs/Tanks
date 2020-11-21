package pp.tanks.model;

import pp.tanks.message.data.BBData;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.TankData;
import pp.tanks.model.item.*;
import pp.util.DoubleVec;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A class that allows to load a game map from an XML file. This class uses a StAX XML reader to parse XML stream.
 */
class TanksMapFileReader {
    private static final Logger LOGGER = Logger.getLogger(TanksMapFileReader.class.getName());
    private final Model model;
    private TanksMap map;
    private final Set<DoubleVec> occupied = new HashSet<>();
    private XMLStreamReader xtr = null;
    private final List<String> errors = new ArrayList<>();
    private int counter = 5;

    /**
     * Creates an instance of this class for the specified game model.
     */
    public TanksMapFileReader(Model model) {
        this.model = model;
    }

    /**
     * Reads an xml file and returns the represented game map.
     *
     * @param file xml file representing a game map
     * @throws IOException        if the file doesn't exist, cannot be opened, or any other IO error occurred.
     * @throws XMLStreamException if the file is no valid xml file
     */
    public TanksMap readFile(File file) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Reader reader = new FileReader(file);
        xtr = factory.createXMLStreamReader(reader);
        map = null;
        errors.clear();

        try {
            while (xtr.hasNext()) {
                if (xtr.getEventType() == XMLStreamConstants.START_ELEMENT)
                    handleElement();
                xtr.next();
            }
        }
        finally {
            xtr.close();
        }
        if (!errors.isEmpty())
            throw new IOException("Datei enthält keine gültige Map");
        return map;
    }

    /**
     * Called for every XML element, which represent an item that is added to the game map.
     */
    private void handleElement() {
        String elemName = xtr.getLocalName();
        if (elemName.equals("map")) {
            final int w = getIntAttribute("w", 100);
            final int h = getIntAttribute("h", 100);
            map = new TanksMap(model, w, h);
            occupied.clear(); ;
        }
        else if (map == null)
            error("unexpected XML element '" + elemName + "'");
        else {
            // load position
            final int x = getIntAttribute("x", 0);
            final int y = getIntAttribute("y", 0);

            if (x < 0 || x > map.getWidth() || y < 0 || y > map.getHeight())
                warning(elemName + " is outside of playable area.");
            // create object
            final DoubleVec pos = new DoubleVec(x, y);
            DoubleVec tmpPos;
            int tx;
            int ty;
            switch (elemName) {
                case "bBlock":
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    if (!occupied.add(pos))
                        error("Multiple objects were created at same position in playable area.");
                    map.addBreakableBlock(new BreakableBlock(model, new BBData(tmpPos, counter, 20))); //TODO
                    counter += 1;
                    break;

                case "uBlock":
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    if (!occupied.add(pos))
                        error("Multiple objects were created at same position in playable area.");
                    UnbreakableBlock uB = new UnbreakableBlock(model, new Data(tmpPos, counter)); //TODO
                    counter += 1;
                    uB.setPos(tmpPos);
                    map.addUnbreakableBlock(uB);
                    break;

                case "rBlock": {
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    if (!occupied.add(pos))
                        error("Multiple objects were created at same position in playable area.");
                    ReflectableBlock rB = new ReflectableBlock(model, new Data(tmpPos, counter)); //TODO
                    counter += 1;
                    rB.setPos(tmpPos);
                    map.addReflectableBlocks(rB);
                    break;
                }

                case "playersTank": {
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    PlayersTank pT = new PlayersTank(model, 3, new Armor(20, 5), new LightTurret(), new TankData(tmpPos, 1000, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0)));
                    map.addTanks(pT);
                    break;
                }

                case "enemy": {
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    break;
                }
                case "apc": {
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    TankData data = new TankData(tmpPos, 0010, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0));
                    ArmoredPersonnelCarrier apc = new ArmoredPersonnelCarrier(model, data);
                    map.addTanks(apc);
                    break;
                }
                case "howitzer": {
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    TankData data = new TankData(tmpPos, 0100, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0));
                    Howitzer howitzer = new Howitzer(model, data);
                    map.addTanks(howitzer);
                    break;
                }

                case "tankDestroyer": {
                    tx = getIntAttribute("x", 0);
                    ty = getIntAttribute("y", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    TankData data = new TankData(tmpPos, 0001, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0));
                    TankDestroyer tankDestroyer = new TankDestroyer(model, data);
                    map.addTanks(tankDestroyer);
                    break;
                }
                case "enemyCounter":
                case "levelCounter": {
                    break;
                }
                default:
                    error("unknown XML element '" + elemName + "'");
            }
        }
    }

    /**
     * Emits an error
     *
     * @param text the error message
     */
    private void error(String text) {
        LOGGER.warning(text);
        errors.add(text);
    }

    /**
     * Emits a warning
     *
     * @param text the error message
     */
    private void warning(String text) {
        LOGGER.warning(text);
    }

    /**
     * Reads an integer value of an attribute of the current XML element.
     *
     * @param name         the name of the attribute
     * @param defaultValue the default value used when this attribute is missing or if it has a value
     *                     that does not represent an integer number.
     * @return the integer value of the attribute or null if the attribute is missing or has a non-integer value.
     */
    private int getIntAttribute(String name, int defaultValue) {
        String value = getAttribute(name);
        if (value == null)
            return defaultValue;
        else
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                LOGGER.warning("Attribute " + name + " should be an int, but has value " + value);
                return defaultValue;
            }
    }

    /**
     * Reads the string value of an attribute of the current XML element.
     *
     * @param name the name of the attribute
     * @return the string value of the attribute or null if the attribute is missing.
     */
    private String getAttribute(String name) {
        for (int i = 0; i < xtr.getAttributeCount(); i++)
            if (xtr.getAttributeLocalName(i).equals(name))
                return xtr.getAttributeValue(i);
        return null;
    }
}
