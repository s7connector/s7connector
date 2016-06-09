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
package com.github.s7connector.impl.utils;

/**
 * S7-Utility class
 *
 * @author Thomas Rudin Libnodave: http://libnodave.sourceforge.net/
 *
 */
public final class S7Utils {
	/**
	 * Converts a byte to 8 bits
	 *
	 * @param buffer
	 *            The Input-Byte
	 * @return The 8 bits
	 */
	public static boolean[] getBits(int buffer) {
		if (buffer < 0) {
			buffer += 256;
		}

		final String binString = Integer.toBinaryString(buffer);
		/*
		 * String-Pos: 0 1 2 3 4 5 6 7 Bit: 128 64 32 16 8 4 2 1
		 */

		final boolean[] ret = new boolean[8];
		for (int i = binString.length() - 1; i >= 0; i--) {
			// Check for the '1'-Char and mirror-set the result
			final int mirrorPos = (binString.length() - 1) - i;
			if (binString.charAt(i) == '1') {
				ret[mirrorPos] = true;
			}
		}
		return ret;
	}

	/** Constructor */
	private S7Utils() {
		// Not needed. Utility class.
	}
}
