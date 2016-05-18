/*
Copyright 2016 Thomas Rudin

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
package io.rudin.s7connector.bean.parser;

import io.rudin.s7connector.converter.base.S7Serializable;
import io.rudin.s7connector.impl.utils.S7Type;

import java.lang.reflect.Field;

/**
 * A Bean-Entry
 * @author Thomas Rudin
 *
 */
public class BeanEntry
{
	/**
	 * The corresponding field
	 */
	public Field field;
	
	/**
	 * The Java type
	 */
	public Class<?> type;
	
	/**
	 * Offsets and size
	 */
	public int byteOffset, bitOffset, size;
	
	/**
	 * The S7 Type
	 */
	public S7Type s7type;
	
	/**
	 * The corresponding serializer
	 */
	public S7Serializable serializer;
	
	/**
	 * Array type
	 */
	public boolean isArray;
	
	/**
	 * The Array size
	 */
	public int arraySize;
}
