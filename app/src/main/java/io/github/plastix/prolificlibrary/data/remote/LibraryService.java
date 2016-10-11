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
import rx.Completable;
import rx.Single;

public interface LibraryService {

    String BASE_URL = "http://prolific-interview.herokuapp.com/57fce7895961070009f68fd0/";

    @GET("/books")
    Single<List<Book>> fetchAllBooks();

    @FormUrlEncoded
    @POST("/books/")
    Single<Book> submitBook(@Field("author") String author,
                            @Field("categories") String categories,
                            @Field("title") String title,
                            @Field("publisher") String publisher
    );

    @GET("/books/{id}")
    Single<Book> getBook(@Path("id") int id);

    @PUT("/books/{id}")
    Single<Book> updateBook(@Path("id") int id,
                            @Field("lastCheckedOutBy") String date
    );

    @DELETE("/clean")
    Completable clearAllBooks();
}
