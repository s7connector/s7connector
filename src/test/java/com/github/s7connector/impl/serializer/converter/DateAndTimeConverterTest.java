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
package com.github.s7connector.impl.serializer.converter;

import com.github.s7connector.exception.S7Exception;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThrows;

public class DateAndTimeConverterTest {
    private static final Logger log = LoggerFactory.getLogger(DateAndTimeConverterTest.class);

    /**
     * Utility method to create Date instance without deprecation warnings.
     */
    private static Date getDateAt(int year, int month, int dayOfMonth, int hourOfDay, int minute, int seconds, int millis) {
        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, seconds);
            calendar.set(Calendar.MILLISECOND, millis);
            return calendar.getTime();
        } catch (IllegalArgumentException e) {
            log.error("getDateAt({},{},{},{},{},{},{}) is invalid",
                    year, month, dayOfMonth, hourOfDay, minute, seconds, millis);
            throw e;
        }
    }


    //18, 1,16,16, 5,80,0,3, (dec)
    //12, 1,10,10, 5,50,0,3, (hex)
    //12-01-10 10:05:50.000

    @Test
    public void putTest() {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];
        c.putAsBCD(buffer, 0, (byte) 0x10);
        Assert.assertEquals(0x16, buffer[0]);
    }

    @Test
    public void getTest() {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];
        buffer[0] = 0x16;
        byte ret = c.getValueFromBCD(buffer, 0);
        Assert.assertEquals(0x10, ret);
    }

    @Test
    public void putGetTest() {
        for (int i = 0; i < 100; i++) {
            putGetLoop((byte) i);
        }
    }

    private void putGetLoop(byte b) {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];

        c.putAsBCD(buffer, 0, b);
        byte ret = c.getValueFromBCD(buffer, 0);

        Assert.assertEquals(b, ret);
    }

    @Test
    public void loop() {
        // seed 195 produces the 1999-02-28 which does not exist (dayOfMonth from 1..28)
        executeLoop(new Random(195));
        // seed 175 produces 2011-02-27 02:08:52.927, which also triggers IllegalArgumentException: HOUR_OF_DAY: 2 -> 3 (dayOfMonth from 1..27)
        executeLoop(new Random(175));
        for (int i = 0; i < 256; ++i) {
            executeLoop(new Random(i));
        }
        executeLoop(new Random());
    }

    private void executeLoop(Random random) {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];

        for (int i = 0; i < 50; i++) {
            final int millis = random.nextInt(1000);
            Date d = getDateAt(
                    random.nextInt(50) + 1991, random.nextInt(12), random.nextInt(26) + 1,
                    random.nextInt(24), random.nextInt(60), random.nextInt(60), millis);

            c.insert(d, buffer, 0, 0, 8);

            Date dout = c.extract(Date.class, buffer, 0, 0);

            Assert.assertEquals(d, dout);
            Assert.assertEquals(d.getTime(), dout.getTime());
        }
    }

    @Test
    public void extract1() {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[]{0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00};

        Date d = c.extract(Date.class, buffer, 0, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        Assert.assertEquals(calendar.getTime(), d);
    }

    /**
     * From <a href="https://support.industry.siemens.com/cs/mdm/109773506?c=85672757259&lc=en-DE">DateTime format spec</a>:<ul>
     * <li>Byte 6:  The two most significant digits of MSEC (BCD#0 to BCD#999)</li>
     * <li>Byte 7 (4MSB)): The least significant digit of MSEC (BCD#0 to BCD#9)</li>
     * <li>Byte 7 (4LSB)): Weekday (BCD#1 to BCD#7, BCD#1 = Sunday ... BCD#7 = Saturday)</li>
     * </ul>
     */
    @Test
    public void millisecondsGet() {
        final DateAndTimeConverter c = new DateAndTimeConverter();
        // buffer data is BCD-encoded by PLC
        final byte[] buffer = new byte[]{0x25/*year*/, 0x08/*month*/, 0x13/*day*/,
                0x11/*hour*/, 0x24/*minute*/, 0x01/*second*/,
                0x68/*millis MSB*/, 0x64/*millis 4-bit-lsb; dayOfWeek*/};
        assertEquals(8, buffer.length);
        final Date result = c.extract(Date.class, buffer, 0, 0);
        assertEquals("actual millis=" + result.getTime() % 1000 + ", expected=" + (6 * 100 + 8 * 10 + 6),
                getDateAt(2025, Calendar.AUGUST, 13, 11, 24, 1, 686),
                result);
    }

    @Test
    public void millisecondsGetAndPut() {
        final DateAndTimeConverter c = new DateAndTimeConverter();
        final byte[] buffer = new byte[8];
        final Date testDate = getDateAt(2025, 8, 14, 1, 12, 9, 197);
        c.insert(testDate, buffer, 0, 0, buffer.length);
        final Date extractResult = c.extract(Date.class, buffer, 0, 0);
        assertEquals("actual millis=" + extractResult.getTime() % 1000, testDate, extractResult);
    }

    @Test
    public void bufferTooShortForInsert() {
        final DateAndTimeConverter c = new DateAndTimeConverter();
        final byte[] buffer = new byte[7];
        final Date testDate = getDateAt(2025, 8, 14, 1, 12, 9, 197);
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> c.insert(testDate, buffer, 0, 0, buffer.length));
    }

    @Test
    public void yearOutOfOfRange() {
        final DateAndTimeConverter c = new DateAndTimeConverter();
        final byte[] buffer = new byte[8];
        final Date valueFuture = getDateAt(2090, Calendar.JANUARY, 1, 1, 1, 1, 1);
        assertThrows(S7Exception.class, () -> c.insert(valueFuture, buffer, 0, 0, buffer.length));
        final Date valuePast = getDateAt(1989, Calendar.DECEMBER, 31, 23, 59, 59, 999);
        assertThrows(S7Exception.class, () -> c.insert(valuePast, buffer, 0, 0, buffer.length));
    }
}
