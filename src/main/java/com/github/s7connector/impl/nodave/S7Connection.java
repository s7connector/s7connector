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
package com.github.s7connector.impl.nodave;

/**
 * 
 * @author Thomas Hergenhahn
 * 
 *         This class comprises the variables and methods common to connections
 *         to an S7 PLC regardless of the type of transport.
 */
public abstract class S7Connection
{
	PLCinterface iface; // pointer to used interface
	int answLen; // length of last message
	PDU rcvdPDU;
	public byte[] msgIn;
	public byte[] msgOut;
	/**
	 * position in result data, incremented when variables are extracted without
	 * position
	 */
	int dataPointer;
	/**
	 * absolut begin of result data
	 */
	int udata;
	public int packetNumber = 0; // packetNumber in transport layer
	public byte messageNumber = 0;

	static int tmo_normal = 150;
	public int PDUstartIn;
	public int PDUstartOut;
	public Semaphore semaphore;
	public int maxPDUlength;

	public S7Connection(PLCinterface ifa)
	{
		iface = ifa;
		msgIn = new byte[Nodave.MAX_RAW_LEN];
		msgOut = new byte[Nodave.MAX_RAW_LEN];
		PDUstartIn = 0;
		PDUstartOut = 0;
		semaphore = new Semaphore(1);
	}

	/**
	 * get a float value from the current position in result bytes
	 */
	public float getFloat()
	{
		dataPointer += 4;
		return Nodave.BEFloat(msgIn, dataPointer - 4);
	}

	/*
	 * The following methods are here to give Siemens users their usual data
	 * types:
	 */
	/**
	 * get a float value from the specified position in result bytes
	 */
	public float getFloat(int pos)
	{
		// System.out.println("getFloat pos " + pos);
		return Nodave.BEFloat(msgIn, udata + pos);
	}

	/**
	 * get an unsigned 32bit value from the specified position in result bytes
	 */
	public long getDWORD(int pos)
	{
		// System.out.println("getDWORD pos " + pos);
		return Nodave.USBELong(msgIn, udata + pos);
	}

	/**
	 * get an unsigned 32bit value from the current position in result bytes
	 */
	public long getU32()
	{
		dataPointer += 4;
		return Nodave.USBELong(msgIn, dataPointer - 4);
	}

	/**
	 * get an signed 32bit value from the current position in result bytes
	 */
	public long getDINT()
	{
		dataPointer += 4;
		return Nodave.SBELong(msgIn, dataPointer - 4);
	}

	/**
	 * get an signed 32bit value from the specified position in result bytes
	 */
	public long getDINT(int pos)
	{
		return Nodave.SBELong(msgIn, udata + pos);
	}

	/**
	 * get an unsigned 16bit value from the specified position in result bytes
	 */
	public int getWORD(int pos)
	{
		return Nodave.USBEWord(msgIn, udata + pos);
	}

	/**
	 * get an unsigned 16bit value from the current position in result bytes
	 */
	public int getWORD()
	{
		dataPointer += 2;
		return Nodave.USBEWord(msgIn, dataPointer - 2);
	}

	public int getINT(int pos)
	{
		return Nodave.SBEWord(msgIn, udata + pos);
	}

	public int getINT()
	{
		dataPointer += 2;
		return Nodave.SBEWord(msgIn, dataPointer - 2);
	}

	public int getBYTE(int pos)
	{
		return Nodave.SByte(msgIn, udata + pos);
	}

	public int getBYTE()
	{
		dataPointer += 1;
		return Nodave.SByte(msgIn, dataPointer - 1);
	}

	public int getCHAR(int pos)
	{
		return Nodave.SByte(msgIn, udata + pos);
	}

	public int getCHAR()
	{
		dataPointer += 1;
		return Nodave.SByte(msgIn, dataPointer - 1);
	}

	public long getUS32(int pos)
	{
		return Nodave.USBELong(msgIn, udata + pos);
	}

	public long getS32(int pos)
	{
		return Nodave.SBELong(msgIn, udata + pos);
	}

	public int getUS16(int pos)
	{
		return Nodave.USBEWord(msgIn, udata + pos);
	}

	public int getS16(int pos)
	{
		return Nodave.SBEWord(msgIn, udata + pos);
	}

	public int getUS8(int pos)
	{
		return Nodave.USByte(msgIn, udata + pos);
	}

	public int getS8(int pos)
	{
		return Nodave.SByte(msgIn, udata + pos);
	}

	abstract public int exchange(PDU p1);

	public int readBytes(DaveArea area, int DBnum, int start, int len,
			byte[] buffer)
	{
		int res = 0;
		semaphore.enter();
		// System.out.println("readBytes");
		PDU p1 = new PDU(msgOut, PDUstartOut);
		p1.initReadRequest();
		p1.addVarToReadRequest(area, DBnum, start, len);

		res = exchange(p1);
		if (res != Nodave.RESULT_OK)
		{
			semaphore.leave();
			return res;
		}
		PDU p2 = new PDU(msgIn, PDUstartIn);
		res = p2.setupReceivedPDU();
		if (res != Nodave.RESULT_OK)
		{
			semaphore.leave();
			return res;
		}

		res = p2.testReadResult();
		if (res != Nodave.RESULT_OK)
		{
			semaphore.leave();
			return res;
		}
		if (p2.udlen == 0)
		{
			semaphore.leave();
			return Nodave.RESULT_CPU_RETURNED_NO_DATA;
		}
		/*
		 * copy to user buffer and setup internal buffer pointers:
		 */
		if (buffer != null)
		{
			System.arraycopy(p2.mem, p2.udata, buffer, 0, p2.udlen);
		}

		dataPointer = p2.udata;
		udata = p2.udata;
		answLen = p2.udlen;
		semaphore.leave();
		return res;
	}

