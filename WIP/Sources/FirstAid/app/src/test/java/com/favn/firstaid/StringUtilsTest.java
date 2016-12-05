package com.favn.firstaid;

import com.favn.firstaid.activites.MainActivity;
import com.favn.firstaid.models.Commons.StringUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
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

public class StringUtilsTest {

    @Test
    public void isConvertCorrect() throws Exception {
        String test = StringUtils.unAccent("Sơ cứu");
        assertEquals("So cuu", test);
    }
}
