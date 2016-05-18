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
