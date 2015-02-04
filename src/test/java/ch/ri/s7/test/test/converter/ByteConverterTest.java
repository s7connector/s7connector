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
package ch.ri.s7.test.test.converter;


import org.junit.Assert;
import org.junit.Test;

import ch.ri.s7.converter.impl.ByteConverter;

public class ByteConverterTest
{
	
	@Test
	public void insert1()
	{
		ByteConverter c = new ByteConverter();
		byte[] buffer = new byte[2];
		c.insert((byte)55, buffer, 0, 0, 1);
		Assert.assertEquals( 55, (int)buffer[0]);
		Assert.assertEquals( 0x00, buffer[1]);
	}

	@Test
	public void insert2()
	{
		ByteConverter c = new ByteConverter();
		byte[] buffer = new byte[2];
		c.insert((byte)55, buffer, 1, 0, 1);
		Assert.assertEquals( 0x00, buffer[0]);
		Assert.assertEquals( 55, (int)buffer[1]);
	}
	
	@Test
	public void extract1()
	{
		ByteConverter c = new ByteConverter();
		byte[] buffer = new byte[]{ 0x55, 0x00 };
		byte b = (Byte)c.extract(Byte.class, buffer, 0, 0);
		Assert.assertEquals( 0x55, (byte)b);
	}

	@Test
	public void extract2()
	{
		ByteConverter c = new ByteConverter();
		byte[] buffer = new byte[]{ 0x55, 0x44 };
		Byte b = (Byte)c.extract(Byte.class, buffer, 1, 0);
		Assert.assertEquals( 0x44, (byte)b);
	}

}
