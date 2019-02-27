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

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Connector;
import com.github.s7connector.api.factory.S7ConnectorFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionDropTest {


	@Test
	public void test() throws Exception {

		final int port = (int)(Math.random() * 10000) + 10000;
		final ServerSocket serverSocket = new ServerSocket(port);
		new Thread(() -> {
			try {
				Socket socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		S7Connector connector = S7ConnectorFactory.buildTCPConnector()
				.withHost("127.0.0.1")
				.withPort(port)
				.build();

		serverSocket.close();

		try {
			connector.read(DaveArea.DB, 1, 1, 0);
		} catch(IllegalArgumentException e){
			return;
		}

		throw new IllegalArgumentException("fail-case not reached!");
	}

}
