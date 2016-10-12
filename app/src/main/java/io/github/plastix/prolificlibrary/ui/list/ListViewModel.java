package io.github.plastix.prolificlibrary.ui.list;

import android.view.View;

import javax.inject.Inject;

import io.github.plastix.prolificlibrary.ui.base.RxViewModel;

public class ListViewModel extends RxViewModel {

    @Inject
    public ListViewModel() {
    }

    public View.OnClickListener onFabClick() {
        return View -> {
        };
    }


}
