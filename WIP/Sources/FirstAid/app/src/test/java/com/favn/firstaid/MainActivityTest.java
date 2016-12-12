package com.favn.firstaid;

import android.content.Context;
import android.test.mock.MockContext;

import com.favn.firstaid.activites.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by Hung Gia on 12/5/2016.
 */


public class MainActivityTest {
    private MainActivity activity;
    Context context;
    @Test
    public void shouldHaveApplicationName() throws Exception {
        context = new MockContext();

    }


}
