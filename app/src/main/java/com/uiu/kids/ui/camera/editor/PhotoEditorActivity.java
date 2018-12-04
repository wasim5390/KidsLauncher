package com.uiu.kids.ui.camera.editor;

import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.util.PreferenceUtil;


public class PhotoEditorActivity extends BaseActivity {

    private static final String TAG = PhotoEditorActivity.class.getSimpleName();

    PhotoEditorFragment photoEditorFragment;
    PhotoEditorPresenter presenter;
    @Override
    public int getID() {
        makeFullScreen();
        return R.layout.activity_photo_edit;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        String path = getIntent().getStringExtra(CAMERA_IMG_PATH);
        photoEditorFragment = photoEditorFragment != null ? photoEditorFragment : PhotoEditorFragment.newInstance(path);
        presenter = presenter!=null ? presenter: new PhotoEditorPresenter(photoEditorFragment, PreferenceUtil.getInstance(this).getAccount().getId(), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, photoEditorFragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mGpsSwitchStateReceiver);
    }


}
