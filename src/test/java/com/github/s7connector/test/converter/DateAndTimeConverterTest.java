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
package com.github.s7connector.test.converter;

import com.github.s7connector.impl.serializer.converter.DateAndTimeConverter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThrows;

public class DateAndTimeConverterTest {
    /**
     * Utility method to create Date instance without deprecation warnings.
     */
    private static Date getDateAt(int year, int month, int dayOfMonth, int hourOfDay, int minute, int seconds, int millis) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, millis);
        return calendar.getTime();
    }


    //18, 1,16,16, 5,80,0,3, (dec)
    //12, 1,10,10, 5,50,0,3, (hex)
    //12-01-10 10:05:50.000

    @Test
    public void putTest() {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];
        c.putToPLC(buffer, 0, (byte) 0x10);
        Assert.assertEquals(0x16, buffer[0]);
    }

    @Test
    public void getTest() {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];
        buffer[0] = 0x16;
        byte ret = c.getFromPLC(buffer, 0);
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

        c.putToPLC(buffer, 0, b);

        byte ret = c.getFromPLC(buffer, 0);

        Assert.assertEquals(b, ret);
    }

    @Test
    public void loop() {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];

        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            Date d = getDateAt(
                    random.nextInt(50) + 1991, random.nextInt(12), random.nextInt(30) + 1,
                    random.nextInt(23), random.nextInt(60), random.nextInt(60), 0);

            c.insert(d, buffer, 0, 0, 8);

            Date dout = c.extract(Date.class, buffer, 0, 0);

            System.out.println("expected: " + d.getTime());
            System.out.println("actual:   " + dout.getTime());

            Assert.assertEquals(d, dout);
            Assert.assertEquals(d.getTime(), dout.getTime());
        }
    }

    @Test
    public void extract1() {
        DateAndTimeConverter c = new DateAndTimeConverter();
        byte[] buffer = new byte[8];

        Date d = c.extract(Date.class, buffer, 0, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, 1999);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
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
        final byte[] buffer = new byte[]{0x25, 0x08, 0x13, 0x11, 0x24, 0x01, 0x68, 0x64};
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
}
