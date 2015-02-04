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
package io.rudin.s7connector.test.test.converter;

import io.rudin.s7connector.converter.impl.DateConverter;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DateConverterTest
{
	
	@Test
	public void loop()
	{
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.YEAR, 1990);
		c.add(Calendar.DAY_OF_YEAR, 200);

		Date d;
		
		for (int i=0; i<30000; i++)
		{
			c.add(Calendar.DAY_OF_YEAR, 1);
			d = c.getTime();
			doLoopTest(d);
		}
	}
	
	private void doLoopTest(Date d)
	{
		DateConverter c = new DateConverter();
		byte[] buffer = new byte[2];
		
		c.insert(d, buffer, 0, 0, 2);

		Date dout = c.extract(Date.class, buffer, 0, 0);
		
		//System.out.println("Excpected: " + d.getTime() + " actual: " + dout.getTime() + " diff: " + (d.getTime()-dout.getTime()));
		
		Assert.assertEquals(d, dout);
	}
	
	@Test
	public void insert1()
	{
		DateConverter c = new DateConverter();
		byte[] buffer = new byte[2];

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, 1990);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		
		Date d = calendar.getTime();
		
		c.insert(d, buffer, 0, 0, 2);

		Assert.assertEquals(0x00, buffer[0]);
		Assert.assertEquals(0x00, buffer[1]);
	}

	@Test
	public void insert2()
	{
		DateConverter c = new DateConverter();
		byte[] buffer = new byte[2];

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, 1990);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 5);
		
		Date d = calendar.getTime();
		
		c.insert(d, buffer, 0, 0, 2);

		Assert.assertEquals(0x00, buffer[0]);
		Assert.assertEquals(0x04, buffer[1]);
	}
	
	@Test
	public void extract1()
	{
		DateConverter c = new DateConverter();
		byte[] buffer = new byte[2];
		
		Date d = c.extract(Date.class, buffer, 0, 0);

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, 1990);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		
		Assert.assertEquals(calendar.getTime(), d);
	}

}