	/*
	 * Write len bytes to PLC memory area "area", data block DBnum.
	 */
	public int writeBytes(DaveArea area, int DBnum, int start, int len,
			byte[] buffer)
	{
		int errorState = 0;
		semaphore.enter();
		PDU p1 = new PDU(msgOut, PDUstartOut);

		// p1.constructWriteRequest(area, DBnum, start, len, buffer);
		p1.prepareWriteRequest();
		p1.addVarToWriteRequest(area, DBnum, start, len, buffer);

		errorState = exchange(p1);

		if (errorState == 0)
		{
			PDU p2 = new PDU(msgIn, PDUstartIn);
			p2.setupReceivedPDU();

			if (p2.mem[p2.param + 0] == PDU.FUNC_WRITE)
			{
				if (p2.mem[p2.data + 0] == (byte) 0xFF)
				{
					semaphore.leave();
					return 0;
				}
			}
			else
			{
				errorState |= 4096;
			}
		}
		semaphore.leave();
		return errorState;
	}

	public class Semaphore
	{
		private int value;

		public Semaphore(int value)
		{
			this.value = value;
		}

		public synchronized void enter()
		{
			--value;
			if (value < 0)
			{
				try
				{
					wait();
				} catch (Exception e)
				{
				}
			}
		}

		public synchronized void leave()
		{
			++value;
			notify();
		}
	}

	/*
	 * public void sendYOURTURN() { }
	 */
	public int getResponse()
	{
		return 0;
	}

	public int getPPIresponse()
	{
		return 0;
	}

	public int sendMsg(PDU p)
	{
		return 0;
	}

	public void sendRequestData(int alt)
	{
	}

	// int numResults;
	/*
	 * class Result { int error; byte[] data; }
	 */
	/*
	 * Read a predefined set of values from the PLC. Return ok or an error state
	 * If a buffer pointer is provided, data will be copied into this buffer. If
	 * it's NULL you can get your data from the resultPointer in daveConnection
	 * long as you do not send further requests.
	 */
	public ResultSet execReadRequest(PDU p)
	{
		PDU p2;
		int errorState;
		errorState = exchange(p);

		p2 = new PDU(msgIn, PDUstartIn);
		p2.setupReceivedPDU();
		/*
		 * if (p2.udlen == 0) { dataPointer = 0; answLen = 0; return
		 * Nodave.RESULT_CPU_RETURNED_NO_DATA; }
		 */
		ResultSet rs = new ResultSet();
		if (p2.mem[p2.param + 0] == PDU.FUNC_READ)
		{
			int numResults = p2.mem[p2.param + 1];
			// System.out.println("Results " + numResults);
			rs.results = new Result[numResults];
			int pos = p2.data;
			for (int i = 0; i < numResults; i++)
			{
				Result r = new Result();
				r.error = Nodave.USByte(p2.mem, pos);
				if (r.error == 255)
				{

					int type = Nodave.USByte(p2.mem, pos + 1);
					int len = Nodave.USBEWord(p2.mem, pos + 2);
					r.error = 0;
					// System.out.println("Raw length " + len);
					if (type == 4)
					{
						len /= 8;
					}
					else if (type == 3)
					{
						; // length is ok
					}

					// System.out.println("Byte length " + len);
					// r.data = new byte[len];

					// System.arraycopy(p2.mem, pos + 4, r.data, 0, len);
					// Nodave.dump("Result " + i + ":", r.data, 0, len);
					r.bufferStart = pos + 4;
					pos += len;
					if ((len % 2) == 1)
					{
						pos++;
					}
				}
				else
				{
					System.out.println("Error " + r.error);
				}
				pos += 4;
				rs.results[i] = r;
			}
			numResults = p2.mem[p2.param + 1];
			rs.setNumResults(numResults);
			dataPointer = p2.udata;
			answLen = p2.udlen;
			// }
		}
		else
		{
			errorState |= 2048;
		}
		semaphore.leave();
		rs.setErrorState(errorState);
		return rs;
	}

	public int useResult(ResultSet rs, int number)
	{
		System.out.println("rs.getNumResults: " + rs.getNumResults()
				+ " number: " + number);
		if (rs.getNumResults() > number)
		{
			dataPointer = rs.results[number].bufferStart;
			return 0;
			// udata=rs.results[number].bufferStart;
		}
		return -33;
	};

	/*
	 * build the PDU for a PDU length negotiation
	 */
	public int negPDUlengthRequest()
	{
		int res;
		PDU p = new PDU(msgOut, PDUstartOut);
		byte pa[] = { (byte) 0xF0, 0, 0x00, 0x01, 0x00, 0x01, 0x03,
				(byte) 0xC0, };
		p.initHeader(1);
		p.addParam(pa);
		res = exchange(p);
		if (res != 0)
		{
			return res;
		}
		PDU p2 = new PDU(msgIn, PDUstartIn);
		res = p2.setupReceivedPDU();
		if (res != 0)
		{
			return res;
		}
		maxPDUlength = Nodave.USBEWord(msgIn, p2.param + 6);
		return res;
	}

}
