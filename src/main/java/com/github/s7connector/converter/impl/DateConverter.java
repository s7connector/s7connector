/*
Copyright 2016 Thomas Rudin

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
package com.github.s7connector.converter.impl;

import java.util.Calendar;
import java.util.Date;

import com.github.s7connector.impl.utils.S7Type;

public class DateConverter extends IntegerConverter
{

	static
	{
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.YEAR, 1990);
		
		OFFSET_1990 = c.getTime().getTime();
	}
	
	/**
	 * 1.1.1990
	 */
	private static final long OFFSET_1990;
	
	private static final long MILLI_TO_DAY_FACTOR = 24 * 60 * 60 * 1000;
	
	/*
	 * Date in days since 01-01-1990
	 * 
	 */
	
	@Override
	public void insert(Object javaType, byte[] buffer, int byteOffset, int bitOffset, int size)
	{
		Date d = (Date)javaType;
		
		long millis = d.getTime();
		
		millis -= OFFSET_1990;
		
		double days = (double)millis / (double)MILLI_TO_DAY_FACTOR;
		
		final long ROUND = 1000;
		
		long expected = (long) (days*MILLI_TO_DAY_FACTOR/ROUND);
		long actual = millis/ROUND;
		
		if ( expected != actual )
			throw new IllegalArgumentException("Expected: " + expected + " got: " + actual);
		
		if (millis < 0)
			super.insert(0, buffer, byteOffset, bitOffset, 2);
		else
			super.insert( (int)Math.round(days), buffer, byteOffset, bitOffset, 2);
	}

	@Override
	public <T> T extract(Class<T> targetClass, byte[] buffer, int byteOffset, int bitOffset)
	{
		long days = (Integer)super.extract(Integer.class, buffer, byteOffset, bitOffset);
		
		long millis = days * MILLI_TO_DAY_FACTOR;
		
		millis += OFFSET_1990;
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(millis);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		
		return targetClass.cast(c.getTime());
	}

	@Override
	public S7Type getS7Type()
	{
		return S7Type.DATE;
	}

}
