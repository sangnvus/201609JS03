package com.favn.firstaid;

import android.content.Context;
import android.test.mock.MockContext;

import com.favn.firstaid.adapters.InjuryAdapter;
import com.favn.firstaid.commons.Injury;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Hung Gia on 12/12/2016.
 */

public class InjuryAdapterTest {
    private InjuryAdapter injuryAdapterAdapter;
    private Injury injury;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = new MockContext();
        List<Injury> injuries = new ArrayList<>();
        injury = new Injury(1, "đau đầu", "đau đầu", "img_1");
        injuries.add(injury);
        injuryAdapterAdapter = new InjuryAdapter(context, injuries, 1);
    }

    @Test
    public void testGetItem() {
        assertEquals("đau đầu was expected.", injury.getInjury_name(),
                ((Injury) injuryAdapterAdapter.getItem(0)).getInjury_name());
    }

    @Test
    public void testGetItemId() {
        assertEquals("Wrong ID.", 1, injuryAdapterAdapter.getItemId(0));
    }

    @Test
    public void testGetCount() {
        assertEquals("Injury amount incorrect.", 1, injuryAdapterAdapter.getCount());
    }
}
