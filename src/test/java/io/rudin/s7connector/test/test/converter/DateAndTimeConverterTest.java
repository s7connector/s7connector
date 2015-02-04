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

import io.rudin.s7connector.converter.impl.DateAndTimeConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class DateAndTimeConverterTest
{
	//18, 1,16,16, 5,80,0,3, (dec)
	//12, 1,10,10, 5,50,0,3, (hex)
	//12-01-10 10:05:50.000

	@Test
	public void putTest()
	{
		DateAndTimeConverter c = new DateAndTimeConverter();
		byte[] buffer = new byte[8];
		c.putToPLC(buffer, 0, (byte) 0x10);
		Assert.assertEquals(0x16, buffer[0]);
	}

	@Test
	public void getTest()
	{
		DateAndTimeConverter c = new DateAndTimeConverter();
		byte[] buffer = new byte[8];
		buffer[0] = 0x16;
		byte ret = c.getFromPLC(buffer, 0);
		Assert.assertEquals(0x10, ret);
	}

	@Test
	public void putGetTest()
	{
		for (int i=0; i<100; i++)
		{
			putGetLoop( (byte)i );
		}
	}

	private void putGetLoop(byte b)
	{
		DateAndTimeConverter c = new DateAndTimeConverter();
		byte[] buffer = new byte[8];

		c.putToPLC(buffer, 0, b);

		byte ret = c.getFromPLC(buffer, 0);

		Assert.assertEquals(b, ret);
	}

	@Test
	public void loop()
	{
		DateAndTimeConverter c = new DateAndTimeConverter();
		byte[] buffer = new byte[8];

		Random random = new Random();
		
		for (int i=0; i<50; i++)
		{
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.YEAR, random.nextInt(50) + 1991);
			calendar.set(Calendar.MONTH, random.nextInt(12));
			calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(30) + 1);
			calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(23));
			calendar.set(Calendar.MINUTE, random.nextInt(60));
			calendar.set(Calendar.SECOND, random.nextInt(60));
			calendar.set(Calendar.MILLISECOND, 0);
			Date d = calendar.getTime();
			
			c.insert(d, buffer, 0, 0, 8);

			Date dout = c.extract(Date.class, buffer, 0, 0);

			System.out.println("expected: " + d.getTime());
			System.out.println("actual:   " + dout.getTime());

			Assert.assertEquals(d, dout);
			Assert.assertEquals(d.getTime(), dout.getTime());
		}
	}

	@Test
	public void extract1()
	{
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

}
