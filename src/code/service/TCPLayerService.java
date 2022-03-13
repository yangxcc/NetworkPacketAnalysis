package code.service;

import code.structure.TCPHeader;
import code.structure.TCPHeaderOptions;
import code.utils.DataUtils;

import java.util.Arrays;

public class TCPLayerService {
    public TCPHeader parseTCPLayer(byte[] tcpHeaderBuffer) {
        byte[] buff_2 = new byte[2];   // 2位缓冲区
        byte[] buff_4 = new byte[4];   // 4位缓冲区

//        byte[] tcpHeaderBuffer = Arrays.copyOfRange(data, 74, 94);  // 先不算OPTIONS字段
        TCPHeader tcpHeader = new TCPHeader();

        buff_2 = Arrays.copyOfRange(tcpHeaderBuffer, 0, 2);
        int sourcePort = DataUtils.byteArray2Int(DataUtils.reverseArray(buff_2), 2);
//        System.out.println(DataUtils.convertFromIntToHexa(sourcePort));
        tcpHeader.setSourcePort(sourcePort);

        buff_2 = Arrays.copyOfRange(tcpHeaderBuffer, 2, 4);
        int dstPort = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));
        tcpHeader.setDstPort(dstPort);

        buff_4 = Arrays.copyOfRange(tcpHeaderBuffer, 4, 8);
        int seqNum = DataUtils.byteArrayToInt(buff_4);
        tcpHeader.setSequenceNumber(seqNum);

        buff_4 = Arrays.copyOfRange(tcpHeaderBuffer, 8, 12);
        int ackNum = DataUtils.byteArrayToInt(buff_4);
        tcpHeader.setAcknowledgmentNumber(ackNum);

        // 取2个字节，然后将16进制 --> 10进制， 10进制 --> 转成2进制，然后截取前4位，和后6位
        buff_2 = Arrays.copyOfRange(tcpHeaderBuffer, 12, 14);
        int tcpHeaderLen = ((buff_2[0] & 0xf0) >> 4) * 4;
        tcpHeader.setDataOffset(tcpHeaderLen);

        int flag = buff_2[1] & 0x3f;
        // System.out.println(flag);   // 000010，倒数第二个，分别是0，2，4，8，16，32
        tcpHeader.setFlag(flag);

        buff_2 = Arrays.copyOfRange(tcpHeaderBuffer, 14, 16);
        int window = DataUtils.byteArray2Int(buff_2, 2);
//        System.out.println(Arrays.toString(buff_2));
        tcpHeader.setWindowSize(window);

        buff_2 = Arrays.copyOfRange(tcpHeaderBuffer, 16, 18);
//        System.out.println(Arrays.toString(buff_2));
        int checkSum = DataUtils.byteArray2Int(buff_2, 2);
        tcpHeader.setCheckSum(checkSum);

//        byte[] bytes = Arrays.copyOfRange(data, 94, 94 + tcpHeaderLen - 20);
//        byte[] options = Arrays.copyOfRange(tcpHeaderBuffer, 20, tcpHeaderLen);
//        TCPHeaderOptions tcpHeaderOptions = new TCPHeaderOptions(options);
//        tcpHeader.setTcpHeaderOptions(tcpHeaderOptions);
        return tcpHeader;
    }
}
