package pp.pegsolitaire.model;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The main class of the game model representing the entire map
 */
public class Cross {
    /**
     * The height and width of the game map, i.e., the number of rows and columns of the game map.
     */
    public final static int SQUARE_XY_COUNT = 9;
    private final static int THIRD = SQUARE_XY_COUNT / 3;
    private final static int HALF = SQUARE_XY_COUNT / 2;

    private final static Logger LOGGER = Logger.getLogger(Cross.class.getName());

    private Square selectedSquare;
    private final Square[][] squares = new Square[SQUARE_XY_COUNT][SQUARE_XY_COUNT];

    /**
     * Creates a new game map
     */
    public Cross() {
        for (int x = 0; x < SQUARE_XY_COUNT; x++) {
            for (int y = 0; y < SQUARE_XY_COUNT; y++) {
                SquareState currentState;
                // initial position of knights
                if (x == HALF && y == HALF)
                    // empty field in the middle
                    currentState = SquareState.Empty;
                else if (x / THIRD == 1 || y / THIRD == 1)
                    // occupied fields
                    currentState = SquareState.Occupied;
                else
                    currentState = SquareState.Inaccessible;
                squares[x][y] = new Square(x, y, currentState);
            }
        }
        LOGGER.info("Cross initialized");
    }

    /**
     * Returns all squares where a knight can be moved to.
     */
    public Set<Square> reachableSquares() {
        final Set<Square> collect = new HashSet<>();
        for (int x = 0; x < SQUARE_XY_COUNT; x++)
            for (int y = 0; y < SQUARE_XY_COUNT; y++)
                if (squares[x][y].isEmpty()) {
                    addReachable(x, y, 1, 0, collect);
                    addReachable(x, y, -1, 0, collect);
                    addReachable(x, y, 0, 1, collect);
                    addReachable(x, y, 0, -1, collect);
                }
        return collect;
    }

    /**
     * Adds a specified square to the specified set if the square can be reached
     * by any knight in the specified direction.
     *
     * @param x   the x coordinate of the square
     * @param y   the y coordinate of the square
     * @param dx  the x coordinate of the direction (must be -1, 0, or 1)
     * @param dy  the y coordinate of the direction (must be -1, 0, or 1)
     * @param set set of reachable squares
     */
    private void addReachable(int x, int y, int dx, int dy, Set<Square> set) {
        final int x1 = x + dx;
        final int y1 = y + dy;
        final int x2 = x1 + dx;
        final int y2 = y1 + dy;
        if (withinBorders(x2, y2) && squares[x1][y1].isOccupied() && squares[x2][y2].isOccupied())
            set.add(squares[x][y]);
    }

    /**
     * Selects the knight on the specified square, if there is a knight.
     */
    public void setSelectedSquare(Square square) {
        if (square.isOccupied()) {
            clearReachableFields();
            if (selectedSquare != null)
                selectedSquare.setSelected(false);
            selectedSquare = square;
            selectedSquare.setSelected(true);
            markReachableFields();
        }
    }

    /**
     * Moves the selected knight - if a knight had been selected - to the specified
     * square and removes the knight in the middle if this is a valid move.
     */
    public void moveSelectedKnightTo(Square square) {
        if (selectedSquare != null) {
            final int dx = Math.abs(square.getX() - selectedSquare.getX());
            final int dy = Math.abs(square.getY() - selectedSquare.getY());
            final int x = (square.getX() + selectedSquare.getX()) / 2;
            final int y = (square.getY() + selectedSquare.getY()) / 2;
            if (dx + dy == 2 && (dx == 0 || dx == 2) && squares[x][y].isOccupied()) {
                squares[x][y].setState(SquareState.Empty);
                selectedSquare.setState(SquareState.Empty);
                square.setState(SquareState.Occupied);
                selectedSquare.setSelected(false);
                selectedSquare = null;
                clearReachableFields();
            }
        }
    }

    /**
     * Removes all reachable-field-flags
     */
    private void clearReachableFields() {
        for (Square[] row : squares)
            for (Square square : row)
                if (square.isEmptyReachable())
                    square.setState(SquareState.Empty);
    }

    /**
     * Marks all reachable fields
     */
    private void markReachableFields() {
        markReachable(0, 1);
        markReachable(0, -1);
        markReachable(1, 0);
        markReachable(-1, 0);
    }

    /**
     * Marks a square as reachable if is reachable from the currently selected one in the specified direction
     *
     * @param dx the x coordinate of the direction (must be -1, 0, or 1)
     * @param dy the y coordinate of the direction (must be -1, 0, or 1)
     */
    private void markReachable(int dx, int dy) {
        final int x1 = selectedSquare.getX() + dx;
        final int y1 = selectedSquare.getY() + dy;
        final int x2 = x1 + dx;
        final int y2 = y1 + dy;
        if (withinBorders(x2, y2) && squares[x2][y2].isEmpty() && squares[x1][y1].isOccupied())
            squares[x2][y2].setState(SquareState.EmptyReachable);
    }

    /**
     * Returns whether the specified square is within the borders of the game
     *
     * @param x the x coordinate of the specified square
     * @param y the y-coordinate  of the specified square
     */
    private boolean withinBorders(int x, int y) {
        return x >= 0 && x < SQUARE_XY_COUNT && y >= 0 && y < SQUARE_XY_COUNT;
    }

    /**
     * Returns the  square with the specified coordinates.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return the square with the specified coordinates.
     */
    public Square getSquare(int x, int y) {
        return squares[x][y];
    }

    /**
     * Returns the number of occupied squares
     */
    public int numOccupiedFields() {
        int counter = 0;
        for (Square[] row : squares)
            for (Square square : row)
                if (square.isOccupied())
                    counter++;
        return counter;
    }

    /**
     * Returns a string representation of this game model.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int y = 0; y < SQUARE_XY_COUNT; y++) {
            for (int x = 0; x < SQUARE_XY_COUNT; x++) {
                final Square square = squares[x][y];
                sb.append(square == selectedSquare ? "X" : square);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
