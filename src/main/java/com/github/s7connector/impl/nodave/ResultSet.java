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
package com.github.s7connector.impl.nodave;

/**
 * @author Thomas Hergenhahn
 *
 */
public final class ResultSet {
	private int errorState, numResults;
	public Result[] results;

	public int getErrorState() {
		return this.errorState;
	}

	public int getNumResults() {
		return this.numResults;
	};

	public void setErrorState(final int error) {
		this.errorState = error;
	}

	public void setNumResults(final int nr) {
		this.numResults = nr;
	};
}
