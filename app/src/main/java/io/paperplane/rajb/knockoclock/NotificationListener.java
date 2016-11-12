package io.paperplane.rajb.knockoclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by raj on 11/11/16.
 */
public class NotificationListener extends NotificationListenerService {

    public NotificationListener() {
        super();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        Log.d("DEBUG", "called");
        return super.getActiveNotifications();
    }


    class NLServiceReceiver {

        public StatusBarNotification[] returnList() {
            return NotificationListener.this.getActiveNotifications();
        }

    }

}

