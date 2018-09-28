package com.uiu.kids.ui.invitation;


import com.uiu.kids.model.Invitation;

public interface InvitationConfirmationCallback {
    void onAcceptInvitation(Invitation invitation);
    void onRejectInvitation(Invitation invitation);
    void onDeleteInvitation(Invitation invitation);
}
