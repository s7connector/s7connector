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
package com.github.s7connector.test.repro;

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Serializer;
import com.github.s7connector.api.annotation.Datablock;
import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.api.factory.S7SerializerFactory;
import com.github.s7connector.api.S7Type;
import com.github.s7connector.test.connector.EchoConnector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Repro for #60
 */
public class ReproBug60 {

	private static final Random RNG = new Random();

	
	@Test
	public void test() {
		EchoConnector c = new EchoConnector();

		//Prepare data
		byte[] in = new byte[10];
		c.write(DaveArea.DB, 4, 0, in);

		S7Serializer p = S7SerializerFactory.buildSerializer(c);

		MyDB out = p.dispense(MyDB.class, 4, 0);
		//Result: Reading area=DB areaNumber=4, bytes=10 offset=0

		Assert.assertNotNull(out);
		Assert.assertNotNull(out.byteArray);
	}
	
	@Datablock
	public static class MyDB {
		@S7Variable(byteOffset=0, bitOffset=0, type=S7Type.BYTE, arraySize=10)
		public Byte[] byteArray;


	}
	


}
