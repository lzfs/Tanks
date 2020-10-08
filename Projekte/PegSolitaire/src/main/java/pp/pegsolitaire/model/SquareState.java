package pp.pegsolitaire.model;

/**
 * States of each square of the game model, i.e., cross. Each state has a simple one-character symbol,
 * which is used for a string representation of a cross. And each state further
 * has a method used to dispatch click events.
 * <p>
 * Note that Java enums are just syntactic sugar for implementing
 * <a href="http://www.javapractices.com/topic/TopicAction.do?Id=1">type-safe enumerations</a>.
 * And providing a new method with a value is nothing else than using an anonymous
 * sub-class.
 */
public enum SquareState {
    /**
     * Indicates an inaccessible square, i.e., outside of the cross.
     */
    Inaccessible("-"),
    /**
     * Indicates an empty square, i.e., a square without a knight.
     */
    Empty(" "),
    /**
     * Indicates an empty square, i.e., a square without a knight, that has been
     * marked as reachable by some knight.
     */
    EmptyReachable("0") {
        /**
         * When a clicked square is empty and marked as reachable, this is the target
         * square of the knight selected previously. This knight is then moved to this
         * square.
         */
        @Override
        public void handleClickEvent(Cross cross, Square clickedSquare) {
            cross.moveSelectedKnightTo(clickedSquare);
        }
    },
    /**
     * Indicates a square with a knight.
     */
    Occupied("#") {
        /**
         * When an occupied square is clicked, its knight is selected and
         * the (empty) squares that can be reached by this knight are marked as reachable.
         */
        @Override
        public void handleClickEvent(Cross cross, Square clickedSquare) {
            cross.setSelectedSquare(clickedSquare);
        }
    };

    private final String symbol;

    /**
     * Creates a new state
     *
     * @param symbol the string symbol used for representing a square in this state in string
     *               representations of game models
     */
    SquareState(String symbol) {
        assert (symbol.length() == 1);
        this.symbol = symbol;
    }

    /**
     * Returns the string symbol used for representing a square in this state in string
     * representations of game models
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Handles a mouse click in the specified square of the specified game model. This method does nothing by
     * default, but can be overridden by concreted subclasses, i.e., for individual enum values.
     *
     * @param cross         the game model that contains clickedSquare
     * @param clickedSquare the square that has been clicked.
     */
    public void handleClickEvent(Cross cross, Square clickedSquare) {
        // do nothing by default
    }
}
