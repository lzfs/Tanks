package pp.tanks.notification;

/**
 * Enumeration of all events fired by the game model that might be visualized.
 */
public enum TanksNotification {
    /**
     * Indicates that the game map has changed
     */
    MAP_UPDATE,
    /**
     * Indicates that a tank has fired
     */
    TANK_FIRED,
    /**
     * Indicates that a tank has been destroyed
     */
    TANK_DESTROYED,
    /**
     * Indicates that a block has been destroyed
     */
    BLOCK_DESTROYED,


    ARMOR_HIT,


}
