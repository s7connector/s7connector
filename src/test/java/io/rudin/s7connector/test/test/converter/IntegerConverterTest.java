package io.rudin.s7connector.test.test.converter;

import io.rudin.s7connector.converter.impl.IntegerConverter;

import org.junit.Assert;
import org.junit.Test;

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
