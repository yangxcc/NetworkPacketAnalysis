package code.service;

import code.structure.IPHeader;
import code.utils.DataUtils;

import java.util.Arrays;

public class IPLayerService {
    public IPHeader parseIPLayer(byte[] ipHeaderBuffer) {
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

//        int ipHeaderLen = (data[54] - 64) * 4;
        int ipHeaderLen = ipHeaderBuffer.length;
        ipHeader.setHeaderLen(ipHeaderLen);
//        System.out.println("IP头部的长度=" + ipHeaderLen);
//        byte[] ipHeaderContext = new byte[ipHeaderLen];
//        for (int i = 0; i < ipHeaderLen; i++) {
//            ipHeaderContext[i] = data[54 + i];
//        }

        // 总长度占两个字节
        buff_2 = Arrays.copyOfRange(ipHeaderBuffer,2, 4);
        int totalLen = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));
        ipHeader.setTotalLen(totalLen);


        buff_1 = Arrays.copyOfRange(ipHeaderBuffer, 8, 9);
        int ttl = buff_1[0];
        ipHeader.setTtl(ttl);

        buff_1 = Arrays.copyOfRange(ipHeaderBuffer, 9, 10);   // 6代表的是TCP
        int protocol = buff_1[0];
        ipHeader.setProtocol(protocol);

        buff_2 = Arrays.copyOfRange(ipHeaderBuffer, 10, 12);
        int checkSum = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));   // 校验和
        ipHeader.setCheckSum(DataUtils.convertFromIntToHexa(checkSum));

        buff_4 = Arrays.copyOfRange(ipHeaderBuffer, 12, 16);
        int srcIP = DataUtils.byteArrayToInt(buff_4);
        ipHeader.setSrcIP(DataUtils.convertFromIntToHexa(srcIP));

        buff_4 = Arrays.copyOfRange(ipHeaderBuffer, 16, 20);
        int dstIP = DataUtils.byteArrayToInt(buff_4);
        ipHeader.setDstIP(DataUtils.convertFromIntToHexa(dstIP));

        return ipHeader;
    }
}
