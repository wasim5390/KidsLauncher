package com.wiser.kids.ui.favorite.people;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.model.User;
import com.wiser.kids.ui.home.contact.ContactActivity;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.home.contact.info.ContactInfoActivity;
import com.wiser.kids.util.PreferenceUtil;

import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

public class FavoritePeopleFragment extends BaseFragment implements FavoritePeopleAdapter.Callback,
        FavoritePeopleContract.View
{
    private static final int REQ_CONTACT = 0x101;
    private static final int REQ_CONTACT_INFO = 0x102;
    public static String TAG ="FavoritePeopleFragment";

    @BindView(R.id.rvFavPeoples)
    RecyclerView recyclerView;
    private FavoritePeopleAdapter adapter;
    FavoritePeopleContract.Presenter mPresenter;

    public static FavoritePeopleFragment newInstance() {
        Bundle args = new Bundle();
        FavoritePeopleFragment homeFragment = new FavoritePeopleFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_people;
    }

    @Override
    public void initUI(View view) {
        setRecyclerView();
        mPresenter.start();
    }
    public void setRecyclerView(){
        adapter = new FavoritePeopleAdapter(getContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem) {
        new Handler().postDelayed(() -> {
            if(slideItem.getAndroidId()==null)
            {
               // if(slideItem.hasAccess())
                    startActivityForResult(new Intent(getContext(), ContactActivity.class),REQ_CONTACT);
            }else{
                Intent i = new Intent(getContext(), ContactInfoActivity.class);
                i.putExtra(Constant.SELECTED_CONTACT, slideItem);
                startActivityForResult(i,REQ_CONTACT_INFO);
            }
        },1);

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onFavoritePeopleLoaded(List<ContactEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void onFavoritePeopleSaved() {

    }

    @Override
    public void setPresenter(FavoritePeopleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CONTACT){
            if(resultCode==RESULT_OK){
                User user= PreferenceUtil.getInstance(getActivity()).getAccount();
                //Log.i("UserId","-"+user.getUserId());
                mPresenter.saveFavoritePeople((ContactEntity) data.getSerializableExtra(KEY_SELECTED_CONTACT),String.valueOf(user.getId()));
            }
        }
    }
}
