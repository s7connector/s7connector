<p align="center">
  <img src="http://s33.postimg.org/fg8gkqfhr/s7connector.png" alt="S7 PLC Connector for Java" width="226">
  <br>
  <a href="https://travis-ci.org/s7connector/s7connector"><img src="https://travis-ci.org/s7connector/s7connector.svg?branch=master" alt="Build Status"></a>
</p>

<p align="center"><b>S7 PLC Connector for Java</b></p>

<p align="center"><img src="http://www.plcdev.com/files/plcdev/images/S7-family.jpg" width=400 height=250 alt="Screenshot of Example Documentation created with Slate"></p>

Features
---------

* **Connect to Siemens S7 PLCs using TCP Connection**

* **Reading and Writing data from/to S7 PLCs**

* **OSGi Support**

* **PROFINET Support**

* **Use directly from Maven Central**

* **Apache License**


Getting Started
----------------

### Simple read/write example
```java
	//Create connection
    S7Connector connector = 
            S7ConnectorFactory
            .buildTCPConnector()
            .withHost("10.0.0.220")
            .withRack(0) //optional
            .withSlot(2) //optional
            .build();
                
	//Read from DB100 10 bytes
	byte[] bs = connector.read(DaveArea.DB, 100, 10, 0);

	//Set some bytes
	bs[0] = 0x00;
		
	//Write to DB100 10 bytes
	connector.write(DaveArea.DB, 101, 0, bs);

	//Close connection
	connector.close();
```

More in the [Documentation](https://s7connector.github.io/s7connector/)


Maven directions
------------------------

```xml
<dependency>
    <groupId>com.github.s7connector</groupId>
    <artifactId>s7connector</artifactId>
    <version>2.0</version>
</dependency>
```


Need Help? Found a bug?
------------------------

Feel free to [submit an issue](https://github.com/s7connector/s7connector/issues). And, of course, feel free to submit pull requests with bug fixes or changes.


Contributors
------------

Pull requests are always welcome.
See [CONTRIBUTING.md](CONTRIBUTING.md) for details.


License
-------

See [LICENSE.txt](LICENSE.txt) file.


Special Thanks
--------------

This project is based on [libnodave](https://sourceforge.net/projects/libnodave/)
