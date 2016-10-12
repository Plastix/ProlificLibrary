package io.github.plastix.prolificlibrary.ui.list;

import dagger.Subcomponent;
import io.github.plastix.prolificlibrary.ui.ActivityScope;

@ActivityScope
@Subcomponent(modules = ListModule.class)
public interface ListComponent {
    void injectTo(ListActivity activity);
}
