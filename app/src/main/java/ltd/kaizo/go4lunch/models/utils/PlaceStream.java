package ltd.kaizo.go4lunch.models.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ltd.kaizo.go4lunch.models.API.PlaceApiData;

public class PlaceStream {

    public static Observable<PlaceApiData> streamFetchNearbyRestaurant(String location) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);
        return placeService.getNearbyRestaurant(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
    public static Observable<PlaceApiData> streamFetchPlaceDetail(String placeId) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);
        return placeService.getPlaceDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
