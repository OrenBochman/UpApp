package org.bochman.upapp.api;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;

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

    @Test
    public void findCurrentLocation() {
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