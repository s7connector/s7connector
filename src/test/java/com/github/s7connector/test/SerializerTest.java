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
package com.github.s7connector.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.github.s7connector.api.S7Serializer;
import com.github.s7connector.api.annotation.Datablock;
import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.api.factory.S7SerializerFactory;
import com.github.s7connector.impl.utils.S7Type;
import com.github.s7connector.test.connector.EchoConnector;

public class SerializerTest {

	private static final Random RNG = new Random();

	
	@Test
	public void test() {
		EchoConnector c = new EchoConnector();
		
		S7Serializer p = S7SerializerFactory.buildSerializer(c);

		/*
		 * first level
		 */
		
		MyDB in = new MyDB();
		in.d = Math.PI;
		in.str = "Hello World";
		in.b1 = true;
		in.b3 = true;
		in.by1 = 0x5A;
		in.myLong = 12345L;
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2010, 10, 20, 9, 15, 30);
		calendar.set(Calendar.MILLISECOND, 0);
		in.date1 = calendar.getTime();

		calendar.clear();
		calendar.set(1991, 10, 20, 9, 15, 30);
		calendar.set(Calendar.MILLISECOND, 0);
		in.date2 = calendar.getTime();

		calendar.clear();
		calendar.set(2010, 10, 20);
		in.simpleDate1 = calendar.getTime();
		
		in.timeInMillis = 322222134;
		
		for (int i=0; i<in.byteArray.length; i++)
			in.byteArray[i] = (byte)RNG.nextInt(256);
		
		in.byteArray2 = new Byte[10];
		for (int i=0; i<in.byteArray2.length; i++)
			in.byteArray2[i] = (byte)RNG.nextInt(256);
		

		
		/*
		 * second level
		 */
		
		in.s = new SubStruct();
		in.s.subByte = 0x11;
		
		/*
		 * third level
		 */
		
		in.s.subStruct = new SubSubStruct();
		in.s.subStruct.subBytes = new Byte[10];
		
		for (int i=0; i<in.s.subStruct.subBytes.length; i++)
			in.s.subStruct.subBytes[i] = (byte)RNG.nextInt(256);

		in.str2 = "Test @ end of db";

		in.s.subStruct.deepString = "Very deep string ;)";
		
		/*
		 * EOL
		 */
		
		p.store(in, 0, 0);
		
		MyDB out = p.dispense(MyDB.class, 0, 0);
		
		Assert.assertEquals(in.d, out.d, 0.0001);
		Assert.assertEquals(in.str, out.str);
		Assert.assertEquals(in.str2, out.str2);
		Assert.assertEquals(in.s.subStruct.deepString, out.s.subStruct.deepString);
		Assert.assertEquals(in.b1, out.b1);
		Assert.assertEquals(in.b2, out.b2);
		Assert.assertEquals(in.b3, out.b3);
		Assert.assertEquals(in.by1, out.by1);
		Assert.assertEquals(in.myLong, out.myLong);
		Assert.assertEquals(in.simpleDate1, out.simpleDate1);
		Assert.assertEquals(in.date1, out.date1);
		Assert.assertEquals(in.date2, out.date2);
		Assert.assertEquals(in.timeInMillis, out.timeInMillis);
		Assert.assertEquals(in.s.subByte, out.s.subByte);
		Assert.assertEquals(in.s.subStruct.deepString, out.s.subStruct.deepString);
		
		for (int i=0; i<in.s.subStruct.subBytes.length; i++)
			Assert.assertEquals( in.s.subStruct.subBytes[i], out.s.subStruct.subBytes[i] );
		for (int i=0; i<in.byteArray.length; i++)
			Assert.assertEquals( in.byteArray[i], out.byteArray[i] );
		for (int i=0; i<in.byteArray2.length; i++)
			Assert.assertEquals( in.byteArray2[i], out.byteArray2[i] );
	}
	
	@Datablock
	public static class MyDB
	{
		@S7Variable(type=S7Type.REAL, byteOffset=0)
		public double d;
		
		@S7Variable(type=S7Type.STRING, byteOffset=4, size=20)
		public String str;
		
		@S7Variable(type=S7Type.BOOL, byteOffset=30, bitOffset=0)
		public boolean b1;

		@S7Variable(type=S7Type.BOOL, byteOffset=30, bitOffset=1)
		public boolean b2;

		@S7Variable(type=S7Type.BOOL, byteOffset=30, bitOffset=2)
		public boolean b3;

		@S7Variable(type=S7Type.BYTE, byteOffset=31)
		public byte by1;

		@S7Variable(type=S7Type.DATE_AND_TIME, byteOffset=32)
		public Date date1;
		
		@S7Variable(type=S7Type.TIME, byteOffset=40)
		public long timeInMillis;
		
		@S7Variable(type=S7Type.DATE, byteOffset=44)
		public Date simpleDate1;

		@S7Variable(type=S7Type.DATE_AND_TIME, byteOffset=46)
		public Date date2;
		
		/**
		 * Initialized array
		 */
		@S7Variable(type=S7Type.BYTE, byteOffset=54, arraySize=10)
		public Byte[] byteArray = new Byte[10];
		
		/**
		 * not initialized array
		 */
		@S7Variable(type=S7Type.BYTE, byteOffset=64, arraySize=10)
		public Byte[] byteArray2;
		
		@S7Variable(type=S7Type.STRING, byteOffset=74, size=20)
		public String str2;
		
		@S7Variable(type=S7Type.DWORD, byteOffset=94)
		public Long myLong;

		@S7Variable(type=S7Type.STRUCT, byteOffset=98)
		public SubStruct s;
		
	}
	
	@Datablock
	public static class SubStruct
	{
		@S7Variable(type=S7Type.BYTE, byteOffset=0)
		public byte subByte;
		
		@S7Variable(type=S7Type.STRUCT, byteOffset=1)
		public SubSubStruct subStruct;
	}
	
	@Datablock
	public static class SubSubStruct
	{
		@S7Variable(type=S7Type.BYTE, byteOffset=0, arraySize=10)
		public Byte[] subBytes;

		@S7Variable(type=S7Type.STRING, byteOffset=10, size=20)
		public String deepString;
	}

}
