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

import com.github.s7connector.api.S7Serializer;
import com.github.s7connector.api.annotation.Datablock;
import com.github.s7connector.api.annotation.S7Variable;
import com.github.s7connector.api.factory.S7SerializerFactory;
import com.github.s7connector.api.S7Type;
import com.github.s7connector.test.connector.EchoConnector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class StringSerializerTest {


	@Test
	public void test() {
		EchoConnector c = new EchoConnector();
		
		S7Serializer p = S7SerializerFactory.buildSerializer(c);

		/*
		 * first level
		 */
		
		MyDB in = new MyDB();
		in.str = "Hello World";

		p.store(in, 0, 0);
		
		MyDB out = p.dispense(MyDB.class, 0, 0);
		
		Assert.assertEquals(in.str, out.str);

	}
	
	@Datablock
	public static class MyDB {
		@S7Variable(type=S7Type.STRING, byteOffset=0, size=20)
		public String str;
	}
}
