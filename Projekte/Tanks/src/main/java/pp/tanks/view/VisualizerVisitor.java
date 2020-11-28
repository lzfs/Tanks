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
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.ReflectableBlock;
import pp.tanks.model.item.Turret;
import pp.tanks.model.item.UnbreakableBlock;
import pp.tanks.model.item.Visitor;
import pp.util.DoubleVec;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * Item visitor that adds visual representations of items to the tanks map view
 */
public class VisualizerVisitor implements Visitor {
    private enum Shape {RECTANGLE, OVAL, DIRECTED_OVAL}

    private final TanksMapView view;

    public VisualizerVisitor(TanksMapView view) {
        this.view = view;
    }

    @Override
    public void visit(PlayersTank playersTank) {

        Boolean destroyed = playersTank.isDestroyed();

        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(playersTank.getPos());
        context.translate(pos.x, pos.y);

        if (!destroyed) {
            //context.rotate(-90);
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
            //drawImage(TanksImageProperty.armor1, Shape.DIRECTED_OVAL, Color.GREEN);

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
            //drawImage(TanksImageProperty.turrettest,Shape.RECTANGLE,Color.GREEN);
        }
        else {
            drawImage(TanksImageProperty.tankDestroyed, Shape.DIRECTED_OVAL, Color.GREEN, 1);
        }
        context.setTransform(ori);
    }

    @Override
    public void visit(Enemy enemy) {

        /*
        drawItem(enemy, TanksImageProperty.armor2, Shape.RECTANGLE, Color.BLUE);
         */
        Boolean destroyed = enemy.isDestroyed();

        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(enemy.getPos());
        context.translate(pos.x, pos.y);

        if (!destroyed) {
            //context.rotate(-90);
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
            //drawImage(TanksImageProperty.armor1, Shape.DIRECTED_OVAL, Color.GREEN);

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
            //drawImage(TanksImageProperty.turrettest,Shape.RECTANGLE,Color.GREEN);
        }
        else {
            drawImage(TanksImageProperty.tankDestroyed, Shape.DIRECTED_OVAL, Color.GREEN, 1);
        }
        context.setTransform(ori);
    }

    @Override
    public void visit(COMEnemy comEnemy) {
        boolean destroyed = comEnemy.isDestroyed();

        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(comEnemy.getPos());
        context.translate(pos.x, pos.y);

        if (!destroyed) {
            //context.rotate(-90);
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
            //drawImage(TanksImageProperty.armor1, Shape.DIRECTED_OVAL, Color.GREEN);

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
            //drawImage(TanksImageProperty.turrettest,Shape.RECTANGLE,Color.GREEN);
        }
        else {
            drawImage(TanksImageProperty.tankDestroyed, Shape.DIRECTED_OVAL, Color.GREEN, 1);
        }
        context.setTransform(ori);
    }

    @Override
    public void visit(BreakableBlock bBlock) {
        if (!bBlock.isDestroyed()) {
            drawItemScale(bBlock, TanksImageProperty.bBlock, Shape.RECTANGLE, Color.BLUE, 0.9);
        }
    }

    @Override
    public void visit(ReflectableBlock rBlock) {
        drawItemScale(rBlock, TanksImageProperty.rBlock, Shape.RECTANGLE, Color.BLUE, 0.9);
    }

    @Override
    public void visit(UnbreakableBlock uBlock) {
        drawItemScale(uBlock, TanksImageProperty.uBlock, Shape.RECTANGLE, Color.BLUE, 0.9);
    }

    @Override
    public void visit(LightProjectile lightProjectile) {
        //bullets drehen
        //final GraphicsContext context = view.getGraphicsContext2D();
        //final Affine ori = context.getTransform();
        //final DoubleVec pos = view.modelToView(lightProjectile.getPos());
        //context.translate(pos.x, pos.y);
        //context.rotate(lightProjectile.getProjectileData().getDir().angle());
        if (lightProjectile.visible()) {
            drawItem(lightProjectile, TanksImageProperty.lightBullet, Shape.OVAL, Color.GRAY);
        }

        //context.setTransform(ori);
    }

    @Override
    public void visit(NormalProjectile normalProjectile) {
        if (normalProjectile.visible()) {
            drawItem(normalProjectile, TanksImageProperty.normalBullet, Shape.OVAL, Color.GREEN);
        }
    }

    @Override
    public void visit(HeavyProjectile heavyProjectile) {
        drawItem(heavyProjectile, TanksImageProperty.heavyBullet, Shape.OVAL, Color.RED);
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

    private void drawItemScale(Item item, TanksImageProperty prop, Shape shape, Color color, double scale) {
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(item.getPos());
        context.translate(pos.x, pos.y);
        context.scale(scale, scale);
        drawImage(prop, shape, color, 1);
        context.setTransform(ori);
    }

    /*
    // this method can probably be deleted
    private void drawItem(Item item, TanksImageProperty prop, Shape shape, Color color, double angle) {
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(item.getPos());
        context.translate(pos.x, pos.y);
        context.rotate(angle);
        drawImage(prop, shape, color);
        context.setTransform(ori);
    }
     */

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
