package io.github.plastix.prolificlibrary.data.remote;

import java.util.List;

import io.github.plastix.prolificlibrary.data.model.Book;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Single;

public interface LibraryService {

    String BASE_URL = "http://prolific-interview.herokuapp.com/57fce7895961070009f68fd0/";

    @GET("books")
    Single<List<Book>> fetchAllBooks();

    @FormUrlEncoded
    @POST("books/")
    Single<Book> submitBook(@Field("author") String author,
                            @Field("categories") String categories,
                            @Field("title") String title,
                            @Field("publisher") String publisher
    );

    @GET("books/{id}")
    Single<Book> getBook(@Path("id") int id);

    @FormUrlEncoded
    @PUT("books/{id}/")
    Single<Book> checkoutBook(@Path("id") int id,
                              @Field("lastCheckedOutBy") String name
    );

    // Using a Single<Void> instead of a Completable because RxJava 1.2.0 broke the
    // Retrofit Rx call adapter and the fix isn't released yet
    // See https://github.com/square/retrofit/issues/2034
    @DELETE("clean/")
    Single<Void> clearAllBooks();
}
