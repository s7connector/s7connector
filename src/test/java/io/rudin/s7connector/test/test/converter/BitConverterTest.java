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


import io.rudin.s7connector.converter.impl.BitConverter;

import org.junit.Assert;
import org.junit.Test;

public class BitConverterTest
{
	
	@Test
	public void insert1()
	{
		BitConverter c = new BitConverter();
		byte[] buffer = new byte[] { 0x55 };
		c.insert(true, buffer, 0, 0, 1);
		Assert.assertEquals( (int)(0x55 | 0x01), (int)buffer[0]);
	}

	@Test
	public void insert2()
	{
		BitConverter c = new BitConverter();
		byte[] buffer = new byte[] { 0x55 };
		c.insert(true, buffer, 0, 1, 1);
		Assert.assertEquals( (int)(0x55 | (0x01<<1)), (int)buffer[0]);
	}

	@Test
	public void insert3()
	{
		BitConverter c = new BitConverter();
		byte[] buffer = new byte[] { 0x55, (byte)0xAA };
		c.insert(true, buffer, 1, 1, 1);
		Assert.assertEquals( (int)(0x55), (int)buffer[0]);
		Assert.assertEquals( (int)((byte)0xAA | (0x01<<1)), (int)buffer[1]);
	}

	@Test
	public void extract1()
	{
		BitConverter c = new BitConverter();
		byte[] buffer = new byte[] {0x01};
		boolean b = c.extract(Boolean.class, buffer, 0, 0);
		Assert.assertTrue(b);
	}
}
