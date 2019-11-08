package org.bochman.upapp.api;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import androidx.test.core.app.ApplicationProvider;

import org.bochman.upapp.data.enteties.Poi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.google.common.truth.Truth.assertThat;

import org.hamcrest.collection.IsEmptyCollection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PlaceApiTest {

    private Context context = ApplicationProvider.getApplicationContext();

    private static final String FAKE_STRING = "Share";

    @Test
    public void readStringFromContext_LocalizedString() {
        // Given a Context object retrieved from Robolectric...
        PlaceApi myObjectUnderTest = new PlaceApi(context);

        // ...when the string is returned from the object under test...
        String result = myObjectUnderTest.getShareString(context);

        // ...then the result should be the expected one.
        assertThat(result).isEqualTo(FAKE_STRING);
    }

    LatLng latLng= new LatLng(32.0668,34.8149);

    /**
     * reminder how to tests lists.
     */
    @Test
    public void testAssertList() {
        List<String> actual = Arrays.asList("a", "b", "c");
        List<String> expected = Arrays.asList("a", "b", "c");

        //All passed / true

        //1. Test equal.
        assertThat(actual, is(expected));

        //2. If List has this value?
        assertThat(actual, hasItems("b"));

        //3. Check List Size
        assertThat(actual, hasSize(3));

        assertThat(actual.size(), is(3));

        //4.  List order

        // Ensure Correct order
        assertThat(actual, contains("a", "b", "c"));

        // Can be any order
        assertThat(actual, containsInAnyOrder("c", "b", "a"));

        //5. check empty list
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertThat(new ArrayList<>(), IsEmptyCollection.empty());


    }
    @Test
    public void findCurrentLocation() throws ExecutionException, InterruptedException {
        PlaceApi myObjectUnderTest = new PlaceApi(context);
        CompletableFuture<List<Poi>> actual = myObjectUnderTest.findCurrentLocation();
        org.hamcrest.MatcherAssert.assertThat(actual.get(), not(IsEmptyCollection.empty()));

    }

    @Test
    public void getPhoto() {
    }

    @Test
    public void widenPlaceData() {
    }

    @Test
    public void storeResult() {
    }

    @Test
    public void parseStatusCode() {
    }
}