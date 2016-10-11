package io.github.plastix.prolificlibrary.data.remote;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    @Singleton
    public static GsonConverterFactory provideGsonConverter() {
        return GsonConverterFactory.create();
    }


    @Provides
    @Singleton
    public static RxJavaCallAdapterFactory provideRxCallAdapter() {
        return RxJavaCallAdapterFactory.create();
    }


    @Provides
    @Singleton
    public static Retrofit provideRetrofit(@Named("BASE_URL") String baseUrl,
                                           GsonConverterFactory gsonConverter,
                                           RxJavaCallAdapterFactory rxAdapter) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverter)
                .addCallAdapterFactory(rxAdapter)
                .build();
    }

    @Provides
    @Singleton
    @Named("BASE_URL")
    public static String provideApiEndpoint() {
        return LibraryService.BASE_URL;
    }

    @Provides
    @Singleton
    public static LibraryService provideLibraryService(Retrofit retrofit) {
        return retrofit.create(LibraryService.class);
    }
}
