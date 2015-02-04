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

import java.util.Random;

import org.junit.Assert;

import ch.ri.s7.bean.S7Serializer;
import ch.ri.s7.bean.annotation.S7Variable;
import ch.ri.s7.impl.S7TCPConnection;
import ch.ri.s7.impl.utils.S7Type;

public class BigDBTest
{

	/**
	 * @param args
	 * 
	 * Instructions:
	 * 
	 * Create a DB100 with 1024 bytes in it
	 * 
	 */
	public static void main(String[] args) throws Exception
	{
		S7TCPConnection c = new S7TCPConnection("10.0.0.220");
		
		S7Serializer s = new S7Serializer(c);
		
		DB out = new DB();
		
		//Fill with random data
		Random r = new Random();
		for (int i=0; i<out.bytes.length; i++)
			out.bytes[i] = (byte)r.nextInt(256);

		//Send
		long storeBegin = System.currentTimeMillis();
		s.store(out, 100, 0);
		long storeEnd = System.currentTimeMillis();
		
		//Receive
		long dispenseBegin = System.currentTimeMillis();
		DB in = s.dispense(DB.class, 100, 0);
		long dispenseEnd = System.currentTimeMillis();
		
		c.close();
		
		Assert.assertArrayEquals(out.bytes, in.bytes);
		
		System.out.println("OK:");
		System.out.println("Storing took: " + ( storeEnd - storeBegin ) + " ms");
		System.out.println("Dispensing took: " + (dispenseEnd - dispenseBegin) + " ms");
	}
	
	public static class DB
	{
		@S7Variable(type=S7Type.BYTE, byteOffset=0, arraySize=4096)
		public Byte[] bytes = new Byte[4096];
	}

}
