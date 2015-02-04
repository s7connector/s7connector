/*
 Part of S7Connector, a connector for S7 PLC's
 
 (C) Thomas Rudin (thomas@rudin-informatik.ch) 2012.

 S7Connector is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3, or (at your option)
 any later version.

 S7Connector is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Visual; see the file COPYING.  If not, write to
 the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.  
*/
package ch.ri.s7.converter.impl;

import java.util.Calendar;
import java.util.Date;

import ch.ri.s7.impl.utils.S7Type;

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
