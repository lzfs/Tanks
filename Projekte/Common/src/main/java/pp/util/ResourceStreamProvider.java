package pp.util;

import java.io.InputStream;

/**
 * Interface for objects that allow to load resources as a stream.
 */
public interface ResourceStreamProvider {
    /**
     * Returns the stream of the specified resource.
     *
     * @param name the path of the resource
     * @return the stream of the specified resource, or null if the resource cannot be located.
     */
    InputStream getResourceAsStream(String name);
}
