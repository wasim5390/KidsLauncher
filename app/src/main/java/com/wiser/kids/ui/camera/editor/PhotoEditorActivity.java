package com.wiser.kids.ui.camera.editor;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.util.PreferenceUtil;


public class PhotoEditorActivity extends BaseActivity  {

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
        photoEditorFragment = photoEditorFragment != null ? photoEditorFragment : PhotoEditorFragment.newInstance();
        presenter = presenter!=null ? presenter: new PhotoEditorPresenter(photoEditorFragment, PreferenceUtil.getInstance(this).getAccount().getId(), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, photoEditorFragment);
        fragmentTransaction.commit();

    }


}
