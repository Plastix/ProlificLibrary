package io.github.plastix.prolificlibrary.ui.add;

import dagger.Subcomponent;
import io.github.plastix.prolificlibrary.ui.ActivityScope;

@ActivityScope
@Subcomponent(modules = AddModule.class)
public interface AddComponent {
    void injectTo(AddActivity activity);
}
