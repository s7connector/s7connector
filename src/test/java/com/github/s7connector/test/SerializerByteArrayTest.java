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

import java.util.Random;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.github.s7connector.api.S7Serializer;
import com.github.s7connector.api.annotation.Datablock;
import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.api.factory.S7SerializerFactory;
import com.github.s7connector.impl.utils.S7Type;
import com.github.s7connector.test.connector.EchoConnector;

public class SerializerByteArrayTest {

	private static final Random RNG = new Random();

	
	@Test
	//@Ignore //Note: Dispenser does not work currently with plain byte[], use Byte[] instead
	public void test() {
		EchoConnector c = new EchoConnector();
		S7Serializer p = S7SerializerFactory.buildSerializer(c);

		/*
		 * first level
		 */
		
		MyDB in = new MyDB();

		for (int i=0; i<in.byteArray.length; i++)
			in.byteArray[i] = (byte)RNG.nextInt(256);
		
		in.byteArray2 = new byte[10];
		for (int i=0; i<in.byteArray2.length; i++)
			in.byteArray2[i] = (byte)RNG.nextInt(256);
		

		
		/*
		 * EOL
		 */
		
		p.store(in, 0, 0);
		
		MyDB out = p.dispense(MyDB.class, 0, 0);
		
		for (int i=0; i<in.byteArray.length; i++)
			Assert.assertEquals( in.byteArray[i], out.byteArray[i] );
		
		for (int i=0; i<in.byteArray2.length; i++)
			Assert.assertEquals( in.byteArray2[i], out.byteArray2[i] );
	}
	
	@Datablock
	public static class MyDB
	{

		/**
		 * Initialized array
		 */
		@S7Variable(type=S7Type.BYTE, byteOffset=0, arraySize=10)
		public byte[] byteArray = new byte[10];
		
		/**
		 * not initialized array
		 */
		@S7Variable(type=S7Type.BYTE, byteOffset=10, arraySize=10)
		public byte[] byteArray2;

	}
	


}
