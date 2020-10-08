package pp.droids.notifications;

/**
 * Enumeration of all events fired by the game model that might be visualized. Use
 * method {@linkplain pp.droids.model.DroidsGameModel#notifyReceivers(DroidsNotification)} to notify
 * any registered {@linkplain pp.droids.notifications.DroidsNotificationReceiver}.
 */
public enum DroidsNotification {
    /**
     * Indicates that the game map has changed
     */
    MAP_UPDATE,
    /**
     * Indicates that the droid has fired
     */
    DROID_FIRED,
    /**
     * Indicates that the droid has been destroyed
     */
    DROID_DESTROYED,
    /**
     * Indicates that an enemy has been destroyed
     */
    ENEMY_DESTROYED,
    /**
     * Indicates that a rocket has started
     */
    ROCKET_STARTS
}
