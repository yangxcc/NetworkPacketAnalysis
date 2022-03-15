### PCAP文件格式

<img src="image/image-20220313151127868.png" alt="image-20220313151127868" style="zoom:50%;" />

如上图所示，PCAP文件是由一个global header，后面跟着若干组packet header + data组成的

global header一共有24字节，下面字段也是按照顺序排列的

- magic，占4B，如果它的值是`0xa1b2c3d4`，代表 Pcap 文件是大端模式存储的；如果它的值是 `0xd4c3b2a1`，代表 Pcap 文件是小端模式存储的。这里注意**这里的大端小端仅仅是指 Pcap 文件的 Global Header 和 Packet Header，而无关Packet Data 里的内容。**Packet Data里面就是利用抓包工具如wireshark捕获的数据包，他们都是符合网络字节序的，而网络字节序就是大端模式。
- major，占2B，文件的主版本号，一般为0x0200。
- minor，占2B，文件的次要版本号，一般为0x0400。
- thisZone，占4B，当地标准时间，如果是GMT则全为0。
- sigFigs，占4B，时间戳精度，一般全0。
- snapLen，占4B，最长存储长度。
- linkType，占4B，链路类型，以太网则为1，一般为1。



packet header共有16个字节，以下字段也是按照文件顺序列出的

- timestamp，占4B，时间戳高位，精确到seconds，这是Unix时间戳。捕获数据包的时间一般是根据这个值。
- timestamp，占4B，时间戳低位，能够精确到microseconds。
- capLen，占4B，当前数据区的长度，即抓取到的数据帧长度，由此可以得到下一个数据帧的位置。
- len，占4B，离线数据长度，网路中实际数据帧的长度，一般不大于capLen，多数情况下和capLen值一样。如果文件中保存不是完整的数据包，那么这个值可能要比前面的数据包长度的值大。



### Packet Data

Packet Data其实就是我们熟悉的数据链路层头部 + IP头 + 传输层头部（TCP/UDP）+ （TLS）+ Application data

- 以太网的帧结构

  <img src="image/image-20220313151728143.png" alt="image-20220313151728143" style="zoom:67%;" />

  可以看到共有14个字节，6个字节的源MAC，6个字节的目的MAC以及2个字节的网络层协议类型（2048表示的是IP）

- IP头部结构

  <img src="image/image-20220313151851228.png" alt="image-20220313151851228" style="zoom:67%;" />

  如果去掉选项字段，那么IP头部共有20个字节，这程序中我目前只解析了这20字节的头部，因为选项字段中有一些信息有的数据包中有，有的数据包中没有，处理起来比较麻烦，所以直接把这个选项字段暂时越过去了

- TCP头部结构

  <img src="image/image-20220313152027333.png" alt="image-20220313152027333" style="zoom:67%;" />

  TCP数据段的首部长度也不是固定的，图中的数据偏移就相当于首部的长度，注意单位是4字节，然后根据此值截取相应的长度对TCP的首部进行解析。





### 大端模式和小端模式

0x123456在内存中的存储方式

\- 大端模式：高字节存放在低地址

```cpp
  低地址 -----> 高地址
  0x12 | 0x34 | 0x56 
```

\- 小端模式：低字节存放在低地址

```cpp
  低地址 -----> 高地址
  0x56 | 0x34 | 0x12 
```

大小端模式各有优势：小端模式强制转换类型时不需要调整字节内容，直接截取低字节即可；大端模式由于符号位为第一个字节，很方便判断正负。



相同字节序的平台在进行网络通信时可以不进行字节序转换，但是跨平台进行网络数据通信时必须进行字节序转换。TCP/IP协议规定了大端模式，还专门除了网络字节序和主机字节序相互转换的接口







### 项目运行流程

1. 在[网站上]([IP/服务器linecorp.com/zh-hans/的信息 - 站长工具 (chinaz.com)](https://ip.tool.chinaz.com/linecorp.com/zh-hans/))查询域名对应的IP地址

2. 抓包，我是在阿里云服务器上使用[tcpdump]()命令进行抓包，具体命令是

   ```shell
   tcpdump -v -i eth0 dst 147.92.184.162 -w PacpForLine.pcap
   # -v是产生比较详细的输出，比如包的TTL等
   # -i是指定要过滤的网卡接口
   # dst是根据目标网段进行过滤
   # -w是把产生的pcap文件写入到本地
   ```

3. 使用`xftp`把产生的文件传入到`Windows`，因为`windows`上安装了`wireshark`，能够可视化数据包文件，如下所示

   <img src="image/image-20220315164545963.png" alt="image-20220315164545963" style="zoom:80%;" />

4. 根据`wireshark`中可视化的信息以及`pcap`文件的结构，编码进行解析

5. 解析代码总共分成了以下几个部分

   ```
   |--- structure
   		|--- GlobalHeader      （讲global header中的字段封装成一个类）
   		|--- PacketHeader
   		|--- DataLinkLayer
   		|--- IPHeader
   		|--- TCPHeader
   				|--- TCPOptions
   		|--- TLSStruct
   				|--- TLSHandShake
           
   
   |--- service
          |---- PCAPHeaderService  （对pcap文件的global header进行解析）
          |---- PacketHeaderService （对每个packet的header部分进行分析）
          |---- PacketDataService   （对每个packet的data部分进行分析）
                      |---- DataLayerService
                      |---- IPLayerService
                      |---- TCPLayerService
                      |---- TLSParseService
   
   |--- DataUtils
   		|--- byteArrayToInt
   		|--- convertFromIntToHexa
   		|--- unixTimeStampToDate
   		|--- byteArrayToString
   		|--- ...
   
   |--- test
   	  |--- TestParse
   ```

6. 运行`TestParse`即可

   ```java
   File file = new File("E:\\idea-workspace\\network\\src\\file\\Line.pcap");
   PCAPFileParse fileParse = new PCAPFileParse();
   fileParse.parsePCAP(file);
   ```

   ![image-20220315165714714](image/image-20220315165714714.png)



**不足之处**

- 项目中并没有对一些标记位进行转换，比如在IP头部中的`protocol`字段等于6的时候，就指的是TCP协议，代码中没有进行进一步的转换，GlobalHeader中的`magicNum=D4C3B2A1`也没有说他是小端模式，但是在后续代码中对于相应的计算已经做了对应处理...
- 项目的TCPOptions字段过于复杂，计算比较困难，所以暂时放弃了
- 项目中对数据包的校验比较少，只有少量的几个校验，比如对`globalheader,packetHeader`进行校验判断数据包是否损坏
- 对于TLS的解析工作也略有不足，对于TLS握手阶段，只对`ClientHello`进行了比较充分的解析，包括密码套件等，但是对于另外的三个过程，只是能够判断出处于那种状态，没有对后续数据做进一步处理

