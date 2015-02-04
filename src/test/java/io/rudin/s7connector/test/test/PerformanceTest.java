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
package io.rudin.s7connector.test.test;

import io.rudin.s7connector.bean.S7Serializer;
import io.rudin.s7connector.blocks.CONT_C;
import io.rudin.s7connector.impl.S7TCPConnection;
import io.rudin.s7connector.test.test.base.Timer;

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
