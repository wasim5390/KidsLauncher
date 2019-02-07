package com.uiu.kids.event.notification;

import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.NotificationSender;
import com.uiu.kids.ui.home.apps.AppsEntity;

public class InviteNotificationEvent {
    Invitation invitation;
    NotificationSender sender;

    public InviteNotificationEvent(Invitation invitation, NotificationSender sender) {
        this.invitation = invitation;
        this.sender = sender;
    }

    public Invitation getInvitation() {
        return invitation;
    }


    public NotificationSender getSender() {
        return sender;
    }
}
