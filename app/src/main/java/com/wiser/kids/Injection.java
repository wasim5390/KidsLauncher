package com.wiser.kids;

import android.content.Context;
import android.support.annotation.NonNull;


import com.wiser.kids.source.Repository;

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
