package pp.util;

/**
 * Interface of all classes that implement a distance metrics.
 *
 * @param <P> the type of the implementing class
 */
public interface WithDistance<P> {
    /**
     * Returns the distance of this object to the specified one.
     */
    double distance(P other);
}
