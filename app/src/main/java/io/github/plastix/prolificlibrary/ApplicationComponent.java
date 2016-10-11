package io.github.plastix.prolificlibrary;

import javax.inject.Singleton;

import dagger.Component;
import io.github.plastix.prolificlibrary.data.remote.ApiModule;

@Singleton // Constraints this component to one-per-application or un-scoped bindings.
@Component(modules = {
        ApplicationModule.class,
        ApiModule.class
})
public interface ApplicationComponent {

}
