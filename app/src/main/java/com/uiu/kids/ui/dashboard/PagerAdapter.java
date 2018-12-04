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
import com.uiu.kids.ui.slides.clock.FragmentClock;
import com.uiu.kids.ui.slides.links.FavoriteLinksFragment;
import com.uiu.kids.ui.slides.links.FavoriteLinksPresenter;
import com.uiu.kids.ui.slides.people.FavoritePeopleFragment;
import com.uiu.kids.ui.slides.people.FavoritePeoplePresenter;
import com.uiu.kids.ui.slides.reminder.ReminderFragment;
import com.uiu.kids.ui.slides.reminder.ReminderPresenter;
import com.uiu.kids.ui.slides.sos.SOSFragment;
import com.uiu.kids.ui.slides.sos.SOSPresenter;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import static com.uiu.kids.Constant.SLIDE_INDEX_CLOCK;
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

    public PagerAdapter(FragmentManager fm,List<Slide> slides,Repository repository, PreferenceUtil preferenceUtil) {
        super(fm);
        this.list = slides;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        notifyDataSetChanged();
    }

    public void setSlides(List<Slide> fragmets){
        this.list.clear();
        notifyDataSetChanged();
        this.list.addAll(fragmets);
        notifyDataSetChanged();
    }


    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Slide fragment = list.get(position);
        return createFragmentsFromSlide(fragment);
    }

    public int getSlideIndex(int slideType){
        for(int i=list.size()-1;i>=0;i--){
            if(list.get(i).getType()==slideType)
                return i;
        }
        return 0;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private Fragment createFragmentsFromSlide(Slide slideItem){
        if(slideItem!=null)
            switch (slideItem.getType()){
                case SLIDE_INDEX_CLOCK:
                    FragmentClock fragmentClock = FragmentClock.newInstance();
                    return fragmentClock;

                case SLIDE_INDEX_INVITE:
                    InviteListFragment inviteListFragment = InviteListFragment.newInstance();
                    new InviteListPresenter(inviteListFragment, preferenceUtil,repository,preferenceUtil.getAccount());
                    return inviteListFragment;

                case SLIDE_INDEX_HOME:
                    HomeFragment homeFragment = HomeFragment.newInstance();
                    new HomePresenter(homeFragment,preferenceUtil, repository);
                    return homeFragment;

                case SLIDE_INDEX_FAV_PEOPLE:
                    FavoritePeopleFragment favoritePeopleFragment = FavoritePeopleFragment.newInstance();
                    new FavoritePeoplePresenter(favoritePeopleFragment,slideItem, preferenceUtil, repository);
                    return favoritePeopleFragment;

                case SLIDE_INDEX_FAV_APP:
                    FavoriteAppFragment appsFragment = FavoriteAppFragment.newInstance(slideItem);
                    new FavoriteAppsPresenter(appsFragment, slideItem, preferenceUtil, repository);
                    return appsFragment;

                case SLIDE_INDEX_FAV_LINKS:
                    FavoriteLinksFragment linksFragment = FavoriteLinksFragment.newInstance();
                    new FavoriteLinksPresenter(linksFragment,slideItem, preferenceUtil, repository);
                    return linksFragment;

                case SLIDE_INDEX_SOS:
                    SOSFragment sosFragment = SOSFragment.newInstance();
                    new SOSPresenter(sosFragment,slideItem, preferenceUtil, repository);
                    return sosFragment;
                case SLIDE_INDEX_REMINDERS:
                    ReminderFragment reminderFragment = ReminderFragment.newInstance();
                    new ReminderPresenter(reminderFragment,slideItem, preferenceUtil, repository);
                    return reminderFragment;

            }
        return null;
    }

}
