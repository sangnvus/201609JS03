package com.favn.firstaid;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hung Gia on 12/5/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)

public class MapsActivityTest {

    @Test
    public void isTitleCorrect() throws Exception {
        //assertTrue(mapsActivity.getTitle().toString().equals("Khẩn cấp"));
    }
}
