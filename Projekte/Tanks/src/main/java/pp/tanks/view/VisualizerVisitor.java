package pp.tanks.view;

import pp.tanks.TanksImageProperty;
import pp.tanks.model.item.Armor;
import pp.tanks.model.item.BreakableBlock;
import pp.tanks.model.item.COMEnemy;
import pp.tanks.model.item.Enemy;
import pp.tanks.model.item.HeavyProjectile;
import pp.tanks.model.item.Item;
import pp.tanks.model.item.LightArmor;
import pp.tanks.model.item.LightProjectile;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.NormalArmor;
import pp.tanks.model.item.NormalProjectile;
import pp.tanks.model.item.NormalTurret;
import pp.tanks.model.item.Oil;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.ReflectableBlock;
import pp.tanks.model.item.Track;
import pp.tanks.model.item.TrackIntensity;
import pp.tanks.model.item.Turret;
import pp.tanks.model.item.UnbreakableBlock;
import pp.tanks.model.item.Visitor;
import pp.util.DoubleVec;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Item visitor that adds visual representations of items to the tanks map view
 */
public class VisualizerVisitor implements Visitor {

    /**
     * Enumeration for the shape that drawable items can have
     */
    private enum Shape {RECTANGLE, OVAL, DIRECTED_OVAL}

    private final TanksMapView view;

    public VisualizerVisitor(TanksMapView view) {
        this.view = view;
    }

