package pp.tanks.model;

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
    private boolean droidSet = false;
    private XMLStreamReader xtr = null;
    private final List<String> errors = new ArrayList<>();

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
            occupied.clear();
            droidSet = false;
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
                case "breakableBlock":
                    tx = getIntAttribute("tx", 0);
                    ty = getIntAttribute("ty", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    if (!occupied.add(pos))
                        error("Multiple objects were created at same position in playable area.");
                    map.addBreakableBlock(new BreakableBlock(tmpPos, model));
                    break;

                case "unbreakableBlock":
                    tx = getIntAttribute("tx", 0);
                    ty = getIntAttribute("ty", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    if (!occupied.add(pos))
                        error("Multiple objects were created at same position in playable area.");
                    UnbreakableBlock uB = new UnbreakableBlock(model);
                    uB.setPos(tmpPos);
                    map.addUnbreakableBlock(uB);
                    break;

                case "reflectableBlock": {
                    tx = getIntAttribute("tx", 0);
                    ty = getIntAttribute("ty", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    if (!occupied.add(pos))
                        error("Multiple objects were created at same position in playable area.");
                    ReflectableBlock rB = new ReflectableBlock(model);
                    rB.setPos(tmpPos);
                    map.addReflectableBlocks(rB);
                    break;
                }

                case "playersTank": {
                    tx = getIntAttribute("tx", 0);
                    ty = getIntAttribute("ty", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    //PlayersTank pT = new PlayersTank(model);
                    //pT.setPos(tmpPos);
                    //map.addTanks(pT);
                    break;
                }

                case "enemy": {
                    tx = getIntAttribute("tx", 0);
                    ty = getIntAttribute("ty", 0);
                    tmpPos = new DoubleVec(tx, ty);
                    //Enemy e= new Enemy(model);
                    //e.setPos(tmpPos);
                    //map.addTanks(e);
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