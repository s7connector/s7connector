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

import com.github.s7connector.api.S7Type;
import com.github.s7connector.exception.S7Exception;

import java.util.Calendar;
import java.util.Date;

public final class DateAndTimeConverter extends ByteConverter {

    public static final int OFFSET_DAY = 2;
    public static final int OFFSET_HOUR = 3;
    public static final int OFFSET_MILLIS_1_AND_DOW = 7;
    public static final int OFFSET_MILLIS_100_10 = 6;
    public static final int OFFSET_MINUTE = 4;
    public static final int OFFSET_MONTH = 1;
    public static final int OFFSET_SECOND = 5;
    public static final int OFFSET_YEAR = 0;

    // 18, 1,16,16, 5,80,0,3, (dec)
    // 12, 1,10,10, 5,50,0,3, (hex)
    // 12-01-10 10:05:50.000

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T extract(final Class<T> targetClass, final byte[] buffer, final int byteOffset, final int bitOffset) {
        final Calendar c = Calendar.getInstance();
        c.clear();

        int year = this.getValueFromBCD(buffer, OFFSET_YEAR + byteOffset);

        if (year < 90) {
            // 1900 - 1989
            year += 2000;
        } else {
            // 2000 - 2090
            year += 1900;
        }

        int month = this.getValueFromBCD(buffer, OFFSET_MONTH + byteOffset);

        if (month > 0) {
            month--;
        }

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, this.getValueFromBCD(buffer, OFFSET_DAY + byteOffset));
        c.set(Calendar.HOUR_OF_DAY, this.getValueFromBCD(buffer, OFFSET_HOUR + byteOffset));
        c.set(Calendar.MINUTE, this.getValueFromBCD(buffer, OFFSET_MINUTE + byteOffset));
        c.set(Calendar.SECOND, this.getValueFromBCD(buffer, OFFSET_SECOND + byteOffset));

        final byte upperMillis = super.extract(Byte.class, buffer, OFFSET_MILLIS_100_10 + byteOffset, bitOffset);
        final byte lowerMillis = super.extract(Byte.class, buffer, OFFSET_MILLIS_1_AND_DOW + byteOffset, bitOffset);
        c.set(Calendar.MILLISECOND, (100 * (upperMillis >> 4)) + (10 * (upperMillis & 0x0F)) + (lowerMillis >> 4));
        // dayOfWeek is ignored by Calendar.selectField()'s implementation thus we save the time to set it.
        // Would have been: c.set(Calendar.DAY_OF_WEEK, (lowerMillis & 0x0F))

        return targetClass.cast(c.getTime());
    }

    /**
     * Dec -> Hex 10 = 0a 16 = 0f 17 = 10
     *
     * @param buffer buffer read from PLC
     * @param offset offset in buffer
     * @return value of BCD encoded number
     */
    byte getValueFromBCD(final byte[] buffer, final int offset) {
        final byte ret = super.extract(Byte.class, buffer, offset, 0);
        return (byte) (((byte) ((ret >> 4) & 0x0F) * 10) + (ret & 0x0F));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S7Type getS7Type() {
        return S7Type.DATE_AND_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSizeInBits() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSizeInBytes() {
        return 8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(final Object javaType, final byte[] buffer, final int byteOffset, final int bitOffset,
                       final int size) {
        final Date date = (Date) javaType;
        final Calendar c = Calendar.getInstance();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);


        if (year < 1990 || year > 2089) {
            throw new S7Exception("Invalid year: " + year + " @ offset: " + byteOffset);
        }

        if (year < 2000) {
            // 1990 -1999
            year -= 1900;
        } else {
            // 2000 - 2089
            year -= 2000;
        }

        this.putAsBCD(buffer, byteOffset + OFFSET_YEAR, year);
        this.putAsBCD(buffer, byteOffset + OFFSET_MONTH, c.get(Calendar.MONTH) + 1);
        this.putAsBCD(buffer, byteOffset + OFFSET_DAY, c.get(Calendar.DAY_OF_MONTH));
        this.putAsBCD(buffer, byteOffset + OFFSET_HOUR, c.get(Calendar.HOUR_OF_DAY));
        this.putAsBCD(buffer, byteOffset + OFFSET_MINUTE, c.get(Calendar.MINUTE));
        this.putAsBCD(buffer, byteOffset + OFFSET_SECOND, c.get(Calendar.SECOND));

        final int millis = c.get(Calendar.MILLISECOND);
        final int msec1 = millis % 10;
        final int msec10 = millis % 100 / 10;
        final int msec100 = millis / 100;
        final int dow = c.get(Calendar.DAY_OF_WEEK);
        super.insert((byte) ((byte) msec10 | (byte) (msec100 << 4)), buffer, OFFSET_MILLIS_100_10 + byteOffset, 0, 1);
        super.insert((byte) ((byte) dow | (byte) (msec1 << 4)), buffer, OFFSET_MILLIS_1_AND_DOW + byteOffset, 0, 1);
    }

    /**
     * Hex -> dec: 0a = 10 0f = 16 10 = 17.
     * <p>
     * Note: 1 byte has 2 nibbles in BCD, allowing for decimal values from 0 to 99 represented.
     *
     * @param buffer       write buffer for sending to PLC
     * @param offset       offset in buffer
     * @param decimalValue value to write as BCD
     */
    void putAsBCD(final byte[] buffer, final int offset, final int decimalValue) {
        if (decimalValue < 0 || decimalValue > 99) {
            throw new S7Exception("can not be stored in 1-byte-BCD: " + decimalValue);
        }
        buffer[offset] = (byte) ((decimalValue / 10) << 4 | (decimalValue % 10));
    }

}
