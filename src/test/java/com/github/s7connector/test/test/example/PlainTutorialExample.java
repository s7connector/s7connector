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
package com.github.s7connector.test.test.example;

import com.github.s7connector.impl.S7Connector;
import com.github.s7connector.impl.S7TCPConnection;
import com.github.s7connector.impl.nodave.DaveArea;

/**
 * @author Thomas Rudin (thomas@rudin-informatik.ch)
 *
 */
public class PlainTutorialExample
{
	
	public static void main(String[] args) throws Exception
	{
		//Open TCP Connection
		S7Connector connector = new S7TCPConnection("10.0.0.240", 0, 2);
		
		//Read from DB100 10 bytes
		byte[] bs = connector.read(DaveArea.DB, 100, 10, 0);
		
		//Set some bytes
		bs[0] = 0x00;
		
		//Write to DB100 10 bytes
		connector.write(DaveArea.DB, 101, 0, bs);
		
		//Close connection
		connector.close();
	}

}
