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
package ch.ri.s7.impl.nodave;

public class PDU
{
	/*
	 * typedef struct { uc P; // allways 0x32 uc type; // a type? type 2 and 3
	 * headers are two bytes longer. uc a,b; // currently unknown us number; //
	 * Number, can be used to identify answers corresponding to requests us
	 * plen; // length of parameters which follow this header us dlen; // length
	 * of data which follows the parameters uc x[2]; // only present in type 2
	 * and 3 headers. This may contain error information. } PDUHeader;
	 */
	/**
	 * return the number of the PDU
	 */
	public int getNumber()
	{
		return Nodave.USBEWord(mem, header + 4);
	}

	/**
	 * set the number of the PDU
	 */
	public void setNumber(int n)
	{
		Nodave.setUSBEWord(mem, header + 4, n);
	}

	/**
	 * return the function code of the PDU
	 */
	public int getFunc()
	{
		return Nodave.USByte(mem, param + 0);
	}

	/**
	 * known function codes
	 */
	public final static byte FUNC_READ = 4;
	public final static byte FUNC_WRITE = 5;

	int header; // the position of the header;
	public int param; // the position of the parameters;
	byte[] mem;
	int hlen;
	public int plen;
	int dlen;
	public int udlen;
	public int data;
	public int udata;

	/**
	 * set up the PDU information
	 */
	public PDU(byte[] mem, int pos)
	{
		this.mem = mem;
		this.header = pos;
	}

	public int addVarToReadRequest(DaveArea area, int DBnum, int start, int len)
	{
		byte[] pa = {
				0x12, 0x0a, 0x10,
				0x02,				/* 1=single bit, 2=byte, 4=word */
				0x00, 0x1A,			/* length in bytes */
				0x00, 0x0B, 		/* DB number */
				(byte) 0x84, 		//* area code */
				0x00, 0x00, (byte) 0xC0 /* start address in bits */
		};

		if ((area == DaveArea.ANALOGINPUTS200)
				|| (area == DaveArea.ANALOGOUTPUTS200))
		{
			pa[3] = 4;
			start *= 8; /* bits */
		}
		else if ((area == DaveArea.TIMER) || (area == DaveArea.COUNTER)
				|| (area == DaveArea.TIMER200) || (area == DaveArea.COUNTER200))
		{
			pa[3] = (byte) area.getCode();
		}
		else
		{
			start *= 8; /* bits */
		}

		Nodave.setUSBEWord(pa, 4, len);
		Nodave.setUSBEWord(pa, 6, DBnum);
		Nodave.setUSBELong(pa, 8, start);
		Nodave.setUSByte(pa, 8, area.getCode());

		mem[param + 1]++;
		System.arraycopy(pa, 0, mem, param + plen, pa.length);
		plen += pa.length;
		Nodave.setUSBEWord(mem, header + 6, plen);
		/**
		 * TODO calc length of result. Do not add variable if it would exceed
		 * max. result length.
		 */
		return 0;
	}

	public int addBitVarToReadRequest(int area, int DBnum, int start, int len)
	{
		byte pa[] = { 0x12, 0x0a, 0x10, 0x01, /* single bits */
		0x00, 0x1A, /* insert length in bytes here */
		0x00, 0x0B, /* insert DB number here */
		(byte) 0x84, /* change this to real area code */
		0x00, 0x00, (byte) 0xC0 /* insert start address in bits */
		};
		Nodave.setUSBEWord(pa, 4, len);
		Nodave.setUSBEWord(pa, 6, DBnum);
		Nodave.setUSBELong(pa, 8, start);
		Nodave.setUSByte(pa, 8, area);

		mem[param + 1]++;
		System.arraycopy(pa, 0, mem, param + plen, pa.length);
		plen += pa.length;
		Nodave.setUSBEWord(mem, header + 6, plen);
		return 0;

	}

