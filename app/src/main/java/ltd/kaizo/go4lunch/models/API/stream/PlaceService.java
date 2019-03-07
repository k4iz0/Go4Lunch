package ltd.kaizo.go4lunch.models.API.stream;

import io.reactivex.Observable;
import ltd.kaizo.go4lunch.BuildConfig;
import ltd.kaizo.go4lunch.models.API.nearbySearch.PlaceApiData;
import ltd.kaizo.go4lunch.models.API.placeDetail.PlaceDetailApiData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The interface Place service.
 */
public interface PlaceService {
    /**
     * The constant apiKey.
     */
    String apiKey = BuildConfig.ApiKey;
    /**
     * The constant url.
     */
    String url = "https://maps.googleapis.com/maps/api/place/";
    /**
     * The radius
     */
    String radius = "3000";
    /**
     * The constant interceptor.
     */
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    /**
     * The constant okHttpClient.
     */
    OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC));


    /**
     * The constant retrofit builder
     */
    Retrofit retrofit = new Retrofit.Builder()

            .baseUrl(url)

            .addConverterFactory(GsonConverterFactory.create())

            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient.build())
            .build();

    /**
     * Gets the nearby restaurant
     *
     * @param location the location
     * @return the nearby restaurant
     */
    @GET("nearbysearch/json?radius=" + radius + "&type=restaurant&key=" + apiKey)
    Observable<PlaceApiData> getNearbyRestaurant(@Query("location") String location);

    /**
     * Gets the place's détails
     *
     * @param placeid the placeid
     * @return the place details
     */
    @GET("details/json?fields=name," +
            "rating," +
            "geometry," +
            "photo," +
            "website," +
            "formatted_phone_number," +
            "vicinity," +
            "opening_hours" +
            "&key=" + apiKey)
    Observable<PlaceDetailApiData> getPlaceDetails(@Query("placeid") String placeid);

}
