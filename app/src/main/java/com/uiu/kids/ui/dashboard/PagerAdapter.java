package com.uiu.kids.ui.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.uiu.kids.Injection;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.model.Slide;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.HomeFragment;
import com.uiu.kids.ui.home.HomePresenter;
import com.uiu.kids.ui.invitation.InviteListFragment;
import com.uiu.kids.ui.invitation.InviteListPresenter;
import com.uiu.kids.ui.slides.apps.FavoriteAppFragment;
import com.uiu.kids.ui.slides.apps.FavoriteAppsPresenter;
import com.uiu.kids.ui.slides.links.FavoriteLinksFragment;
import com.uiu.kids.ui.slides.links.FavoriteLinksPresenter;
import com.uiu.kids.ui.slides.people.FavoritePeopleFragment;
import com.uiu.kids.ui.slides.people.FavoritePeoplePresenter;
import com.uiu.kids.ui.slides.reminder.ReminderFragment;
import com.uiu.kids.ui.slides.reminder.ReminderPresenter;
import com.uiu.kids.ui.slides.sos.SOSFragment;
import com.uiu.kids.ui.slides.sos.SOSPresenter;
import com.uiu.kids.util.PreferenceUtil;

import java.util.List;

import static com.uiu.kids.Constant.SLIDE_INDEX_FAV_APP;
import static com.uiu.kids.Constant.SLIDE_INDEX_FAV_GAMES;
import static com.uiu.kids.Constant.SLIDE_INDEX_FAV_LINKS;
import static com.uiu.kids.Constant.SLIDE_INDEX_FAV_PEOPLE;
import static com.uiu.kids.Constant.SLIDE_INDEX_HOME;
import static com.uiu.kids.Constant.SLIDE_INDEX_INVITE;
import static com.uiu.kids.Constant.SLIDE_INDEX_REMINDERS;
import static com.uiu.kids.Constant.SLIDE_INDEX_SOS;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<Slide> list;
    private Repository repository;
    private PreferenceUtil preferenceUtil;

    public PagerAdapter(FragmentManager fm, List<Slide> fragments,Repository repository, PreferenceUtil preferenceUtil) {
        super(fm);
        this.list = fragments;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        notifyDataSetChanged();
    }

    public void setSlides(List<Slide> fragmets){
        this.list.clear();
        notifyDataSetChanged();
        this.list=fragmets;
        notifyDataSetChanged();
    }


   /* public Object instantiateItem(ViewGroup container, int position)
    {
        Object ret = super.instantiateItem(container, position);
        list.remove(position);
        list.add(position,(Fragment)ret);
        return ret;
    }*/

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        Slide fragment = list.get(position);
        return createFragmentsFromSlide(fragment);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private Fragment createFragmentsFromSlide(Slide slideItem){
        if(slideItem!=null)
            switch (slideItem.getType()){
                case SLIDE_INDEX_INVITE:
                    InviteListFragment inviteListFragment = InviteListFragment.newInstance();
                    new InviteListPresenter(inviteListFragment, repository,preferenceUtil.getAccount());
                    return inviteListFragment;

                case SLIDE_INDEX_HOME:
                    HomeFragment homeFragment = HomeFragment.newInstance();
                    new HomePresenter(homeFragment, repository);
                    return homeFragment;

                case SLIDE_INDEX_FAV_PEOPLE:
                    FavoritePeopleFragment favoritePeopleFragment = FavoritePeopleFragment.newInstance();
                    new FavoritePeoplePresenter(favoritePeopleFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    return favoritePeopleFragment;

                case SLIDE_INDEX_FAV_APP:
                    FavoriteAppFragment appsFragment = FavoriteAppFragment.newInstance();
                    new FavoriteAppsPresenter(appsFragment, slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    return appsFragment;

                case SLIDE_INDEX_FAV_LINKS:
                    FavoriteLinksFragment linksFragment = FavoriteLinksFragment.newInstance();
                    new FavoriteLinksPresenter(linksFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    return linksFragment;

                case SLIDE_INDEX_SOS:
                    SOSFragment sosFragment = SOSFragment.newInstance();
                    new SOSPresenter(sosFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    return sosFragment;
                case SLIDE_INDEX_REMINDERS:
                    ReminderFragment reminderFragment = ReminderFragment.newInstance();
                    new ReminderPresenter(reminderFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    return reminderFragment;

            }
        return null;
    }

}
