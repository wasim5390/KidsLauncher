package com.uiu.kids.ui.invitation;



import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.Invitation;

import java.util.List;

public class InviteContract {

   public interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onInvitesLoaded(List<Invitation> list);
    }

   public interface Presenter extends BasePresenter {
        void getInvites();
        void disconnect(Invitation invitation);
        void updateInvitation(Invitation invitation);
    }
}
