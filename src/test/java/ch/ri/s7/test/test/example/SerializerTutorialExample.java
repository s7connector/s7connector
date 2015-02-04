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
package ch.ri.s7.test.test.example;

import ch.ri.s7.bean.S7Serializer;
import ch.ri.s7.impl.S7Connector;
import ch.ri.s7.impl.S7TCPConnection;

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
