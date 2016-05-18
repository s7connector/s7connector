package io.rudin.s7connector.test.test.example;

import io.rudin.s7connector.impl.S7Connector;
import io.rudin.s7connector.impl.S7TCPConnection;
import io.rudin.s7connector.impl.nodave.DaveArea;

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
