package com.favn.firstaid;

import com.favn.firstaid.utils.StringConverter;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hung Gia on 12/5/2016.
 */


public class StringUtilsTest {

    @Test
    public void isConvertCorrect() throws Exception {
        String test = StringConverter.unAccent("Sơ cứu");
        assertEquals("So cuu", test);
    }
}