	/**
	 * prepare a read request with no item.
	 */
	public void prepareReadRequest()
	{
		byte pa[] = new byte[2];
		pa[0] = PDU.FUNC_READ;
		pa[1] = (byte) 0x00;
		initHeader(1);
		addParam(pa);
	}

	/**
	 * prepare a write request with no item.
	 */
	public void prepareWriteRequest()
	{
		byte pa[] = new byte[2];
		pa[0] = PDU.FUNC_WRITE;
		pa[1] = (byte) 0x00;
		initHeader(1);
		addParam(pa);
	}

	/**
	 * construct a write request for a single item in PLC memory.
	 */
	/*
	 * void constructWriteRequest( int area, int DBnum, int start, int len,
	 * byte[] buffer) { byte pa[] = new byte[14]; byte da[] = { 0, 4, 0, 0 };
	 * pa[0] = PDU.FUNC_WRITE; pa[1] = (byte) 0x01; pa[2] = (byte) 0x12; pa[3] =
	 * (byte) 0x0a; pa[4] = (byte) 0x10; pa[5] = (byte) 0x02;
	 * 
	 * Nodave.setUSBEWord(pa, 6, len); Nodave.setUSBEWord(pa, 8, DBnum);
	 * Nodave.setUSBELong(pa, 10, 8 * start); // the bit address
	 * Nodave.setUSByte(pa, 10, area); initHeader(1); addParam(pa); addData(da);
	 * addValue(buffer); if ((Nodave.Debug & Nodave.DEBUG_PDU) != 0) { dump(); }
	 * }
	 */
	/**
	 * display information about a PDU
	 */
	public void dump()
	{
		Nodave.dump("PDU header ", mem, header, hlen);
		System.out.println("plen: " + plen + " dlen: " + dlen);
		Nodave.dump("Parameter", mem, param, plen);
		if (dlen > 0)
		{
			Nodave.dump("Data     ", mem, data, dlen);
		}
		if (udlen > 0)
		{
			Nodave.dump("result Data ", mem, udata, udlen);
		}
	}

	/**
	 * reserve space for the header of a new PDU
	 */
	public void initHeader(int type)
	{
		if ((type == 2) || (type == 3))
		{
			hlen = 12;
		}
		else
		{
			hlen = 10;
		}
		for (int i = 0; i < hlen; i++)
		{
			mem[header + i] = 0;
		}
		param = header + hlen;
		mem[header] = (byte) 0x32;
		mem[header + 1] = (byte) type;
		dlen = 0;
		plen = 0;
		udlen = 0;
		data = 0;
		udata = 0;
	}

	public void addParam(byte[] pa)
	{
		plen = pa.length;
		System.arraycopy(pa, 0, mem, param, plen);
		Nodave.setUSBEWord(mem, header + 6, plen);
		// mem[header + 6] = (byte) (pa.length / 256);
		// mem[header + 7] = (byte) (pa.length % 256);
		data = param + plen;
		dlen = 0;
	}

	/**
	 * Add data after parameters, set dlen as needed. Needs valid header and
	 * parameters
	 */
	void addData(byte[] newData)
	{
		int appPos = data + dlen; // append to this position
		dlen += newData.length;
		System.arraycopy(newData, 0, mem, appPos, newData.length);
		Nodave.setUSBEWord(mem, header + 8, dlen);
	}

	/**
	 * Add len bytes of len after parameters from a maybe longer block of bytes.
	 * Set dlen as needed. Needs valid header and parameters
	 */
	public void addData(byte[] newData, int len)
	{
		int appPos = data + dlen; // append to this position
		dlen += len;
		System.arraycopy(newData, 0, mem, appPos, len);
		Nodave.setUSBEWord(mem, header + 8, dlen);
	}

