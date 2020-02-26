package com.ebookfrenzy.quoteclient.service;

import com.ebookfrenzy.quoteclient.BuildConfig;
import com.ebookfrenzy.quoteclient.model.Quote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.Single;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface QodService {

  @GET("quotes/random")
  Single<Quote> getRandom();

  @GET("quotes")
  Single<List<Quote>> getAll();

  static QodService getInstance() {
    return InstanceHolder.INSTANCE;
  }
  class InstanceHolder {

    private static final String TIMESTAMP_FORMAT= "yyyy-MM-dd'T'HH:mm:ss.SSSZ ";
    private static final QodService INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat( TIMESTAMP_FORMAT )
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(  );
      interceptor.setLevel( Level.BODY );
      OkHttpClient client = new OkHttpClient.Builder()
          .readTimeout(60, TimeUnit.SECONDS )
          .addInterceptor( interceptor )
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory( GsonConverterFactory.create(gson) )
          .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
          .client( client )
          .baseUrl( BuildConfig.BASE_URL )
          .build();
      INSTANCE = retrofit.create( QodService.class );
    }
  }

}
