package pp.droids.model;

import pp.droids.model.item.Droid;
import pp.droids.model.item.Enemy;
import pp.droids.model.item.Item;
import pp.droids.model.item.Mover;
import pp.droids.model.item.Obstacle;
import pp.droids.model.item.Projectile;
import pp.droids.model.item.Rocket;
import pp.droids.model.item.Visitor;
import pp.util.DoubleVec;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that allows to save a game map to an XML file. This class uses a StAX XML writer to write an XML stream.
 */
class DroidsMapFileWriter {
    private static final Logger LOGGER = Logger.getLogger(DroidsMapFileWriter.class.getName());

    private XMLStreamWriter xtw = null;
    private final DroidsGameModel model;

    /**
     * Creates a writer object for the specified game model.
     *
     * @param model the model of the game whose map is going to be written using {@linkplain #writeFile(java.io.File)}
     */
    public DroidsMapFileWriter(DroidsGameModel model) {
        this.model = model;
    }

    /**
     * Writes the droids map of the game model specified in the constructor to the specified xml file.
     *
     * @param file xml file representing where the droids map is written to.
     * @throws FileNotFoundException if the file doesn't exist or cannot be opened
     * @throws XMLStreamException    if the file the produced file is not a valid xml file
     */
    public void writeFile(File file) throws FileNotFoundException, XMLStreamException {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        FileOutputStream fos = new FileOutputStream(file);
        xtw = xof.createXMLStreamWriter(fos, "UTF-8");

        xtw.setPrefix("", "");

        xtw.writeStartDocument("UTF-8", "1.0");
        xtw.writeCharacters("\n");
        xtw.writeStartElement("", "map");
        xtw.writeAttribute("w", Integer.toString(model.getDroidsMap().getWidth()));
        xtw.writeAttribute("h", Integer.toString(model.getDroidsMap().getHeight()));
        xtw.writeCharacters("\n");

        // write DroidsMap Items
        for (Item item : model.getDroidsMap())
            item.accept(new Visitor() {
                @Override
                public void visit(Droid droid) {
                    writeItem("droid", droid);
                }

                @Override
                public void visit(Obstacle obstacle) {
                    writeItem("obstacle", obstacle);
                }

                @Override
                public void visit(Enemy enemy) {
                    writeItem("enemy", enemy);
                }

                @Override
                public void visit(Projectile proj) {}

                @Override
                public void visit(Rocket rocket) {
                    writeMovingItem("rocket", rocket, rocket.getPos());
                }
            });

        xtw.writeEndElement();
        xtw.writeEndDocument();

        xtw.flush();
        xtw.close();

        try {
            fos.close();
            LOGGER.info("Wrote map to " + file);
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Writes an item into the XML File
     *
     * @param name name
     * @param item item to be written
     */
    private void writeItem(String name, Item item) {
        try {
            xtw.writeCharacters("   ");
            xtw.writeStartElement("", name);
            xtw.writeAttribute("x", round(item.getPos().x));
            xtw.writeAttribute("y", round(item.getPos().y));
            xtw.writeEndElement();
            xtw.writeCharacters("\n");
        }
        catch (XMLStreamException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Writes a moving item into the XML File
     *
     * @param name name
     * @param item item to be written
     */
    private void writeMovingItem(String name, Mover item, DoubleVec pos) {
        try {
            xtw.writeCharacters("   ");
            xtw.writeStartElement("", name);
            xtw.writeAttribute("x", round(pos.x));
            xtw.writeAttribute("y", round(pos.y));
            xtw.writeAttribute("tx", round(item.getTarget().x));
            xtw.writeAttribute("ty", round(item.getTarget().y));
            xtw.writeEndElement();
            xtw.writeCharacters("\n");
        }
        catch (XMLStreamException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Computes the closest integer value to the argument and returns its string representation.
     */
    private static String round(double v) {
        return Long.toString(Math.round(v));
    }
}