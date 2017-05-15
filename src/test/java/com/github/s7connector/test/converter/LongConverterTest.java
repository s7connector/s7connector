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

import com.github.s7connector.impl.serializer.converter.LongConverter;

public class LongConverterTest {

	@Test
	public void insert1() {		
		LongConverter c = new LongConverter();
		byte[] buffer = new byte[4];
		c.insert(530L, buffer, 0, 0, 0);
		Assert.assertEquals( 0x00, (int)buffer[0]);
		Assert.assertEquals( 0x00, (int)buffer[1]);
		Assert.assertEquals( 0x02, (int)buffer[2]);
		Assert.assertEquals( 0x12, (int)buffer[3]);
	}

	
	@Test
	public void extract1() {
		LongConverter c = new LongConverter();
		byte[] buffer = new byte[]{ 0x00, 0x00, 0x02, 0x12 };
		long l = (Long)c.extract(Long.class, buffer, 0, 0);
		Assert.assertEquals( 530L, l );
	}
	
	@Test
	public void roundTrip(){
		LongConverter c = new LongConverter();
		byte[] buffer = new byte[4];
		
		Long[] longs = new Long[]{
				0L, 1L, -1L,
				(long)Math.pow(2, 16),
				(long)Math.pow(2, 16)*-1
		};
		
		for (long l: longs){
			c.insert(l, buffer, 0, 0, 0);
			long extract = c.extract(Long.class, buffer, 0, 0);
			Assert.assertEquals(l, extract);
		}
		
	}
}
