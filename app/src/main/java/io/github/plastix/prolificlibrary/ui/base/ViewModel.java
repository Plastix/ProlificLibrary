package io.github.plastix.prolificlibrary.ui.base;

import android.content.Context;

public interface ViewModel {

    void bind(Context context);

    void unbind();

    void onDestroy();
}
