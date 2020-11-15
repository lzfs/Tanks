package pp.tanks.model.item;

import pp.tanks.model.TanksMap;
import pp.tanks.model.item.navigation.Navigator;
import pp.util.IntVec;

import java.util.*;

class TanksNavigator extends Navigator<IntVec> {
    private static final double ROTATION_PENALTY = .5;
    private final Set<IntVec> obstacles = new HashSet<>();
    private final TanksMap map;

    /**
     * Creates an instance of this class to search an optimal path from the specified start to the target position
     *
     * @param map      the game map in which the optimal path is searched.
     * @param startPos the start position of the path
     * @param endPos   the requested target position of the path
     */
    TanksNavigator(TanksMap map, IntVec startPos, IntVec endPos) {
        super(startPos, endPos);
        this.map = map;
        for (Item obs : map.getBlocks())
            obstacles.add(obs.getPos().toIntVec());
        for (Item obs : map.getCOMTanks())
            obstacles.add(obs.getPos().toIntVec());
    }

    /**
     * Calculates the costs of a step from one position (pos) to a neighbor position (nextPos) if the previous
     * position was prevPos. The previous position is crucial if these three points do not lie on a straight line,
     * which requires a path bend. Path bend cause additional costs as the object needs time to turn.
     *
     * @param prevPos the position of the path previous to pos. May be null.
     * @param pos     the position at the beginning of the step
     * @param nextPos the position after the step
     * @return the costs of the step
     */
    @Override
    protected double addedCosts(IntVec prevPos, IntVec pos, IntVec nextPos) {
        final IntVec dir = nextPos.sub(pos);
        final IntVec prevDir = prevPos == null ? IntVec.NULL : pos.sub(prevPos);
        if (prevDir.equals(dir))
            return dir.length();
        else
            return dir.length() + ROTATION_PENALTY;
    }

    /**
     * Computes the list of all neighbors of the specified position reachable in one step.
     *
     * @param pos some position in the game map
     * @return the list of all positions of the game m ap reachable in one step from pos.
     */
    @Override
    protected Collection<IntVec> neighbors(IntVec pos) {
        List<IntVec> neighbors = new ArrayList<>(8);
        for (int x = Math.max(pos.x - 1, 0); x < Math.min(pos.x + 2, map.getWidth()); x++)
            for (int y = Math.max(pos.y - 1, 0); y < Math.min(pos.y + 2, map.getHeight()); y++)
                if ((x != pos.x || y != pos.y) && accessibleFrom(x, y, pos))
                    neighbors.add(new IntVec(x, y));
        return neighbors;
    }

    /**
     * Checks whether the position (x,y) can be reached from the specified position pos where (x,y) is guaranteed
     * to be a neighbor of pos. This method takes into account that one must not make a diagonal move across a corner
     * of a blocked field, i.e., a field with an obstacle.
     *
     * @param x   the x coordinate of the grid
     * @param y   the y coordinate of the gird
     * @param pos the Position you go to
     * @return true or false
     */
    private boolean accessibleFrom(int x, int y, IntVec pos) {
        if (blocked(x, y)) return false;
        if (pos.x == x || pos.y == y) return true;
        // this is a diagonal move => don't cut corners with obstacles
        return !blocked(x, pos.y) && !blocked(pos.x, y);
    }

    /**
     * Returns whether the specified position is blocked by an obstacle
     *
     * @param x the x value
     * @param y the y value
     * @return true or false
     */
    private boolean blocked(int x, int y) {
        return obstacles.contains(new IntVec(x, y));
    }
}
