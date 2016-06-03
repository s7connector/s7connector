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
package com.github.s7connector.test.test.converter;


import org.junit.Assert;
import org.junit.Test;

import com.github.s7connector.converter.impl.ByteConverter;

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
