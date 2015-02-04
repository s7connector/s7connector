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

import ch.ri.s7.converter.impl.StringConverter;

public class StringConverterTest
{
	
	@Test
	public void insert1()
	{
		StringConverter c = new StringConverter();
		byte[] buffer = new byte[10];
		c.insert("Hello", buffer, 0, 0, 8);
		byte[] expected = new byte[]{ 8, 5, 'H', 'e', 'l', 'l', 'o', 0, 0, 0 };
		Assert.assertArrayEquals(expected, buffer);
	}

	@Test
	public void insert2()
	{
		StringConverter c = new StringConverter();
		byte[] buffer = new byte[12];
		c.insert("Hello", buffer, 2, 0, 8);
		byte[] expected = new byte[]{ 0, 0, 8, 5, 'H', 'e', 'l', 'l', 'o', 0, 0, 0};
		Assert.assertArrayEquals(expected, buffer);
	}
	
	@Test
	public void extract1()
	{
		StringConverter c = new StringConverter();
		byte[] buffer = new byte[]{ 8, 5, 'H', 'e', 'l', 'l', 'o', 0, 0, 0};
		String str = c.extract(String.class, buffer, 0, 0);
		Assert.assertEquals("Hello", str);
	}

	@Test
	public void extract2()
	{
		StringConverter c = new StringConverter();
		byte[] buffer = new byte[]{ 0, 8, 5, 'H', 'e', 'l', 'l', 'o', 0, 0, 0};
		String str = c.extract(String.class, buffer, 1, 0);
		Assert.assertEquals("Hello", str);
	}

}
