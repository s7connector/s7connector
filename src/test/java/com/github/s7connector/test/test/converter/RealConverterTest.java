package com.github.s7connector.test.test.converter;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.github.s7connector.converter.impl.RealConverter;
import com.github.s7connector.test.test.converter.base.ConverterBase;

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
