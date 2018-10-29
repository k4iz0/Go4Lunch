import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ltd.kaizo.go4lunch.models.API.NearbySearch.PlaceApiData;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailApiData;
import ltd.kaizo.go4lunch.models.API.Stream.PlaceStream;

import static org.junit.Assert.assertEquals;

public class PlaceStreamTest {

    @Test
    public void streamFetchNearbyRestaurantResponseShouldBeOK() {
        Observable<PlaceApiData> apidata = PlaceStream.INSTANCE.streamFetchNearbyRestaurant("48.733333,-3.466667");
        TestObserver<PlaceApiData> testObserver = new TestObserver<>();
        apidata.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();
        String apiResponseStatus = testObserver.values().get(0).getStatus();


        assertEquals("OK", apiResponseStatus);

    }

    @Test
    public void streamFetchPlaceDetailResponseShouldBeOK() {
        Observable<PlaceDetailApiData> apidata = PlaceStream.INSTANCE.streamFetchPlaceDetail("ChIJ264QumksEkgRyrZAcMn-Zus");
        TestObserver<PlaceDetailApiData> testObserver = new TestObserver<>();
        apidata.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();
        String apiResponseStatus = testObserver.values().get(0).getStatus();


        assertEquals("OK", apiResponseStatus);

    }

    @Test
    public void streamFetchNearbyRestaurantAndGetPlaceDetailResponseShouldBeOK() {
        Observable<PlaceDetailApiData> apidata = PlaceStream.INSTANCE.streamFetchNearbyRestaurantAndGetPlaceDetail("48.733333,-3.466667");
        TestObserver<PlaceDetailApiData> testObserver = new TestObserver<>();
        apidata.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();
        int apiResponseStatus = testObserver.values().size();


        assertEquals("OK", apiResponseStatus);

    }

}