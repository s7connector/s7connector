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

import com.github.s7connector.bean.S7Serializer;
import com.github.s7connector.blocks.CONT_C;
import com.github.s7connector.impl.S7TCPConnection;
import com.github.s7connector.test.test.base.Timer;

public class PerformanceTest
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		Timer timer = new Timer();
		
		S7TCPConnection c = new S7TCPConnection("192.168.0.1");
		
		S7Serializer serializer = new S7Serializer(c);
		
		System.out.println("Connection: " + timer.getMillisAndReset() + " ms");

		CONT_C cont_c = serializer.dispense(CONT_C.class, 66, 0);
		
		System.out.println(cont_c);
		
		System.out.println("Read: " + timer.getMillisAndReset() + " ms");

		c.close();
	}
	

}
