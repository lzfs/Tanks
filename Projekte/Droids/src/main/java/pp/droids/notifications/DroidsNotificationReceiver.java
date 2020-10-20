package pp.droids.notifications;

/**
 * Interface for any subscriber interested in notifications of events occurring in the game model.
 *
 * @see pp.droids.notifications.DroidsNotification
 */
public interface DroidsNotificationReceiver {
    void notify(DroidsNotification notification);
}
