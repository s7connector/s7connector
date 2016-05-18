package io.rudin.s7connector.test.test.converter;


import io.rudin.s7connector.converter.impl.ByteConverter;

import org.junit.Assert;
import org.junit.Test;

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
