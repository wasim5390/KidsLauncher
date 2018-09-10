package com.uiu.kids;

import android.content.Context;
import android.support.annotation.NonNull;

import com.uiu.kids.source.Repository;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created on 24/10/2017.
 */

public class Injection {

    public static Repository provideRepository(@NonNull Context context) {
        checkNotNull(context);
        return Repository.getInstance();
    }
}
