package com.favn.firstaid;


import android.content.Context;
import android.test.mock.MockContext;

import com.favn.firstaid.adapters.HealthFacilityAdapter;
import com.favn.firstaid.models.HealthFacility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Hung Gia on 12/5/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)

public class HealthFacilityAdapterTest {
    private HealthFacilityAdapter healthFacilityAdapter;
    private HealthFacility hospital;
    private HealthFacility medicineCenter;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = new MockContext();
        List<HealthFacility> healthFacilities = new ArrayList<>();
        hospital = new HealthFacility(1, "Bệnh viện Đa Khoa", 1, "Hà Nội", "Hà Nội", null, 21.011208, 105.525544);
        medicineCenter = new HealthFacility(2, "Trạm y tế", 2, "Hà Nội", "Hà Nội", null, 22.011208, 106.525544);
        healthFacilities.add(hospital);
        healthFacilities.add(medicineCenter);
        healthFacilityAdapter = new HealthFacilityAdapter(context, healthFacilities);
    }

    @Test
    public void testGetItem() {
        assertEquals("Bệnh viện Đa Khoa was expected.", hospital.getName(),
                ((HealthFacility) healthFacilityAdapter.getItem(0)).getName());
    }

    @Test
    public void testGetItemId() {
        assertEquals("Wrong ID.", 0, healthFacilityAdapter.getItemId(0));
    }

    @Test
    public void testGetCount() {
        assertEquals("Health Facility amount incorrect.", 2, healthFacilityAdapter.getCount());
    }

}
