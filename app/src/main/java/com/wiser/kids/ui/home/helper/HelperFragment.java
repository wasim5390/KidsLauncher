package com.wiser.kids.ui.home.helper;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HelperFragment extends BaseFragment implements HelperContract.view,HelperAdapterList.Callback {

    public HelperContract.Presenter presenter;
    public HelperAdapterList adapter;
    public List<HelperEntity> helperEntities = new ArrayList<>();

    private boolean isPrimaryHelperSelection=false;

    @BindView(R.id.rvhelper)
    RecyclerView recyclerView;

    @BindView(R.id.headerView)
    TextView title;

    HelperEntity primaryHelper;

    public static HelperFragment newInstance()
    {
        Bundle args=new Bundle();
        HelperFragment instance=new HelperFragment();
        instance.setArguments(args);
        return instance;
    }


    @Override
    public int getID() {
        return R.layout.fragment_helper;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        presenter.start();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new HelperAdapterList(getContext(), helperEntities, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void setPresenter(HelperContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(HelperEntity slideItem, boolean isChecked) {
        if(isPrimaryHelperSelection)
        {
            primaryHelper = slideItem;
            for(HelperEntity entity:helperEntities){
                entity.setHelperSelected(slideItem==entity);
            }
            adapter.notifyDataSetChanged();
        }else
            slideItem.setHelperSelected(isChecked);

    }

    @Override
    public void loadHelperList(List<HelperEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void onHelpersSaved() {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public void onPrimarySelection(boolean isPrimay) {
        isPrimaryHelperSelection =isPrimay;
        title.setText(isPrimay?"Select primary helper":"Helpers");
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnDone)
    public void saveParents(){
        if(isPrimaryHelperSelection)
            presenter.savePrimaryHelper(primaryHelper.getId());
        else {
            List<HelperEntity> selectedHelper = adapter.getSelectedHelpers();
            presenter.updateHelpers(selectedHelper);
        }
    }
}
