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
package com.github.s7connector.api.factory;

import com.github.s7connector.api.S7Connector;
import com.github.s7connector.impl.S7TCPConnection;

/**
 * S7 connector factory, currently only for TCP connections
 * 
 * @author Thomas Rudin
 *
 */
public class S7ConnectorFactory {

	/**
	 * returns a new TCP connection builder
	 * @return
	 */
	public static TCPConnectionBuilder buildTCPConnector(){
		return new TCPConnectionBuilder();
	}
	
	/**
	 * TCP Connection builder
	 *
	 */
	public static class TCPConnectionBuilder {
		
		private String host;
		
		private int rack = 0, slot = 2, port = 102;
		
		/**
		 * use hostname/ip
		 */
		public TCPConnectionBuilder withHost(String host){
			this.host = host;
			return this;
		}
		
		/**
		 * use port, default is 102
		 */
		public TCPConnectionBuilder withPort(int port){
			this.port = port;
			return this;
		}
		
		/**
		 * use rack, default is 0
		 */
		public TCPConnectionBuilder withRack(int rack){
			this.rack = rack;
			return this;
		}
		
		/**
		 * use slot, default is 2
		 */
		public TCPConnectionBuilder withSlot(int slot){
			this.slot = slot;
			return this;
		}
		
		/**
		 * Builds a connection with given params
		 */
		public S7Connector build(){
			return new S7TCPConnection(host, rack, slot, port);
		}
		
	}
	
}
