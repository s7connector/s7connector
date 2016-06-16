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
package com.github.s7connector.impl.io;

import com.github.s7connector.api.S7Connector;

// TODO: Auto-generated Javadoc
/**
 * Datablock implementation.
 *
 * @author Thomas Rudin (thomas@rudin-informatik.ch)
 */
public final class DataBlock {

	/** The connector. */
	private final S7Connector connector;

	/** The db num. */
	private final int dbNum;

	/**
	 * Instantiates a new data block.
	 *
	 * @param connector
	 *            the connector
	 * @param dbNum
	 *            the db num
	 */
	public DataBlock(final S7Connector connector, final int dbNum) {
		this.connector = connector;
		this.dbNum = dbNum;
	}

	/**
	 * Gets the connector.
	 *
	 * @return the connector
	 */
	public S7Connector getConnector() {
		return this.connector;
	}

	/**
	 * Gets the db num.
	 *
	 * @return the db num
	 */
	public int getDbNum() {
		return this.dbNum;
	}

}
