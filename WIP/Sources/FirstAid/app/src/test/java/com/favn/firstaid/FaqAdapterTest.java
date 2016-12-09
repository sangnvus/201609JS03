package com.favn.firstaid;

import android.test.mock.MockContext;

import com.favn.firstaid.adapters.FaqAdapter;
import com.favn.firstaid.commons.Faq;

import org.codehaus.plexus.context.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
/**
 * Created by Mikey on 12/5/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)

public class FaqAdapterTest {
    private FaqAdapter faqAdapter;
    private Faq faq;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = (Context) new MockContext();
        List<Faq> faqs = new ArrayList<>();
        faq = new Faq(1, "Câu hỏi", "Câu trả lời");
        faqs.add(faq);
        faqAdapter = new FaqAdapter((android.content.Context) context, faqs);
    }

    @Test
    public void testGetItem() {
        assertEquals("Câu hỏi was expected.", faq.getQuestion(),
                ((Faq) faqAdapter.getItem(0)).getQuestion());
    }

    @Test
    public void testGetItemId() {
        assertEquals("Wrong ID.", 0, faqAdapter.getItemId(0));
    }

    @Test
    public void testGetCount() {
        assertEquals("Faq amount incorrect.", 2, faqAdapter.getCount());
    }
}
