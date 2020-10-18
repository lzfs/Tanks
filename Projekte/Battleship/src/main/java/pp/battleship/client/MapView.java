package pp.battleship.client;

import pp.battleship.model.Battleship;
import pp.battleship.model.ShipMap;
import pp.battleship.model.Shot;
import pp.util.DoubleVec;
import pp.util.IntVec;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * View for the map. This view is used for the structure of ownMap, harbor and opponentMap.
 */
class MapView {
    private static final Logger LOGGER = Logger.getLogger(MapView.class.getName());
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color GRID_COLOR = Color.GREEN;
    private static final Color HIT_COLOR = Color.RED;
    private static final Color MISS_COLOR = Color.LIGHTSTEELBLUE;
    private static final Color SHIP_BORDER_COLOR = Color.WHITE;
    private static final Color PREVIEW_COLOR = Color.GRAY;
    private static final Color SPOTTED_COLOR = Color.ORANGE;
    private static final double FIELD_SIZE = 20;

    private final BattleshipImages images = new BattleshipImages();

    private final Canvas canvas;
    private final int width;
    private final int height;
    private ShipMap map;
    private boolean grid;

    private ArrayList<Shot> shooted = new ArrayList<>();

    /**
     * Creates a MapView
     *
     * @param canvas the canvas on which to draw
     * @param width  the width of the map
     * @param height the height of the map
     */
    MapView(Canvas canvas, int width, int height) {
        this.canvas = canvas;
        this.width = width;
        this.height = height;
        canvas.setWidth(width * FIELD_SIZE + 2);
        canvas.setHeight(height * FIELD_SIZE + 2);
    }

    /**
     * Returns the width of the map.
     *
     * @return width of the map
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the map.
     *
     * @return height of the map
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns whether the grid lines are displayed or not.
     *
     * @return whether the grid lines are displayed or not
     */
    public boolean showGrid() {
        return grid;
    }

    /**
     * Sets whether the grid lines are displayed or not.
     *
     * @param grid whether the grid lines should be displayed or not
     */
    public void setShowGrid(boolean grid) {
        this.grid = grid;
    }

    /**
     * Converts coordinates from view coordinates to model coordinates.
     *
     * @param x x value
     * @param y y value
     * @return model coordinates
     */
    public IntVec viewToModel(double x, double y) {
        return new IntVec((int) ((x - 1) / FIELD_SIZE), (int) ((y - 1) / FIELD_SIZE));
    }

    /**
     * Converts coordinates from model coordinates to view coordinates.
     *
     * @param x x value
     * @param y y value
     * @return view coordinates
     */
    public DoubleVec modelToView(int x, int y) {
        return new DoubleVec(x * FIELD_SIZE + 1, y * FIELD_SIZE + 1);
    }

    /**
     * Returns the current ShipMap
     *
     * @return the ShipMap
     */
    public ShipMap getMap() {
        return map;
    }

    /**
     * Sets a ShipMap
     *
     * @param map the ShipMap to be set
     */
    public void setMap(ShipMap map) {
        if (map.getWidth() == width && map.getHeight() == height)
            this.map = map;
        else
            LOGGER.severe("Received map has wrong dimensions"); //NON-NLS
    }

    /**
     * Updates the displayed content
     */
    public void update() {
        final GraphicsContext context = canvas.getGraphicsContext2D();

        // delete background
        context.setFill(BACKGROUND_COLOR);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw grid
        context.setLineWidth(1.);
        if (grid) {
            context.setStroke(GRID_COLOR);
            for (int x = 0; x <= width; x++) {
                DoubleVec f = modelToView(x, 0);
                DoubleVec t = modelToView(x, height);
                context.strokeLine(f.x, f.y, t.x, t.y);
            }
            for (int y = 0; y <= height; y++) {
                DoubleVec f = modelToView(0, y);
                DoubleVec t = modelToView(width, y);
                context.strokeLine(f.x, f.y, t.x, t.y);
            }
        }
        if (map != null) {
            for (Shot shot : map.getShots()) {
                DoubleVec p = modelToView(shot.x, shot.y);
                context.setFill(shot.hit ? HIT_COLOR : MISS_COLOR);
                context.fillRect(p.x + 1, p.y + 1, FIELD_SIZE - 2, FIELD_SIZE - 2);

                if(shot.hit){
                    shooted.add(shot);
                }
            }
            context.setStroke(SHIP_BORDER_COLOR);
            context.setLineWidth(2.);
            for (Battleship ship : map.getShips())
                drawShip(context, ship.getAllParts());

            for(Shot shot:shooted){
                DoubleVec p = modelToView(shot.x, shot.y);
                context.drawImage(images.getImage(StringProperty.imghit), p.x, p.y, 20, 20);
            }
            shooted=new ArrayList<>();

            context.setStroke(PREVIEW_COLOR);
            if (map.getPreview() != null)
                drawShip(context, map.getPreview().getAllParts());
        }
    }

    /**
     * Draws a ship
     *
     * @param context where to draw
     * @param parts   all parts which determine the position of the ship
     */
    private void drawShip(GraphicsContext context, List<IntVec> parts) {
        IntVec first = parts.get(0);
        IntVec last = parts.get(parts.size() - 1);
        int xMin = Math.min(first.x, last.x);
        int xMax = Math.max(first.x, last.x);
        int yMin = Math.min(first.y, last.y);
        int yMax = Math.max(first.y, last.y);
        DoubleVec p1 = modelToView(xMin, yMin);
        DoubleVec p2 = modelToView(xMax + 1, yMax + 1);
        //context.strokeRect(p1.x + 2, p1.y + 2,
        //                   p2.x - p1.x - 4, p2.y - p1.y - 4);
        context.save();
        //rotate
        if (xMax - xMin == 0) {
            context.translate(p1.x +20, p1.y);
            context.rotate(90);
        }
        else {
            context.translate(p1.x, p1.y);
        }

        if (parts.size()==1) {
            context.drawImage(images.getImage(StringProperty.imgship1), 0, 0, 20, 20);
        }
        else if (parts.size()==2) {
            context.drawImage(images.getImage(StringProperty.imgship2), 0, 0, 40, 20);
        }
        else if (parts.size()==3) {
            context.drawImage(images.getImage(StringProperty.imgship3), 0, 0, 60, 20);
        }
        else if (parts.size()==4) {
            context.drawImage(images.getImage(StringProperty.imgship4), 0, 0, 80, 20);
        }

        //restore
        context.restore();
    }
}