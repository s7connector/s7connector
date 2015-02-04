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

import ch.ri.s7.converter.impl.IntegerConverter;

public class IntegerConverterTest
{
	@Test
	public void insert1()
	{
		
		IntegerConverter c = new IntegerConverter();
		byte[] buffer = new byte[] { 0, 0, 0, 0 };
		c.insert(666, buffer, 0, 0, 2);
		for (int i=0; i<buffer.length; i++)
			System.out.print( Integer.toHexString(buffer[i] & 0xFF) + ",");
		
		Assert.assertEquals( 0x02, (byte)buffer[0]);
		Assert.assertEquals( (byte)0x9a, (byte)buffer[1]);
	}

	@Test
	public void insert2()
	{
		IntegerConverter c = new IntegerConverter();
		byte[] buffer = new byte[] { 0, 0, 0, 0 };
		c.insert(666, buffer, 1, 0, 2);
		Assert.assertEquals( 0x00, (byte)buffer[0]);
		Assert.assertEquals( 0x02, (byte)buffer[1]);
		Assert.assertEquals( (byte)0x9a, (byte)buffer[2]);
		Assert.assertEquals( 0x00, (byte)buffer[3]);
	}

	@Test
	public void extract1()
	{
		IntegerConverter c = new IntegerConverter();
		byte[] buffer = new byte[]{ 0x02, (byte)0x9a, 0, 0 };

		for (int i=0; i<buffer.length; i++)
			System.out.print( Integer.toHexString(buffer[i] & 0xFF) + ",");

		int i = c.extract(Integer.class, buffer, 0, 0);
		Assert.assertEquals(666, i);
	}
	
	@Test
	public void extract2()
	{
		IntegerConverter c = new IntegerConverter();
		byte[] buffer = new byte[]{ 0, 0, 0x02, (byte)0x9a, 0, 0 };

		for (int i=0; i<buffer.length; i++)
			System.out.print( Integer.toHexString(buffer[i] & 0xFF) + ",");

		int i = c.extract(Integer.class, buffer, 2, 0);
		Assert.assertEquals(666, i);
	}
	
}
