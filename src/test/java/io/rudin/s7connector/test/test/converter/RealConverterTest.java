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

import io.rudin.s7connector.converter.impl.RealConverter;
import io.rudin.s7connector.test.test.converter.base.ConverterBase;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class RealConverterTest extends ConverterBase
{
	@Test
	public void insert1()
	{
		RealConverter c = new RealConverter();
		byte[] buffer = new byte[4];
		c.insert(3.141f, buffer, 0, 0, 4);
		dump(buffer);
	}
	
	@Test
	public void loopTest1()
	{
		Random r = new Random();
		for (int i=0; i<1000; i++)
			loop(r.nextFloat() * r.nextInt(1000000));
	}
	
	public void loop(float f)
	{
		System.out.println("Testing: " + f);
		
		RealConverter c = new RealConverter();
		byte[] buffer = new byte[4];
		c.insert(f, buffer, 0, 0, 4);

		float ret = c.extract(Float.class, buffer, 0, 0);
		
		Assert.assertEquals(f, ret, 0.1);
	}

}
