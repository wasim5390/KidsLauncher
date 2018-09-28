package com.uiu.kids.model.response;


import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.Invitation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InvitationResponse extends BaseResponse implements Serializable{

    @SerializedName("invitations")
    private List<Invitation> invitationList;

    @SerializedName("invitation")
    private Invitation invitation;

    public List<Invitation> getInvitationList() {
        return invitationList==null?new ArrayList<>():invitationList;
    }

    public void setInvitationList(List<Invitation> mList) {
        this.invitationList = mList;
    }

    public Invitation getInvitation() {
        return invitation;
    }
}
