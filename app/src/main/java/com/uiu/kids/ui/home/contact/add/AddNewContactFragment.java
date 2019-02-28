package com.uiu.kids.ui.home.contact.add;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.PicModeSelectDialog;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.home.contact.info.ContactInfoFragment;
import com.uiu.kids.util.PermissionUtil;

import java.io.File;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddNewContactFragment extends BaseFragment implements AddNewContactContract.View,
        PicModeSelectDialog.IPicModeSelectListener
{

    private AddNewContactContract.Presenter presenter ;

    @BindView(R.id.contact_first_name)
    public EditText etName;


    @BindView(R.id.contact_mobile_number)
    public EditText etMobileNumber;

    @BindView(R.id.contact_info_avatar)
    public CircleImageView mPicture;


    private String contactImagePath=null;




    public static AddNewContactFragment newInstance() {
        Bundle args = new Bundle();
        AddNewContactFragment homeFragment = new AddNewContactFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public int getID() {
        return R.layout.fragment_add_new_contact;
    }




    @Override
    public void initUI(View view) {


    }


    @Override
    public void showMessage(String message) {
        Toast toast = Toast.makeText(getContext(),message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,100);
        toast.show();
    }

    @Override
    public void onContactCreated(ContactEntity contact) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SELECTED_CONTACT,contact);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getActivity().setResult(RESULT_OK,intent);
        getActivity().finish();

    }


    @Override
    public void onMobileEmpty() {
        etMobileNumber.requestFocus();

    }

    @Override
    public void onFirstNameEmpty() {
        etName.requestFocus();
    }



    @OnClick({R.id.contact_info_avatar,R.id.ivUpdatePic})
    public void onProfilePicUpdate(){
        showAddPicDialog(false);
    }

    @OnClick(R.id.iv_add)
    public void onSaveClick(){
        presenter.createContact(etName.getText().toString(),etMobileNumber.getText().toString());
    }

    @OnClick(R.id.iv_cancel)
    public void onCancelClick(){
        getActivity().setResult(RESULT_CANCELED);
        getActivity().finish();
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
    public void setPresenter(AddNewContactContract.Presenter presenter) {
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
                        File file  = new File(contactImagePath);
                        Picasso.get().load(file).into(mPicture);
                        presenter.saveContactImage(file);

                    }
                    break;

            }

        }

    }

    public interface ContactAddListener {
        public void onAddListener(ContactEntity entity);
    }
}
