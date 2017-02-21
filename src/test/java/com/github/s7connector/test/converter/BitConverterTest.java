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
package com.github.s7connector.test.converter;


import org.junit.Assert;
import org.junit.Test;

import com.github.s7connector.impl.serializer.converter.BitConverter;

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