	/**
	 * Add values after value header in data, adjust dlen and data count. Needs
	 * valid header,parameters,data,dlen
	 */
	void addValue(byte[] values)
	{
		int valCount = (0x100 * mem[data + 2]) + mem[data + 3];
		if (mem[data + 1] == 4)
		{ // bit data, length is in bits
			valCount += 8 * values.length;
		}
		else if (mem[data + 1] == 9)
		{ // byte data, length is in bytes
			valCount += values.length;
		}
		else
		{
			// XXX
		}
		if (udata == 0)
		{
			udata = data + 4;
		}
		udlen += values.length;
		Nodave.setUSBEWord(mem, data + 2, valCount);
		addData(values);
	}

	int error;

	public int getError()
	{
		return error;
	}

	/**
	 * Setup a PDU instance to reflect the structure of data present in the
	 * memory area given to initHeader. Needs valid header.
	 */

	public int setupReceivedPDU()
	{
		int res = Nodave.RESULT_CANNOT_EVALUATE_PDU; // just assume the worst
		if ((mem[header + 1] == 2) || (mem[header + 1] == 3))
		{
			hlen = 12;
			res = Nodave.USBEWord(mem, header + 10);
		}
		else
		{
			error = 0;
			hlen = 10;
			res = 0;
		}
		param = header + hlen;
		plen = Nodave.USBEWord(mem, header + 6);
		data = param + plen;
		dlen = Nodave.USBEWord(mem, header + 8);
		udlen = 0;
		udata = 0;
		return res;
	}

	/*		
			
	*/
	int testResultData()
	{
		int res = Nodave.RESULT_CANNOT_EVALUATE_PDU; // just assume the worst
		if ((mem[data] == (byte) 255) && (dlen > 4))
		{
			res = Nodave.RESULT_OK;
			udata = data + 4;
			// udlen=data[2]*0x100+data[3];
			udlen = Nodave.USBEWord(mem, data + 2);
			if (mem[data + 1] == 4)
			{
				udlen >>= 3; /* len is in bits, adjust */
			}
			else if (mem[data + 1] == 9)
			{
				/* len is already in bytes, ok */
			}
			else if (mem[data + 1] == 3)
			{
				/* len is in bits, but there is a byte per result bit, ok */
			}
			else
			{
				res = Nodave.RESULT_UNKNOWN_DATA_UNIT_SIZE;
			}
		}
		else
		{
			res = mem[data];
		}
		return res;
	}

	int testReadResult()
	{
		if (mem[param] != FUNC_READ)
		{
			return Nodave.RESULT_UNEXPECTED_FUNC;
		}
		return testResultData();
	}

	public int testPGReadResult()
	{
		if (mem[param] != 0)
		{
			return Nodave.RESULT_UNEXPECTED_FUNC;
		}
		return testResultData();
	}

	int testWriteResult()
	{
		int res = Nodave.RESULT_CANNOT_EVALUATE_PDU;
		if (mem[param] != FUNC_WRITE)
		{
			return Nodave.RESULT_UNEXPECTED_FUNC;
		}
		if ((mem[data] == 255))
		{
			res = Nodave.RESULT_OK;
		}
		else
		{
			res = mem[data];
		}
		return res;
	}

	/*
	 * add data in user data. Add a user data header, if not yet present.
	 */
	public void addUserData(byte[] da)
	{
		byte udh[] = { (byte) 0xff, 9, 0, 0 };
		if (dlen == 0)
		{
			addData(udh);
		}
		addValue(da);
	};

	public void initReadRequest()
	{
		byte pa[] = new byte[2];
		pa[0] = PDU.FUNC_READ;
		pa[1] = (byte) 0x00;
		initHeader(1);
		addParam(pa);
	}

