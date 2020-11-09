package pp.tanks.model.item.navigation;

import pp.util.WithDistance;

import java.util.*;
import java.util.logging.Logger;

/**
 * Base class for searching an optimal path between two nodes in a graph using the A* algorithm.
 * <p>
 * Implement at least {@linkplain #neighbors(WithDistance)} and
 * {@linkplain #addedCosts(WithDistance, WithDistance, WithDistance)} in a concrete subclass.
 *
 * @param <N> the type of each node in the graph. It must at least implement {@linkplain WithDistance}
 */
public abstract class Navigator<N extends WithDistance<N>> {
    private static final Logger LOGGER = Logger.getLogger(Navigator.class.getName());
    private final Map<N, PathNode<N>> nodes = new HashMap<>();
    private final Queue<PathNode<N>> openQueue = new PriorityQueue<>();
    private final Set<PathNode<N>> closedSet = new HashSet<>();
    protected final N startNode;
    protected final N endNode;

    protected Navigator(N startNode, N endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    /**
     * Computes all nodes that are directly reachable from the specified one
     */
    protected abstract Collection<N> neighbors(N node);

    /**
     * Computes the costs when moving from node to nextNode in one step where the path has prevNode, node, and nextNode as
     * its last nodes. Note the nextNode and node must not be null, but prevNode may be null if node
     * is the starting node ({@linkplain #startNode}) of the path.
     *
     * @param prevNode the node of the path previous to node. May be null.
     * @param node     the node at the beginning of the step
     * @param nextNode the node after the step
     * @return the additional costs, usually computed using the distance from node to nextNode and the additional costs
     * needed for turning.
     */
    protected abstract double addedCosts(N prevNode, N node, N nextNode);

    /**
     * Computes a lower bound of the costs of any path from the specified node to the end node.
     * The default implementation uses the Euclidean distance to {@linkplain #endNode}.
     */
    protected double estimateCostToEnd(N node) {
        return endNode.distance(node);
    }

    /**
     * Computes a path between startNode and endNode using the A* algorithm
     */
    public List<N> findPath() {
        LOGGER.fine(() -> "find path from " + startNode + " to " + endNode);
        if (startNode.equals(endNode))
            return Collections.emptyList();
        nodes.clear();
        openQueue.clear();
        closedSet.clear();

        // priority queue, start node initially in openQueue
        addToOpenQueue(startNode, null);

        while (!openQueue.isEmpty()) {
            PathNode<N> curPathNode = openQueue.poll();
            closedSet.add(curPathNode);
            LOGGER.fine(() -> "openQueue: " + openQueue);
            LOGGER.fine(() -> "curPathNode = " + curPathNode);

            // path found?
            if (curPathNode.getNode().equals(endNode))
                return buildPath(curPathNode);

            // get the neighboring nodes
            // loop through all the neighboring nodes
            for (N nextNode : neighbors(curPathNode.getNode())) {
                final PathNode<N> nextPathNode = nodes.get(nextNode);
                if (nextPathNode == null)
                    addToOpenQueue(nextNode, curPathNode);
                else if (!closedSet.contains(nextPathNode)) {
                    final N prevNode = curPathNode.getPrevNode() == null ? null : curPathNode.getPrevNode().getNode();
                    final double costForThisPath =
                            curPathNode.getCostFromStart() + addedCosts(prevNode, curPathNode.getNode(), nextNode);
                    if (costForThisPath < nextPathNode.getCostFromStart()) {
                        LOGGER.fine(() -> "shorter path to " + nextPathNode + " via " + curPathNode);
                        addToOpenQueue(nextNode, curPathNode);
                    }
                }
            }
        }

        // no path to end found
        return null;
    }

    /**
     * Adds a node to the open list
     *
     * @param node     current node
     * @param prevNode previous plan node
     */
    private void addToOpenQueue(N node, PathNode<N> prevNode) {
        PathNode<N> pathNode = nodes.get(node);
        if (pathNode == null) {
            pathNode = new PathNode<>(node, prevNode, this);
            nodes.put(node, pathNode);
        }
        else {
            openQueue.remove(pathNode);
            pathNode.setPrevNode(prevNode);
        }
        openQueue.add(pathNode);
    }

    /**
     * Builds the path from end to start
     *
     * @param endNode end node
     * @return path
     */
    private List<N> buildPath(PathNode<N> endNode) {
        List<N> plan = new LinkedList<>();
        for (PathNode<N> n = endNode; n != null; n = n.getPrevNode())
            plan.add(0, n.getNode());
        return plan;
    }
}
