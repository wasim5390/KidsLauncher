package com.uiu.kids.ui.home.contact.info;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.HomeSlideAdapter;
import com.uiu.kids.ui.home.PicModeSelectDialog;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PermissionUtil;


import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ContactInfoFragment extends BaseFragment implements ContactInfoContract.View,
        PicModeSelectDialog.IPicModeSelectListener
{

    private ContactInfoContract.Presenter presenter ;

    @BindView(R.id.contact_info_text_name)
    public TextView mName;
    @BindView(R.id.contact_info_details)
    public TextView mDetails;

    @BindView(R.id.contact_info_avatar)
    public CircleImageView mPicture;
    private ContactEntity mContact;
    @BindView(R.id.progressLoader)
    public RelativeLayout mLoader;

    @BindView(R.id.contact_info_tabs_container)
    public LinearLayout tabsView;
    
    private String contactImagePath=null;
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
    public void showProgress() {
        mLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mLoader.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onContactLoaded(ContactEntity contactEntity) {
        this.mContact = contactEntity;
        mName.setText(mContact.getName()+"");
        tabsView.setVisibility(View.GONE);
        Picasso.get().load(mContact.getProfilePic())
                .placeholder(R.mipmap.wiser_avatar).error(R.mipmap.wiser_avatar).into(mPicture);
        onContactTypeMobile();
    }

    @Override
    public void onContactTypeMobile() {
        mLoader.setVisibility(View.GONE);
        //check if the user has mobile number
        if (!TextUtils.isEmpty(mContact.getMobileNumber())) {
            mDetails.setText(PhoneNumberUtils.formatNumber((mContact.getMobileNumber())));
        }else if(!TextUtils.isEmpty(mContact.getmHomeNumber())){
            mDetails.setText(PhoneNumberUtils.formatNumber((mContact.getmHomeNumber())));
        }

    }



    @Override
    public void onContactUpdated(ContactEntity contact) {
        getActivity().setResult(RESULT_OK);
    }






    @OnClick(R.id.contact_info_call_btn)
    public void onCallContact() {
        callContact();
    }

    @OnClick(R.id.contact_info_sms_btn)
    public void onSendSmsClick() {
        allowSMS();
    }


    @OnClick(R.id.contact_info_tab_mobile)
    public void onMobileTabClick(){
        onContactTypeMobile();
    }
    
    @OnClick({R.id.contact_info_avatar,R.id.ivUpdatePic})
    public void onProfilePicUpdate(){
        showAddPicDialog(false);
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

    /**
     * Dialog Pic chooser
     */
    private void showAddPicDialog(boolean isIdentityPic) {
        PicModeSelectDialog dialog = new PicModeSelectDialog(getActivity(),isIdentityPic);
        dialog.setiPicModeSelectListener(this);
        dialog.show();
    }

    @Override
    public void setPresenter(ContactInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }




    @Override
    public void onPicModeSelected(String mode, boolean isIdentityPic) {
        if( mode.equalsIgnoreCase(PicModes.CANCEL))
            return;
        String action = mode.equalsIgnoreCase(Constant.PicModes.CAMERA) ? Constant.IntentExtras.ACTION_CAMERA : Constant.IntentExtras.ACTION_GALLERY;
        actionPic(action,isIdentityPic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_CODE_UPDATE_PIC:
                    contactImagePath = result.getStringExtra(Constant.IntentExtras.IMAGE_PATH);
                    if(contactImagePath!=null) {
                        Picasso.get().load(new File(contactImagePath)).into(mPicture);
                        presenter.updateContactPic(new File(contactImagePath));
                        ((TextView)getActivity().findViewById(R.id.tvProgress)).setText("Saving...");
                    }
                    break;
           
            }

        }

    }
}
