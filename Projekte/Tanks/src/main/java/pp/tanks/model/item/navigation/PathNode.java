package pp.tanks.model.item.navigation;

import pp.util.WithDistance;

/**
 *
 * @param <N> the type of each node in the graph
 */
class PathNode<N extends WithDistance<N>> implements Comparable<PathNode<N>> {
    private final N node; // node in the graph
    private final double costToEnd; // lower bound of the distance for the remaining path
    private final Navigator<N> navigator;
    private PathNode<N> prevNode; // previous path node
    private double costFromStart; // distance from start to current node in the graph

    /**
     * Creates a path plan node
     *
     * @param node      node in the graph
     * @param prevNode  previous path node
     * @param navigator the navigator this node belongs to
     */
    public PathNode(N node, PathNode<N> prevNode, Navigator<N> navigator) {
        this.node = node;
        this.navigator = navigator;
        costToEnd = navigator.estimateCostToEnd(node);
        setPrevNode(prevNode);
    }

    /**
     * Sets the previous path plan node
     */
    public void setPrevNode(PathNode<N> prevNode) {
        this.prevNode = prevNode;
        if (prevNode == null)
            costFromStart = 0;
        else {
            final N ppNode = prevNode.getPrevNode() == null ? null : prevNode.getPrevNode().getNode();
            final double added = navigator.addedCosts(ppNode, prevNode.getNode(), node);
            costFromStart = prevNode.getCostFromStart() + added;
        }
    }

    /**
     * Returns the previous path plan node
     *
     * @return path plan node
     */
    public PathNode<N> getPrevNode() {
        return prevNode;
    }

    /**
     * Returns the associated node
     *
     * @return associated node
     */
    public N getNode() {
        return node;
    }

    /**
     * Returns the distance from the start to the current node
     *
     * @return distance from start to current node
     */
    public double getCostFromStart() {
        return costFromStart;
    }

    /**
     * Returns the estimated overall distance
     *
     * @return overall distance
     */
    private double getOverallCost() {
        return costFromStart + costToEnd;
    }

    /**
     * Compares two path plan nodes according to their overall distances
     */
    @Override
    public int compareTo(PathNode<N> other) {
        return Double.compare(this.getOverallCost(), other.getOverallCost());
    }

    @Override
    public String toString() {
        if (prevNode == null)
            return node + "<-null";
        else
            return node + "<-" + prevNode.getNode();
    }
}
