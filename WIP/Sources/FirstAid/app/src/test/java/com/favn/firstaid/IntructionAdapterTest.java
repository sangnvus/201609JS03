package com.favn.firstaid;

import android.content.Context;
import android.test.mock.MockContext;

import com.favn.firstaid.adapters.InstructionAdapter;
import com.favn.firstaid.commons.Injury;
import com.favn.firstaid.commons.Instruction;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Hung Gia on 12/12/2016.
 */

public class IntructionAdapterTest {
    private InstructionAdapter instructionAdapter;
    private Instruction instruction;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = new MockContext();
        List<Instruction> instructions = new ArrayList<>();
        instruction = new Instruction(1, 1,"content", "explain", "img");
        instructions.add(instruction);
        instructionAdapter = new InstructionAdapter(context, new derivedClass(),instructions, true);
    }

    @Test
    public void testGetItem() {
        assertEquals("đau đầu was expected.", instruction.getStep(),
                ((Instruction) instructionAdapter.getItem(0)).getStep());
    }

    @Test
    public void testGetItemId() {
        assertEquals("Wrong ID.", 1, instructionAdapter.getItemId(0));
    }

    @Test
    public void testGetCount() {
        assertEquals("Injury amount incorrect.", 1, instructionAdapter.getCount());
    }

    class derivedClass implements InstructionAdapter.InformationSending {

        @Override
        public void requestInformationSending() {

        }
    }
}
