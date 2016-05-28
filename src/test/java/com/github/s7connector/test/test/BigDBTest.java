package com.github.s7connector.test.test;

import java.util.Random;

import org.junit.Assert;

import com.github.s7connector.bean.S7Serializer;
import com.github.s7connector.bean.annotation.S7Variable;
import com.github.s7connector.impl.S7TCPConnection;
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
