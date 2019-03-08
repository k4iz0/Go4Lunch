package ltd.kaizo.go4lunch.models.API.stream

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ltd.kaizo.go4lunch.models.API.nearbySearch.PlaceApiData
import ltd.kaizo.go4lunch.models.API.placeDetail.PlaceDetailApiData
import ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RADIUS_KEY
import ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read
import java.util.concurrent.TimeUnit

object PlaceStream {

    fun streamFetchNearbyRestaurant(location: String, radius: String): Observable<PlaceApiData> {
        val placeService = PlaceService.retrofit.create(PlaceService::class.java)
        return placeService.getNearbyRestaurant(location, radius)
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

    fun streamFetchNearbyRestaurantAndGetPlaceDetail(location: String, radius: String): Observable<PlaceDetailApiData> {
        return streamFetchNearbyRestaurant(location, radius)
                .concatMapIterable { it.results }
                .concatMap { streamFetchPlaceDetail(it.placeId) }
    }

}
