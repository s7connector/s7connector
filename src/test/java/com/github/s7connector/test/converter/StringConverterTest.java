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

import com.github.s7connector.impl.serializer.converter.StringConverter;

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
