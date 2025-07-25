/*
Copyright 2016 S7connector members (github.com/s7connector)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.github.s7connector.test;

import com.github.s7connector.api.S7Serializer;
import com.github.s7connector.api.S7Type;
import com.github.s7connector.api.annotation.Datablock;
import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.api.factory.S7SerializerFactory;
import com.github.s7connector.test.connector.EchoConnector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerializerStructArrayTest {
    private static final int SUBSTRUCTURE_REPETITION = 5;
    private static final long EPOCH_YEAR2000_MILLIS = (2000 - 1970) * 365 * 24 * 60 * 60 * 1000L;

    @Test
    public void test() {
        final EchoConnector c = new EchoConnector();

        final S7Serializer p = S7SerializerFactory.buildSerializer(c);

        // fixture
        final StructDB in = new StructDB();
        in.byte1 = 0x01;
        in.byteArray[0] = 0;
        in.byteArray[1] = 1;
        in.byteArray[2] = 2;
        in.subStruct = new SubStruct[SUBSTRUCTURE_REPETITION];
        for (int i = 0; i < SUBSTRUCTURE_REPETITION; ++i) {
            in.subStruct[i] = new SubStruct();
            in.subStruct[i].subByte = (byte) (10 + i);
        }

        // execute test
        p.store(in, 0, 0);
        Assert.assertEquals(
                Arrays.asList((byte) 1, (byte) 0, (byte) 1, (byte) 2, (byte) 10, (byte) 11, (byte) 12, (byte) 13, (byte) 14),
                IntStream.range(0, c.buffer.length).mapToObj(i -> c.buffer[i]).collect(Collectors.toList()));

        // check results
        final StructDB out = p.dispense(StructDB.class, 0, 0);
        Assert.assertEquals(in.byte1, out.byte1);
        Assert.assertArrayEquals(in.byteArray, out.byteArray);
        Assert.assertEquals(in.subStruct.length, out.subStruct.length);
        for (int i = 0; i < SUBSTRUCTURE_REPETITION; ++i) {
            Assert.assertEquals("subStruct[" + i + "]", in.subStruct[i].subByte, out.subStruct[i].subByte);
        }

        // execute same test on same connector instance
        testTopLevelArrayStruct(p);
    }

    private void testTopLevelArrayStruct(S7Serializer p) {
        final ArrayStructDB in = new ArrayStructDB();
        in.listOfStruct = new ArraySubStruct[3];
        in.listOfStruct[0] = new ArraySubStruct("txt0", new Date(1_000_000 + EPOCH_YEAR2000_MILLIS));
        in.listOfStruct[1] = new ArraySubStruct("txt1", new Date(2_000_000 + EPOCH_YEAR2000_MILLIS));
        in.listOfStruct[2] = new ArraySubStruct("txt2", new Date(3_000_000 + EPOCH_YEAR2000_MILLIS));
        p.store(in, 0, 0);
        final ArrayStructDB out = p.dispense(ArrayStructDB.class, 0, 0);
        Assert.assertEquals(in.listOfStruct.length, out.listOfStruct.length);
        for (int i = 0; i < in.listOfStruct.length; ++i) {
            Assert.assertEquals(in.listOfStruct[i].str, out.listOfStruct[i].str);
            Assert.assertEquals(in.listOfStruct[i].date, out.listOfStruct[i].date);
        }
    }

    @Test
    public void testTopLevelArrayStruct() {
        testTopLevelArrayStruct(S7SerializerFactory.buildSerializer(new EchoConnector()));
    }

    @Datablock
    public static class StructDB {
        @S7Variable(type = S7Type.BYTE, byteOffset = 0)
        public byte byte1;

        /**
         * Initialized array
         */
        @S7Variable(type = S7Type.BYTE, byteOffset = 1, arraySize = 3)
        public byte[] byteArray = new byte[3];

        @S7Variable(type = S7Type.STRUCT, byteOffset = 1 + 3, arraySize = SUBSTRUCTURE_REPETITION)
        public SubStruct[] subStruct;
    }

    public static class SubStruct {
        @S7Variable(type = S7Type.BYTE, byteOffset = 0)
        public byte subByte;
    }

    @Datablock
    public static class ArrayStructDB {
        @S7Variable(type = S7Type.STRUCT, arraySize = 3, byteOffset = 0)
        public ArraySubStruct[] listOfStruct;
    }

    public static class ArraySubStruct {
        @S7Variable(type = S7Type.STRING, size = 4, byteOffset = 0)
        public String str;
        // note: String has 2 bytes overhead (for max length and actual length)
        @S7Variable(type = S7Type.DATE_AND_TIME, byteOffset = 4 + 2)
        public Date date;

        public ArraySubStruct() {
        }

        public ArraySubStruct(String s, Date d) {
            str = s;
            date = d;
        }
    }
}
