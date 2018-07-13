package com.wiser.kids.ui.home.contact.edit;

import android.os.Bundle;

import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;

import com.wiser.kids.ui.home.contact.ContactEntity;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditContactInfoFragment extends BaseFragment implements EditContactInfoContract.View{


    @BindView(R.id.tv_contact_title)
    public TextView tvTitleHeading;
    @BindView(R.id.tv_contact_name)
    public TextView tvTitleName;

    @BindView(R.id.tv_contact_picture)
    public TextView tvPictureHeading;
    @BindView(R.id.iv_contact_picture)
    public CircleImageView mPicture;

    @BindView(R.id.tv_contact_mobile_title)
    public TextView tvMobileHeading;
    @BindView(R.id.tv_contact_mobile)
    public TextView tvMobile;

    @BindView(R.id.tv_contact_home_title)
    public TextView tvHomeHeading;
    @BindView(R.id.tv_contact_home)
    public TextView tvHome;

    @BindView(R.id.tv_contact_email_title)
    public TextView tvEmailHeading;
    @BindView(R.id.tv_contact_email)
    public TextView tvEmail;


    private ContactEntity mContact;
    private EditContactInfoContract.Presenter presenter ;



    public static EditContactInfoFragment newInstance(ContactEntity contactEntity) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.SELECTED_CONTACT,contactEntity);
        EditContactInfoFragment homeFragment = new EditContactInfoFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public int getID() {
        return R.layout.fragment_edit_contact_info;
    }


    @Override
    public void initUI(View view) {
        mContact = (ContactEntity) getActivity().getIntent().getSerializableExtra(Constant.SELECTED_CONTACT);
        presenter.checkContactDetails();
    }

    @Override
    public void showMessage() {

    }

    @Override
    public void onNameEntry(boolean value) {
        if(value) {
            tvTitleHeading.setText(R.string.contact_edit_name);
            tvTitleName.setText(mContact.getName());
        }else {
            tvTitleHeading.setText(R.string.contact_add_name);
            tvTitleName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPicEntry(boolean value) {
        if(value) {
            tvPictureHeading.setText(R.string.contact_edit_picture);
            Picasso.with(getContext()).load(mContact.getPhotoUri()).into(mPicture);
        }else {
            tvPictureHeading.setText(R.string.contact_add_picture);
            mPicture.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMobileNumberEntry(boolean value) {
        if(value) {
            tvMobileHeading.setText(R.string.contact_edit_mobile_number);
            tvMobile.setText(PhoneNumberUtils.formatNumber(mContact.getmPhoneNumber()));
        }else {
            tvMobileHeading.setText(R.string.contact_add_mobile_number);
            tvMobile.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHomeNumberEntry(boolean value) {
        if(value) {
            tvHomeHeading.setText(R.string.contact_edit_home_number);
            tvHome.setText(PhoneNumberUtils.formatNumber(mContact.getmHomeNumber()));
        }else {
            tvHomeHeading.setText(R.string.contact_add_home_number);
            tvHome.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmailEntry(boolean value) {
        if(value) {
            tvEmailHeading.setText(R.string.contact_edit_email);
            tvEmail.setText(mContact.getEmail());
        }else {
            tvEmailHeading.setText(R.string.contact_add_email);
            tvEmail.setVisibility(View.GONE);
        }
    }


    @Override
    public void setPresenter(EditContactInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

}
