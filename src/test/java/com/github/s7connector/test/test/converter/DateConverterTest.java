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
package com.github.s7connector.test.test.converter;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.github.s7connector.impl.serializer.converter.DateConverter;

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