    /**
     * visit method for the Visitor-Pattern
     *
     * @param playersTank the player Tank
     */
    @Override
    public void visit(PlayersTank playersTank) {
        boolean destroyed = playersTank.isDestroyed();
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(playersTank.getPos());
        context.translate(pos.x, pos.y);
        if (!destroyed) {
            context.rotate((playersTank.getRotation() + 90) % 360);
            context.scale(0.7, 0.7);

            Armor armor = playersTank.getArmor();
            if (armor instanceof LightArmor) {
                drawImage(TanksImageProperty.armor1, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else if (armor instanceof NormalArmor) {
                drawImage(TanksImageProperty.armor2, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else {
                drawImage(TanksImageProperty.armor3, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }

            context.rotate(-playersTank.getRotation());
            context.rotate(playersTank.getData().getTurretDir().angle());

            Turret turret = playersTank.getTurret();
            if (turret instanceof LightTurret) {
                drawImage(TanksImageProperty.turret1, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else if (turret instanceof NormalTurret) {
                drawImage(TanksImageProperty.turret2, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else {
                drawImage(TanksImageProperty.turret3, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
        }
        else {
            drawImage(TanksImageProperty.tankDestroyed, Shape.DIRECTED_OVAL, Color.GREEN, 1);
        }
        context.setTransform(ori);
    }

    /**
     * Draw a track behind the tank
     *
     * @param posTrack the Track
     */
    public void drawMeATrack(Track posTrack) {
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(posTrack.getVec());
        context.translate(pos.x, pos.y);
        context.rotate((posTrack.getRotation() + 90) % 360);
        if (posTrack.getintensity() == TrackIntensity.NORMAL) {
            drawImage(TanksImageProperty.tracks, Shape.DIRECTED_OVAL, Color.RED, 1);
        }
        else if (posTrack.getintensity() == TrackIntensity.OIL) {
            drawImage(TanksImageProperty.tracksDark, Shape.DIRECTED_OVAL, Color.RED, 1);
        }
        context.setTransform(ori);
    }

    /**
     * visit method for the Visitor-Pattern
     *
     * @param enemy the enemy Tank
     */
    @Override
    public void visit(Enemy enemy) {
        List<Track> posList = enemy.getTracksPosList();
        for (Track posTrack : posList) {
            drawMeATrack(posTrack);
        }
        boolean destroyed = enemy.isDestroyed();

        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(enemy.getPos());
        context.translate(pos.x, pos.y);

        if (!destroyed) {
            context.rotate((enemy.getRotation() + 90) % 360);
            context.scale(0.7, 0.7);

            Armor armor = enemy.getArmor();
            if (armor instanceof LightArmor) {
                drawImage(TanksImageProperty.armor1, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else if (armor instanceof NormalArmor) {
                drawImage(TanksImageProperty.armor2, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else {
                drawImage(TanksImageProperty.armor3, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }

            context.rotate(-enemy.getRotation());
            context.rotate(enemy.getData().getTurretDir().angle());

            Turret turret = enemy.getTurret();
            if (turret instanceof LightTurret) {
                drawImage(TanksImageProperty.turret1, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else if (turret instanceof NormalTurret) {
                drawImage(TanksImageProperty.turret2, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else {
                drawImage(TanksImageProperty.turret3, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
        }
        else {
            drawImage(TanksImageProperty.tankDestroyed, Shape.DIRECTED_OVAL, Color.GREEN, 1);
        }
        context.setTransform(ori);
    }

    /**
     * visit method for the Visitor-Pattern
     *
     * @param comEnemy the comEnemy Tank
     */
    @Override
    public void visit(COMEnemy comEnemy) {
        List<Track> posList = comEnemy.getTracksPosList();
        for (Track posTrack : posList) {
            drawMeATrack(posTrack);
        }

        boolean destroyed = comEnemy.isDestroyed();
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(comEnemy.getPos());
        context.translate(pos.x, pos.y);
        if (!destroyed) {
            context.rotate((comEnemy.getRotation() + 90) % 360);
            context.scale(0.7, 0.7);
            Armor armor = comEnemy.getArmor();
            if (armor instanceof LightArmor) {
                drawImage(TanksImageProperty.armor1, Shape.DIRECTED_OVAL, Color.GREEN, 0.9);
            }
            else if (armor instanceof NormalArmor) {
                drawImage(TanksImageProperty.armor2, Shape.DIRECTED_OVAL, Color.GREEN, 0.7);
            }
            else {
                drawImage(TanksImageProperty.armor3, Shape.DIRECTED_OVAL, Color.GREEN, 0.65);
            }
            context.rotate(-comEnemy.getRotation());
            context.rotate(comEnemy.getData().getTurretDir().angle());

            Turret turret = comEnemy.getTurret();
            if (turret instanceof LightTurret) {
                drawImage(TanksImageProperty.turret1, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else if (turret instanceof NormalTurret) {
                drawImage(TanksImageProperty.turret2, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
            else {
                drawImage(TanksImageProperty.turret3, Shape.DIRECTED_OVAL, Color.GREEN, 1);
            }
        }
        else {
            drawImage(TanksImageProperty.tankDestroyed, Shape.DIRECTED_OVAL, Color.GREEN, 1);
        }
        context.setTransform(ori);
    }

    /**
     * visit method for the Visitor-Pattern
     * @param bBlock the bBlock
     */
    @Override
    public void visit(BreakableBlock bBlock) {
        if (!bBlock.isDestroyed()) {
            drawItemScale(bBlock, TanksImageProperty.bBlock, Shape.RECTANGLE, Color.BLUE, 0.9);
        }
    }

    /**
     * visit method for the Visitor-Pattern
     * @param rBlock the rBlock
     */
    @Override
    public void visit(ReflectableBlock rBlock) {
        drawItemScale(rBlock, TanksImageProperty.rBlock, Shape.RECTANGLE, Color.BLUE, 0.9);
    }

    /**
     * visit method for the Visitor-Pattern
     * @param uBlock the Unbreakable Block
     */
    @Override
    public void visit(UnbreakableBlock uBlock) {
        drawItemScale(uBlock, TanksImageProperty.uBlock, Shape.RECTANGLE, Color.BLUE, 0.9);
    }

    /**
     * visit method for the Visitor-Pattern
     * @param lightProjectile the lightProjectile
     */
    @Override
    public void visit(LightProjectile lightProjectile) {
        if (lightProjectile.visible()) {
            drawItem(lightProjectile, TanksImageProperty.lightBullet, Shape.OVAL, Color.GRAY);
        }
    }

    /**
     * visit method for the Visitor-Pattern
     * @param normalProjectile the normalProctile
     */
    @Override
    public void visit(NormalProjectile normalProjectile) {
        if (normalProjectile.visible()) {
            drawItem(normalProjectile, TanksImageProperty.normalBullet, Shape.OVAL, Color.GREEN);
        }
    }

    /**
     * visit method for the Visitor-Pattern
     * @param heavyProjectile the heavyProjectile
     */
    @Override
    public void visit(HeavyProjectile heavyProjectile) {
        drawItem(heavyProjectile, TanksImageProperty.heavyBullet, Shape.OVAL, Color.RED);
    }

    /**
     * visit method for the Visitor-Pattern
     * @param oil the Oil
     */
    @Override
    public void visit(Oil oil) {
        drawItemScale(oil, TanksImageProperty.oil, Shape.OVAL, Color.RED, 1);
    }

    /**
     * Draws an item as an image or, if the image is missing, as the specified shape.
     *
     * @param item  the item to be drawn
     * @param prop  the property specifying the image
     * @param shape the shape, which is drawn if the image is missing
     * @param color the color, which is used if the image is missing
     */
    private void drawItem(Item item, TanksImageProperty prop, Shape shape, Color color) {
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(item.getPos());
        context.translate(pos.x, pos.y);
        drawImage(prop, shape, color, 1);
        context.setTransform(ori);
    }

    /**
     * Draws an item as an image or, if the image is missing, as the specified shape and scales them.
     *
     * @param item  the item to be drawn
     * @param prop  the property specifying the image
     * @param shape the shape, which is drawn if the image is missing
     * @param color the color, which is used if the image is missing
     * @param scale the scale, which is used on the image
     */
    private void drawItemScale(Item item, TanksImageProperty prop, Shape shape, Color color, double scale) {
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(item.getPos());
        context.translate(pos.x, pos.y);
        context.scale(scale, scale);
        drawImage(prop, shape, color, 1);
        context.setTransform(ori);
    }

    /**
     * Draws an image if such an image has been configured.
     * Otherwise, a specified shape of the specified color is drawn
     *
     * @param prop  the string property indicating an image
     * @param shape the shape to be used when the image is missing
     * @param color the color that is used as a circle color if the image is missing
     */
    private void drawImage(TanksImageProperty prop, Shape shape, Color color, double scale) {
        final GraphicsContext context = view.getGraphicsContext2D();
        context.scale(1, 1);
        final Image img = view.getImages().getImage(prop);
        if (img != null)
            context.drawImage(img, -img.getWidth() * 0.5, -img.getHeight() * 0.5);
        else
            switch (shape) {
                case RECTANGLE:
                    context.setFill(color);
                    context.fillRect(-TanksMapView.HALF_FIELD_SIZE + 2,
                                     -TanksMapView.HALF_FIELD_SIZE + 2,
                                     TanksMapView.FIELD_SIZE - 4,
                                     TanksMapView.FIELD_SIZE - 4);
                    break;
                case DIRECTED_OVAL:
                    context.setFill(Color.BROWN);
                    context.fillRect(10, -10, 20, 20);
                case OVAL:
                    context.setFill(color);
                    context.fillOval(-TanksMapView.HALF_FIELD_SIZE + 2,
                                     -TanksMapView.HALF_FIELD_SIZE + 2,
                                     TanksMapView.FIELD_SIZE - 4,
                                     TanksMapView.FIELD_SIZE - 4);
                    break;
            }
    }
}
