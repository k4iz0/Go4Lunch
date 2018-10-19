import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ltd.kaizo.go4lunch.models.API.PlaceApiData;
import ltd.kaizo.go4lunch.models.utils.PlaceStream;

import static org.junit.Assert.assertEquals;

public class PlaceStreamTest {

    @Test
    public void streamFetchNearbyRestaurantResponseShouldBeOK() {
        Observable<PlaceApiData> apidata = PlaceStream.streamFetchNearbyRestaurant("48.733333,-3.466667");
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
        Observable<PlaceApiData> apidata = PlaceStream.streamFetchPlaceDetail("ChIJ264QumksEkgRyrZAcMn-Zus");
        TestObserver<PlaceApiData> testObserver = new TestObserver<>();
        apidata.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();
        String apiResponseStatus = testObserver.values().get(0).getStatus();


        assertEquals("OK", apiResponseStatus);

    }
}