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

import java.util.Random;

import org.junit.Assert;

import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.impl.S7TCPConnection;
import com.github.s7connector.impl.serializer.S7SerializerImpl;
import com.github.s7connector.impl.utils.S7Type;

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
		
		S7SerializerImpl s = new S7SerializerImpl(c);
		
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
