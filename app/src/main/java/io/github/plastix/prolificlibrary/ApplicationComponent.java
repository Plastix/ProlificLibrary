package io.github.plastix.prolificlibrary;

import javax.inject.Singleton;

import dagger.Component;
import io.github.plastix.prolificlibrary.data.remote.ApiModule;
import io.github.plastix.prolificlibrary.ui.detail.DetailComponent;
import io.github.plastix.prolificlibrary.ui.detail.DetailModule;
import io.github.plastix.prolificlibrary.ui.list.ListComponent;
import io.github.plastix.prolificlibrary.ui.list.ListModule;

@Singleton // Constraints this component to one-per-application or un-scoped bindings.
@Component(modules = {
        ApplicationModule.class,
        ApiModule.class
})
public interface ApplicationComponent {

    // Sub-component methods
    // Every screen has its own sub-component of the graph and must be added here!
    ListComponent plus(ListModule module);

    DetailComponent plus(DetailModule module);

}