	public void addVarToWriteRequest(DaveArea area, int DBnum, int start,
			int byteCount, byte[] buffer)
	{
		byte da[] = { 0, 4, 0, 0, };
		byte pa[] = { 0x12, 0x0a, 0x10, 0x02,
		/* unit (for count?, for consistency?) byte */
		0, 0, /* length in bytes */
		0, 0, /* DB number */
		0, /* area code */
		0, 0, 0 /* start address in bits */
		};
		if ((area == DaveArea.TIMER) || (area == DaveArea.COUNTER)
				|| (area == DaveArea.TIMER200) || (area == DaveArea.COUNTER200))
		{
			pa[3] = (byte) area.getCode();
			pa[4] = (byte) (((byteCount + 1) / 2) / 0x100);
			pa[5] = (byte) (((byteCount + 1) / 2) & 0xff);
		}
		else if ((area == DaveArea.ANALOGINPUTS200)
				|| (area == DaveArea.ANALOGOUTPUTS200))
		{
			pa[3] = 4;
			pa[4] = (byte) (((byteCount + 1) / 2) / 0x100);
			pa[5] = (byte) (((byteCount + 1) / 2) & 0xff);
		}
		else
		{
			pa[4] = (byte) (byteCount / 0x100);
			pa[5] = (byte) (byteCount & 0xff);
		}
		pa[6] = (byte) (DBnum / 256);
		pa[7] = (byte) (DBnum & 0xff);
		pa[8] = (byte) (area.getCode());
		start *= 8; /* number of bits */
		pa[11] = (byte) (start & 0xff);
		pa[10] = (byte) ((start / 0x100) & 0xff);
		pa[9] = (byte) (start / 0x10000);
		if ((dlen % 2) != 0)
		{
			addData(da, 1);
		}
		mem[param + 1]++;
		if (dlen > 0)
		{
			byte[] saveData = new byte[dlen];
			System.arraycopy(mem, data, saveData, 0, dlen);
			System.arraycopy(saveData, 0, mem, data + pa.length, dlen);
		}
		System.arraycopy(pa, 0, mem, param + plen, pa.length);
		plen += pa.length;
		Nodave.setUSBEWord(mem, header + 6, plen);
		data = param + plen;
		addData(da);
		addValue(buffer);
	}

	public void addBitVarToWriteRequest(DaveArea area, int DBnum, int start,
			int byteCount, byte[] buffer)
	{
		byte da[] = { 0, 3, 0, 0, };
		byte pa[] = { 0x12, 0x0a, 0x10, 0x01, /* single bit */
		0, 0, /* insert length in bytes here */
		0, 0, /* insert DB number here */
		0, /* change this to real area code */
		0, 0, 0 /* insert start address in bits */
		};
		if ((area == DaveArea.TIMER) || (area == DaveArea.COUNTER)
				|| (area == DaveArea.TIMER200) || (area == DaveArea.COUNTER200))
		{
			pa[3] = (byte) area.getCode();
			pa[4] = (byte) (((byteCount + 1) / 2) / 0x100);
			pa[5] = (byte) (((byteCount + 1) / 2) & 0xff);
		}
		else if ((area == DaveArea.ANALOGINPUTS200)
				|| (area == DaveArea.ANALOGOUTPUTS200))
		{
			pa[3] = 4;
			pa[4] = (byte) (((byteCount + 1) / 2) / 0x100);
			pa[5] = (byte) (((byteCount + 1) / 2) & 0xff);
		}
		else
		{
			pa[4] = (byte) (byteCount / 0x100);
			pa[5] = (byte) (byteCount & 0xff);
		}
		pa[6] = (byte) (DBnum / 256);
		pa[7] = (byte) (DBnum & 0xff);
		pa[8] = (byte) area.getCode();
		pa[11] = (byte) (start & 0xff);
		pa[10] = (byte) ((start / 0x100) & 0xff);
		pa[9] = (byte) (start / 0x10000);

		if ((dlen % 2) != 0)
		{
			addData(da, 1);
		}

		mem[param + 1]++;
		if (dlen > 0)
		{
			byte[] saveData = new byte[dlen];
			System.arraycopy(mem, data, saveData, 0, dlen);
			System.arraycopy(saveData, 0, mem, data + pa.length, dlen);
		}
		System.arraycopy(pa, 0, mem, param + plen, pa.length);
		plen += pa.length;
		Nodave.setUSBEWord(mem, header + 6, plen);
		data = param + plen;

		addData(da);
		addValue(buffer);
	}

}
