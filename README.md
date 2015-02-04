
S7 PLC Connector for Java
---------------------------------------------------

Connect to a PLC
---------------------------------------------------

```java
 S7Connector c = new S7TCPConnection("10.0.0.1");
```

Read raw data
---------------------------------------------------

```java
 c.write(DaveArea.FLAGS, 0, 1, 10, new byte[]{ 0x01 }); //writes 1 byte to MB10
 c.write(DaveArea.DB, 100, 2, 0, new byte[] { 0x01, 0x02 } ); //Writes 2 bytes to DB100
 byte[] b1 = c.read(DaveArea.FLAGS, 0, 10, 0); //reads 1 byte from MB10
```

Close connection
---------------------------------------------------

```java
 c.close(); //closes the connection
```

Use the bean serializer
---------------------------------------------------

Annotated bean:

```java
 public class DB
 {
  @S7Variable(type=S7Type.REAL, byteOffset=0)
  public double value;
  
  @S7Variable(type=S7Type.BOOL, byteOffset=0, bitOffset=1)
  public boolean myBit;
 }
```

Dispense/Store

```java
 S7Serializer s = new S7Serializer(c);
 DB fromPlc = s.dispense(DB.class, 100, 0); //Creates a bean from DB100 at offset 0
 
 DB toPlc = new DB();
 toPlc.value = Math.PI;
 toPlc.myBit = true;
 s.store(toPlc, 101, 0); //Saves the bean to DB101 at offset 0
```




Limitations
---------------------------------------------------

At the moment, only byte[] are supported (except for the Serializer component).
The bits have to be set manually for boolean values (e.g: 0x01 for first bit true)


Maven Repository
---------------------------------------------------

To access the latest release

Add artifact to your pom.xml:
```xml
 <dependency>
	<artifactId>s7connector</artifactId>
	<groupId>io.rudin.s7connector</groupId>
	<version>1.0</version>
 </dependency>
```

Libnodave
---------------------------------------------------

This project is based on libnodave
http://libnodave.sourceforge.net/



