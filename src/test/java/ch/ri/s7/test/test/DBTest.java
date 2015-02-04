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
package ch.ri.s7.test.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import ch.ri.s7.bean.S7Serializer;
import ch.ri.s7.bean.annotation.Datablock;
import ch.ri.s7.bean.annotation.S7Variable;
import ch.ri.s7.impl.utils.S7Type;
import ch.ri.s7.test.test.connector.DummyConnector;

public class DBTest
{

	
	@Test
	public void test()
	{
		DummyConnector c = new DummyConnector();
		
		S7Serializer p = new S7Serializer(c);

		MyDB in = new MyDB();
		in.d = Math.PI;
		in.str = "Hello World";
		in.b1 = true;
		in.b3 = true;
		in.by1 = 0x5A;
		
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
		
		in.s.subByte = 0x11;
		
		Random r = new Random();
		for (int i=0; i<in.s.subStruct.subBytes.length; i++)
			in.s.subStruct.subBytes[i] = (byte)r.nextInt(256);
		for (int i=0; i<in.byteArray.length; i++)
			in.byteArray[i] = (byte)r.nextInt(256);
		for (int i=0; i<in.byteArray2.length; i++)
			in.byteArray2[i] = (byte)r.nextInt(256);
		
		in.str2 = "Test @ end of db";
		in.s.subStruct.deepString = "Very deep string ;)";
		
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
		Assert.assertEquals(in.simpleDate1, out.simpleDate1);
		Assert.assertEquals(in.date1, out.date1);
		Assert.assertEquals(in.date2, out.date2);
		Assert.assertEquals(in.timeInMillis, out.timeInMillis);
		Assert.assertEquals(in.s.subByte, out.s.subByte);
		
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
		
		@S7Variable(type=S7Type.BYTE, byteOffset=54, arraySize=10)
		public Byte[] byteArray = new Byte[10];
		
		@S7Variable(type=S7Type.BYTE, byteOffset=64, arraySize=10)
		public Byte[] byteArray2 = new Byte[10];
		
		@S7Variable(type=S7Type.STRING, byteOffset=74, size=20)
		public String str2;
		
		@S7Variable(type=S7Type.STRUCT, byteOffset=94)
		public SubStruct s = new SubStruct();
	}
	
	@Datablock
	public static class SubStruct
	{
		@S7Variable(type=S7Type.BYTE, byteOffset=0)
		public byte subByte;
		
		@S7Variable(type=S7Type.STRUCT, byteOffset=1)
		public SubSubStruct subStruct = new SubSubStruct();
	}
	
	@Datablock
	public static class SubSubStruct
	{
		@S7Variable(type=S7Type.BYTE, byteOffset=0, arraySize=10)
		public Byte[] subBytes = new Byte[10];

		@S7Variable(type=S7Type.STRING, byteOffset=10, size=20)
		public String deepString;
	}

}
