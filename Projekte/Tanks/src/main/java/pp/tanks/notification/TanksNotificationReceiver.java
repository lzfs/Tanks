package pp.tanks.notification;

/**
 * Interface for any subscriber interested in notifications of events occurring in the game model.
 */
public interface TanksNotificationReceiver {

    /**
     * method to notify the observers
     * @param notification
     */
    void notify(TanksNotification notification);
}