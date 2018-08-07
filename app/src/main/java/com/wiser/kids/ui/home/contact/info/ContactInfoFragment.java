package com.wiser.kids.ui.home.contact.info;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.HomeItemView;
import com.wiser.kids.ui.home.HomeSlideAdapter;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PermissionUtil;

import java.security.Permission;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.internal.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactInfoFragment extends BaseFragment implements ContactInfoContract.View,HomeSlideAdapter.Callback{

    private ContactInfoContract.Presenter presenter ;

    @BindView(R.id.contact_info_text_name)
    public TextView mName;
    @BindView(R.id.contact_info_details)
    public TextView mDetails;
    @BindView(R.id.contact_info_add_number_btn)
    public TextView mAddNumber;
    @BindView(R.id.contact_info_avatar)
    public CircleImageView mPicture;
    private ContactEntity mContact;
    @BindView(R.id.progressLoader)
    public RelativeLayout mLoader;

  //  @BindView(R.id.contact_info_call_btn)
  //  HomeItemView callBtn;


    private int mTabMode = TAB_MODE_MOBILE;

    public static ContactInfoFragment newInstance() {
        Bundle args = new Bundle();
        ContactInfoFragment homeFragment = new ContactInfoFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public int getID() {
        return R.layout.fragment_contact_info;
    }

    @Override
    public void initUI(View view) {
        presenter.loadContact();

    }

    @Override
    public void showMessage() {

    }

    @Override
    public void onContactLoaded(ContactEntity contactEntity) {
        this.mContact = contactEntity;
        mName.setText(mContact.getName());
        presenter.getContactType(contactEntity);
        Picasso.with(getContext()).load(mContact.getPhotoUri())
                .placeholder(R.mipmap.wiser_avatar).error(R.mipmap.wiser_avatar).into(mPicture);
    }

    @Override
    public void onContactTypeMobile() {
        mLoader.setVisibility(View.GONE);
        //check if the user has mobile number
        if (TextUtils.isEmpty(mContact.getmPhoneNumber())) {
            //user has no phone number, let's show the "Add number" button
            showAddNumberButton(getString(R.string.add_number));
            mDetails.setText(R.string.mobile_number);
        } else {
            showCallAndSMSButtons();
            mDetails.setText(PhoneNumberUtils.formatNumber((mContact.getmPhoneNumber())));
        }
        updateTabBackground(R.id.contact_info_tab_mobile);
    }

    @Override
    public void onContactTypeHome() {
        mLoader.setVisibility(View.GONE);
        //check if the user has home number
        if (TextUtils.isEmpty(mContact.getmHomeNumber())) {
            //user has no phone number, let's show the "Add number" button
            showAddNumberButton(getString(R.string.add_number));
            mDetails.setText(R.string.home_number);
        } else {
            showCallAndSMSButtons();
            mDetails.setText(mContact.getmHomeNumber());
        }
        updateTabBackground(R.id.contact_info_tab_home);
    }

    @Override
    public void onContactTypeEmail() {
        mLoader.setVisibility(View.GONE);
        showEmailButton();
        if (TextUtils.isEmpty(mContact.getEmail())) {
            //user has no email address
            showAddNumberButton(getString(R.string.add_email));
            mDetails.setText(R.string.email_address);
        } else {
            showEmailButton();
            mDetails.setText(mContact.getEmail());
        }
        updateTabBackground(R.id.contact_info_tab_email);
    }

    private void showAddNumberButton(String text) {
        //hide call and sms
        view.findViewById(R.id.contact_info_call_btn).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.contact_info_sms_btn).setVisibility(View.INVISIBLE);
        //hide email btn
        view.findViewById(R.id.contact_info_email_btn).setVisibility(View.GONE);
        //show add number
        mAddNumber.setText(text);
        mAddNumber.setVisibility(View.VISIBLE);
    }
    private void showEmailButton() {
        //hide call and sms
        view.findViewById(R.id.contact_info_call_btn).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.contact_info_sms_btn).setVisibility(View.INVISIBLE);
        //hide add number
        view.findViewById(R.id.contact_info_add_number_btn).setVisibility(View.GONE);
        //show email btn
        view.findViewById(R.id.contact_info_email_btn).setVisibility(View.VISIBLE);
    }
    private void updateTabBackground(int selectedTabId) {
        view.findViewById(R.id.contact_info_tab_mobile).setSelected(false);
        view.findViewById(R.id.contact_info_tab_home).setSelected(false);
        view.findViewById(R.id.contact_info_tab_email).setSelected(false);
        view.findViewById(selectedTabId).setSelected(true);
    }

    private void showCallAndSMSButtons() {
        //hide add number
        view.findViewById(R.id.contact_info_add_number_btn).setVisibility(View.GONE);
        //hide email
        view.findViewById(R.id.contact_info_email_btn).setVisibility(View.GONE);
        //show call & sms
        view.findViewById(R.id.contact_info_call_btn).setVisibility(View.VISIBLE);
        view.findViewById(R.id.contact_info_sms_btn).setVisibility(View.VISIBLE);

    }




    @OnClick(R.id.contact_info_call_btn)
    public void onCallContact() {
        callContact();
    }

    @OnClick(R.id.contact_info_sms_btn)
    public void onSendSmsClick() {
        allowSMS();
    }

    @OnClick(R.id.contact_info_email_btn)
    public void onSendEmailClick() {
        sendEmail();
    }

    @OnClick(R.id.contact_info_tab_mobile)
    public void onMobileTabClick(){
        onContactTypeMobile();
    }
    @OnClick(R.id.contact_info_tab_home)
    public void onHomeTabClick(){
        onContactTypeHome();
    }
    @OnClick(R.id.contact_info_tab_email)
    public void onEmailTabClick(){
        onContactTypeEmail();
    }


    private void callContact() {
        if(PermissionUtil.isPermissionGranted(getContext(), Manifest.permission.CALL_PHONE))
            callPhoneNumber();
        else{
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CALL_PHONE, new PermissionUtil.PermissionCallback() {
                @Override
                public void onPermissionsGranted(String permission) {
                    callPhoneNumber();
                }

                @Override
                public void onPermissionsGranted() {

                }

                @Override
                public void onPermissionDenied() {

                }
            });

        }
    }
    private void allowSMS() {
        if(PermissionUtil.isPermissionGranted(getContext(),Manifest.permission.SEND_SMS))
            sendMessage();
        else
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.SEND_SMS, new PermissionUtil.PermissionCallback() {
                @Override
                public void onPermissionsGranted(String permission) {
                    sendMessage();
                }

                @Override
                public void onPermissionsGranted() {

                }

                @Override
                public void onPermissionDenied() {

                }
            });

    }
    /**
     * Send message
     */
    private void sendMessage() {

        if (!TextUtils.isEmpty(mDetails.getText())) {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:" + mDetails.getText()));
            try {
                startActivity(smsIntent);
            } catch (ActivityNotFoundException ex) {
                // @todo replace toast with in-app notification dialog
                Toast.makeText(mBaseActivity, getString(R.string.no_sms_clients), Toast.LENGTH_SHORT).show();
               // Utils.showAlertError(mContext.getString(R.string.no_sms_clients), true);
            }
        }
    }
    /**
     * Send email
     */
    private void sendEmail() {

        if (!TextUtils.isEmpty(mDetails.getText())) {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mDetails.getText().toString()});
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                // @todo replace toast with in-app notification dialog
                Toast.makeText(mBaseActivity, getString(R.string.no_email_clients), Toast.LENGTH_SHORT).show();
               // Utils.showAlertError(mContext.getString(R.string.no_email_clients), true);
            }
        }
    }
    /**
     * Call a phone number
     */
    @SuppressLint("MissingPermission")
    private void callPhoneNumber(){
        if (!TextUtils.isEmpty(mDetails.getText())) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + mDetails.getText()));
            try {
                startActivity(callIntent);
            } catch (ActivityNotFoundException ex) {
                // @todo replace toast with in-app notification dialog
                Toast.makeText(mBaseActivity, getString(R.string.no_phone_clients), Toast.LENGTH_SHORT).show();
              //  Utils.showAlertError(getString(R.string.no_phone_clients), true);
            }
        }
    }

    @Override
    public void setPresenter(ContactInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(String slideItem) {

    }
}
