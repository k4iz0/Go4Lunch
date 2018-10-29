package ltd.kaizo.go4lunch.models.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ltd.kaizo.go4lunch.models.API.NearbySearch.PlaceApiData;
import ltd.kaizo.go4lunch.models.API.NearbySearch.Result;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailApiData;
import ltd.kaizo.go4lunch.models.API.Stream.PlaceService;

public class PlaceStreamJava {
    public static Observable<PlaceApiData>  streamFetchNearbyRestaurant(String location) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);
        return placeService.getNearbyRestaurant(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);

    }
 public static Observable<PlaceDetailApiData> streamFetchPlaceDetail(String placeId) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);
        return placeService.getPlaceDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);

    }

    public static Observable<PlaceDetailApiData> streamFetchNearbyRestaurantAndGetPlaceDetail(String location) {
        final PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);
        return placeService.getNearbyRestaurant(location)
                .flatMapIterable(new Function<PlaceApiData, List<Result>>() {
                    @Override
                    public List<Result> apply(PlaceApiData placeApiData) throws Exception {
                        return placeApiData.getResults();
                    }
                })
                .flatMap(new Function<Result, ObservableSource<PlaceDetailApiData>>() {
                    @Override
                    public ObservableSource<PlaceDetailApiData> apply(Result result) throws Exception {
                        return placeService.getPlaceDetails(result.getPlaceId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);

    }



}
