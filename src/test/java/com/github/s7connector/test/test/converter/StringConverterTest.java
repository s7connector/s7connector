package com.github.s7connector.test.test.converter;

import org.junit.Assert;
import org.junit.Test;

import com.github.s7connector.converter.impl.StringConverter;

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
