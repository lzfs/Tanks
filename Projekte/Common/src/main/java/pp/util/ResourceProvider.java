package pp.util;

import java.net.URL;

/**
 * Interface for objects that allow to load resources by their URL.
 */
public interface ResourceProvider {
    /**
     * Returns the URL of the specified resource.
     *
     * @param name the path of the resource
     * @return the URL of the specified resource, or null if the resource cannot be located.
     */
    URL getResource(String name);
}
