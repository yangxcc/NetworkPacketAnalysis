package code;

import code.structure.*;
import code.utils.DataUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class PcapDecoder {


    private byte[] data = null;
    // 小端模式
    private boolean isBigEndian = true;

    /**
     * Build Pcap Decoder from an absolute file path
     *
     * @param inputFilePath absolute file path
     */
    public PcapDecoder(String inputFilePath) {
        if (inputFilePath != null) {
            try {
                this.data = Files.readAllBytes(Paths.get(inputFilePath));
//                System.out.println("数据文件长度（单位/字节）" + data.length);
                System.out.println(Arrays.toString(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析 pcap 文件头
     */
    public GlobalHeader parseFileHeader() throws IOException {
        // 是一个左闭右开的区间，GlobalHeader共24B，前24B
        byte[] file_header = Arrays.copyOfRange(this.data, 0, 24);
//        file_header = this.data;
        GlobalHeader fileHeader = new GlobalHeader();
        byte[] buff_4 = new byte[4];    // 4 字节的数组
        byte[] buff_2 = new byte[2];    // 2 字节的数组

        int offset = 0;
        for (int i = 0; i < 4; i++) {
            buff_4[i] = file_header[i + offset];
        }
        offset += 4;
        int magic = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setMagic(DataUtils.convertFromIntToHexa(magic));

        for (int i = 0; i < 2; i++) {
            buff_2[i] = file_header[i + offset];
        }
        offset += 2;
        short majorVersion = DataUtils.byteArrayToShort(buff_2);
        fileHeader.setMajor(majorVersion);

        for (int i = 0; i < 2; i++) {
            buff_2[i] = file_header[i + offset];
        }
        offset += 2;
        short minorVersion = DataUtils.byteArrayToShort(buff_2);
        fileHeader.setMinor(minorVersion);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = file_header[i + offset];
        }
        offset += 4;
        int timezone = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setThisZone(timezone);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = file_header[i + offset];
        }
        offset += 4;
        int sigflags = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setSigFigs(sigflags);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = file_header[i + offset];
        }
        offset += 4;
        int snaplen = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setSnapLen(snaplen);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = file_header[i + offset];
        }
        offset += 4;
        int linktype = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setLinkType(linktype);

//      LogUtils.printObjInfo(fileHeader);

        return fileHeader;
    }


    // 第一，因为是小端，所以他需要reverse，才能够得到正确的值，但是在小端模式中，地址不用反转，时间戳和长度需要反转
    // 第二，对于unix时间戳，要给转换成毫秒，所以要乘1000，这里使用int是不行的，必须要使用字符串
    public PacketHeader parseFilePacketHeader() {
        byte[] packet_header = Arrays.copyOfRange(this.data, 24, 40);
        int offset = 0;
        PacketHeader packetHeader = new PacketHeader();

        byte[] buff_4 = new byte[4];    // 4 字节的数组

        for (int i = 0; i < 4; i++) {
            buff_4[3- i] = packet_header[i + offset];
        }
        offset += 4;
        int timestampHigh = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setTimestampHigh(timestampHigh);

        for (int i = 0; i < 4; i++) {
            buff_4[3- i] = packet_header[i + offset];
        }
        offset += 4;
        int timestampLow = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setTimestampLow(timestampLow);

        for (int i = 0; i < 4; i++) {
            buff_4[3- i] = packet_header[i + offset];
        }
        offset += 4;
        int capLen = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setCapLen(capLen);

        for (int i = 0; i < 4; i++) {
            buff_4[3- i] = packet_header[i + offset];
        }
        offset += 4;
        int len = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setLen(len);

        return packetHeader;
    }

    // 解析数据链路层,一共14个字节
    /*
     *     |  6-bytes dis | 6-bytes sr | 2-bytes protocol type |
     *     |---------------------- 14bytes  -------------------|
     *     IPv4 中type为采用 0x0800
     */
    public DataLinkLayer parseDataLinkLayer() {
        DataLinkLayer dataLinkLayer = new DataLinkLayer();
        byte[] protocolBuffer = Arrays.copyOfRange(data, 40, 54);

        byte[] buff_6 = new byte[6];    // 6字节的缓冲区
        byte[] buff_2 = new byte[2];    // 2字节的缓冲区

        int offset = 0;
        for (int i = 0; i < 6; i++) {
            buff_6[i] = protocolBuffer[i + offset];
        }
        offset += 6;
        int source = DataUtils.byteArrayToInt(buff_6);
        dataLinkLayer.setSourceMac(source);

        for (int i = 0; i < 4; i++) {
            buff_6[i] = protocolBuffer[i + offset];
        }
        offset += 6;
        int dst = DataUtils.byteArrayToInt(buff_6);
        dataLinkLayer.setDstMac(dst);

        for (int i = 0; i < 2; i++) {
            buff_2[1 - i] = protocolBuffer[i + offset];
        }
        int protocol = DataUtils.byteArrayToShort(buff_2);    // 2048表示的就是ipv4
        dataLinkLayer.setProtocol(protocol);
        return dataLinkLayer;
    }


    public IPHeader parseIPLayer() {
        byte[] buff_2 = new byte[2];    // 2字节的缓冲区
        byte[] buff_1 = new byte[1];    // 1字节的缓冲区
        byte[] buff_4 = new byte[4];    // 4字节的缓冲区
        // 数据链路层14字节之后就是IP包的内容
        // IP包的首部长度不是固定的，可以从IP包的第一个字节的一半得出IP首部的长度，
        // 注意单位是4字节，然后根据这个长度截取相应的长度对IP首部进行解析。
        // 第一个字节的前四位是版本，第一个字节的后四位是包头长度
        // expect:0x45--->4:ipv4  5->20字节     69
        // 版本固定是64所以减去64 再 *4就是长度
//        System.out.println(data[54]);
//        System.out.println("---------");
////        System.out.println(Integer.toBinaryString(45));
//        // 16进制的0x45应该是1000101
//        String complex = DataUtils.convertFromIntToHexa(data[54]);
//        System.out.println(complex);

        IPHeader ipHeader = new IPHeader();

        int ipHeaderLen = (data[54] - 64) * 4;
        ipHeader.setHeaderLen(ipHeaderLen);
        System.out.println("IP头部的长度=" + ipHeaderLen);
        byte[] ipHeaderContext = new byte[ipHeaderLen];
        for (int i = 0; i < ipHeaderLen; i++) {
            ipHeaderContext[i] = data[54 + i];
        }

        // 总长度占两个字节
        buff_2 = Arrays.copyOfRange(ipHeaderContext,2, 4);
        int totalLen = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));
        ipHeader.setTotalLen(totalLen);


        buff_1 = Arrays.copyOfRange(ipHeaderContext, 8, 9);
        int ttl = buff_1[0];
        ipHeader.setTtl(ttl);

        buff_1 = Arrays.copyOfRange(ipHeaderContext, 9, 10);   // 6代表的是TCP
        int protocol = buff_1[0];
        ipHeader.setProtocol(protocol);

        buff_2 = Arrays.copyOfRange(ipHeaderContext, 10, 12);
        int checkSum = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));   // 校验和
        ipHeader.setCheckSum(DataUtils.convertFromIntToHexa(checkSum));

        buff_4 = Arrays.copyOfRange(ipHeaderContext, 12, 16);
        int srcIP = DataUtils.byteArrayToInt(buff_4);
        ipHeader.setSrcIP(DataUtils.convertFromIntToHexa(srcIP));

        buff_4 = Arrays.copyOfRange(ipHeaderContext, 16, 20);
        int dstIP = DataUtils.byteArrayToInt(buff_4);
        ipHeader.setDstIP(DataUtils.convertFromIntToHexa(dstIP));

        return ipHeader;
    }

    public TCPHeader parseTCPLayer() {
        byte[] buff_2 = new byte[2];   // 2位缓冲区
        byte[] buff_4 = new byte[4];   // 4位缓冲区

        byte[] TCPHeaderContext = Arrays.copyOfRange(data, 74, 94);  // 先不算OPTIONS字段
        TCPHeader tcpHeader = new TCPHeader();

        buff_2 = Arrays.copyOfRange(TCPHeaderContext, 0, 2);
        int sourcePort = DataUtils.byteArray2Int(DataUtils.reverseArray(buff_2), 2);
        System.out.println(DataUtils.convertFromIntToHexa(sourcePort));
        tcpHeader.setSourcePort(sourcePort);

        buff_2 = Arrays.copyOfRange(TCPHeaderContext, 2, 4);
        int dstPort = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));
        tcpHeader.setDstPort(dstPort);

        buff_4 = Arrays.copyOfRange(TCPHeaderContext, 4, 8);
        int seqNum = DataUtils.byteArrayToInt(buff_4);
        tcpHeader.setSequenceNumber(seqNum);

        buff_4 = Arrays.copyOfRange(TCPHeaderContext, 8, 12);
        int ackNum = DataUtils.byteArrayToInt(buff_4);
        tcpHeader.setAcknowledgmentNumber(ackNum);

        // 取2个字节，然后将16进制 --> 10进制， 10进制 --> 转成2进制，然后截取前4位，和后6位
        buff_2 = Arrays.copyOfRange(TCPHeaderContext, 12, 14);
        int tcpHeaderLen = ((buff_2[0] & 0xf0) >> 4) * 4;
        tcpHeader.setDataOffset(tcpHeaderLen);

        int flag = buff_2[1] & 0x3f;
        // System.out.println(flag);   // 000010，倒数第二个，分别是0，2，4，8，16，32
        tcpHeader.setFlag(flag);

        buff_2 = Arrays.copyOfRange(TCPHeaderContext, 14, 16);
        int window = DataUtils.byteArray2Int(buff_2, 2);
        System.out.println(Arrays.toString(buff_2));
        tcpHeader.setWindowSize(window);

        buff_2 = Arrays.copyOfRange(TCPHeaderContext, 16, 18);
        System.out.println(Arrays.toString(buff_2));
        int checkSum = DataUtils.byteArray2Int(buff_2, 2);
        tcpHeader.setCheckSum(checkSum);

        byte[] bytes = Arrays.copyOfRange(data, 94, 94 + tcpHeaderLen - 20);
        TCPHeaderOptions tcpHeaderOptions = new TCPHeaderOptions(bytes);
        tcpHeader.setTcpHeaderOptions(tcpHeaderOptions);
        return tcpHeader;
    }


}
