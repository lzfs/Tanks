package pp.tanks.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import pp.tanks.TanksImageProperty;
import pp.tanks.model.item.*;
import pp.util.DoubleVec;

public class VisualizerVisitor implements Visitor {

    private enum Shape {RECTANGLE, OVAL, DIRECTED_OVAL}

    private final TanksMapView view;

    public VisualizerVisitor(TanksMapView view) {
        this.view = view;
    }

    // TODO Save tank config and uses variable images
    //This class isnt finished yet

    @Override
    public void visit(PlayersTank playersTank) {

        //---------
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(playersTank.getPos());
        //System.out.println(pos.x + "  " + pos.y);
        context.translate(pos.x, pos.y);
        //context.rotate(-90);
        context.rotate((playersTank.getRotation()+90)%360);
        context.scale(0.75,0.75);
        drawImage(TanksImageProperty.armor1, Shape.DIRECTED_OVAL, Color.GREEN);
        context.rotate(-playersTank.getRotation());


        context.rotate(playersTank.getTurret().getDirection().angle());
        drawImage(TanksImageProperty.turrettest,Shape.RECTANGLE,Color.GREEN);

        context.setTransform(ori);
    }

    @Override
    public void visit(Enemy enemy) {
        drawItem(enemy, TanksImageProperty.armor2, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(COMEnemy comEnemy) {
        drawItem(comEnemy, TanksImageProperty.armor2, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(BreakableBlock bBlock) {
        drawItem(bBlock, TanksImageProperty.bBlock, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(ReflectableBlock rBlock) {
        drawItem(rBlock, TanksImageProperty.rBlock, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(UnbreakableBlock uBlock) {
        drawItem(uBlock, TanksImageProperty.uBlock, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(LightProjectile lightProjectile) {
        //bullets drehen
        //final GraphicsContext context = view.getGraphicsContext2D();
        //final Affine ori = context.getTransform();
        //final DoubleVec pos = view.modelToView(lightProjectile.getPos());
        //context.translate(pos.x, pos.y);
        //context.rotate(lightProjectile.getProjectileData().getDir().angle());
        drawItem(lightProjectile, TanksImageProperty.lightBullet, Shape.RECTANGLE, Color.BLUE);

        //context.setTransform(ori);
    }

    @Override
    public void visit(NormalProjectile normalProjectile) {
        drawItem(normalProjectile, TanksImageProperty.normalBullet, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(HeavyProjectile heavyProjectile) {
        drawItem(heavyProjectile, TanksImageProperty.heavyBullet, Shape.RECTANGLE, Color.BLUE);
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
        //System.out.println(item.getPos());
        context.translate(pos.x, pos.y);
        drawImage(prop, shape, color);
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
    private void drawImage(TanksImageProperty prop, Shape shape, Color color) {
        final GraphicsContext context = view.getGraphicsContext2D();
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
