package pp.tanks.notification;

/**
 * Interface for any subscriber interested in notifications of events occurring in the game model.
 */
public interface TanksNotificationReceiver {
    void notify(TanksNotification notification);
}