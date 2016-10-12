package io.github.plastix.prolificlibrary.ui.detail;

import dagger.Subcomponent;
import io.github.plastix.prolificlibrary.ui.ActivityScope;

@ActivityScope
@Subcomponent(modules = DetailModule.class)
public interface DetailComponent {
    void injectTo(DetailActivity activity);
}
