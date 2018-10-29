package ltd.kaizo.go4lunch.models.API.Stream

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ltd.kaizo.go4lunch.models.API.NearbySearch.PlaceApiData
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailApiData
import java.util.concurrent.TimeUnit

object PlaceStream {

    fun streamFetchNearbyRestaurant(location: String): Observable<PlaceApiData> {
        val placeService = PlaceService.retrofit.create(PlaceService::class.java)
        return placeService.getNearbyRestaurant(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
    }

    fun streamFetchPlaceDetail(placeId: String): Observable<PlaceDetailApiData> {
        val placeService = PlaceService.retrofit.create(PlaceService::class.java)
        return placeService.getPlaceDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
    }

    fun streamFetchNearbyRestaurantAndGetPlaceDetail(location: String): Observable<PlaceDetailApiData> {
        return streamFetchNearbyRestaurant(location)
                .flatMapIterable { it.results }
                .flatMap { streamFetchPlaceDetail(it.placeId) }
    }


}
