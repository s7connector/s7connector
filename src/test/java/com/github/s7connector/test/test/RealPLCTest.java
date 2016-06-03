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
package com.github.s7connector.test.test;

import java.util.Date;

import com.github.s7connector.bean.S7Serializer;
import com.github.s7connector.bean.annotation.S7Variable;
import com.github.s7connector.impl.S7TCPConnection;
import com.github.s7connector.impl.utils.S7Type;

public class RealPLCTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		
		/*
		 * Byte:
		 * Tested: 2012-01-20 OK
		 * 0x05 = 0x05
		 * 0xA5 = 0xA5
		 */
		
		/*
		 * Word:
		 * Tested: 2012-01-20 OK
		 * 0x0001 = 0x00, 0x01
		 * 0x0002 = 0x00, 0x02
		 * 0xAA55 = 0xAA, 0x55
		 * W#16#5A5A = 0x5a, 0x5a
		 */
		
		/*
		 * Double:
		 * Tested: 2012-01-21 OK -> float!
		 * 0.0 = 0,0,0,0
		 * 0.001 = 3a,83,12,6f
		 * 0.002 = 3b,3,12,6f
		 * 0.004 = 3b,83,12,6f
		 * 1.0 = 3f,80,0,0
		 * 1.5 = 3f,c0,0,0
		 * 2.0 = 40,0,0,0
		 * 3.0 = 40,40,0,0
		 * 4.0 = 40,80,0,0
		 * 10.0 = 41,20,0,0
		 * 30.0 = 41,f0,0,0
		 * 100.0 = 42,c8,0,0
		 * 1000.0 = 44,7a,0,0
		 */
		
		/*
		 * S5Time:
		 * S5T#0ms = 0x00, 0x00
		 * S5T#10ms = 0x00, 0x01
		 * S5T#20ms = 0x00, 0x02
		 * S5T#50ms = 0x00, 0x05
		 * S5T#100ms = 0x00, 0x10
		 * S5T#500ms = 0x00, 0x50
		 * S5T#900ms = 0x00, 0x90
		 * S5T#1s0ms = 0x01, 0x00
		 * S5T#10s0ms = 0x11, 0x00
		 * S5T#20s0ms = 0x12, 0x00
		 * S5T#30s0ms = 0x13, 0x00
		 * S5T#1m0ms = 0x16, 0x00
		 * S5T#1m500ms = 0x16, 0x05
		 * S5T#1h = 0x33, 0x60
		 */
		
		/*
		 * Integer:
		 * Tested: 2012-01-20 OK
		 * 0 = 0x00, 0x00
		 * 1 = 0x00, 0x01
		 * 200 = 0x00, 0xc8
		 * -200 = ff,38
		 * 1000 = 3,e8
		 * -1000 = fc,18
		 * -1 = ff,ff
		 * 32000 = 7d,0
		 * -32000 = 83,0
		 */
		
		S7TCPConnection c = new S7TCPConnection("10.0.0.220");
		
		S7Serializer s = new S7Serializer(c);
		
		DB db = new DB();
		db.str = "Hello!";
		db.d = Math.PI;
		db.b1 = true;
		db.b3 = true;
		db.by1 = 0x5A;
		
		Date d = new Date();
		System.out.println(d);
		System.out.println(d.getTime());
		
		db.date1 = d;
		db.date2 = d;
		db.millis = 3600000;
		
		s.store(db, 100, 0);

		System.out.println("OK");
		
		c.close();
	}
	
	public static class DB
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
		public long millis;
		
		@S7Variable(type=S7Type.DATE, byteOffset=44)
		public Date date2;
	}

}
