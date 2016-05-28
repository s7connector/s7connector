package com.github.s7connector.test.test.example;

import com.github.s7connector.bean.S7Serializer;
import com.github.s7connector.impl.S7Connector;
import com.github.s7connector.impl.S7TCPConnection;

/**
 * @author Thomas Rudin (thomas@rudin-informatik.ch)
 *
 */
public class SerializerTutorialExample
{
	
	public static void main(String[] args) throws Exception
	{
		//Open TCP Connection
		S7Connector connector = new S7TCPConnection("10.0.0.240", 0, 2);
		
		//Create serializer
		S7Serializer serializer = new S7Serializer(connector);
		
		//dispense bean from DB100 and offset 0
		MyDataBean bean1 = serializer.dispense(MyDataBean.class, 100, 0);
		
		//Set some values
		bean1.bit1 = true;
		bean1.myNumber = 123;
		
		//Store bean to DB101 offset 0
		serializer.store(bean1, 101, 0);
		
		//Close connection
		connector.close();
	}

}
