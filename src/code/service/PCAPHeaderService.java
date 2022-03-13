package code.service;

import code.structure.GlobalHeader;
import code.utils.DataUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * 解析 pcap 文件头，GlobalHeader一共24字节
 * magic，占4B，如果它的值是0xa1b2c3d4，代表 Pcap 文件是大端模式存储的；如果它的值是 0xd4c3b2a1，代表 Pcap 文件是小端模式存储的。这里注意这里的大端小端仅仅是指 Pcap 文件的 Global Header 和 Packet Header，而无关Packet Data 里的内容。Packet Data里面就是利用抓包工具如wireshark捕获的数据包，他们都是符合网络字节序的，而网络字节序就是大端模式。如果对大端模式和小端模式不熟悉的童靴可以先去看一下，在回来学习，因为不知道这个解析包一定会有问题。
 * major，占2B，文件的主版本号，一般为0x0200。
 * minor，占2B，文件的次要版本号，一般为0x0400。
 * thisZone，占4B，当地标准时间，如果是GMT则全为0。
 * sigFigs，占4B，时间戳精度，一般全0。
 * snapLen，占4B，最长存储长度。
 * linkType，占4B，链路类型，以太网则为1，一般为1。
 */

public class PCAPHeaderService {

    public GlobalHeader parseFileHeader(byte[] fileHeaderBuffer) throws IOException {
        // 是一个左闭右开的区间，GlobalHeader共24B，前24B
//        byte[] fileHeaderBuffer = Arrays.copyOfRange(this.data, 0, 24);
//        fileHeaderBuffer = this.data;
        GlobalHeader fileHeader = new GlobalHeader();
        byte[] buff_4 = new byte[4];    // 4 字节的数组
        byte[] buff_2 = new byte[2];    // 2 字节的数组

        int offset = 0;
        for (int i = 0; i < 4; i++) {
            buff_4[i] = fileHeaderBuffer[i + offset];
        }
        offset += 4;
        int magic = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setMagic(DataUtils.convertFromIntToHexa(magic));

        for (int i = 0; i < 2; i++) {
            buff_2[i] = fileHeaderBuffer[i + offset];
        }
        offset += 2;
        short majorVersion = DataUtils.byteArrayToShort(buff_2);
        fileHeader.setMajor(majorVersion);

        for (int i = 0; i < 2; i++) {
            buff_2[i] = fileHeaderBuffer[i + offset];
        }
        offset += 2;
        short minorVersion = DataUtils.byteArrayToShort(buff_2);
        fileHeader.setMinor(minorVersion);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = fileHeaderBuffer[i + offset];
        }
        offset += 4;
        int timezone = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setThisZone(timezone);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = fileHeaderBuffer[i + offset];
        }
        offset += 4;
        int sigflags = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setSigFigs(sigflags);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = fileHeaderBuffer[i + offset];
        }
        offset += 4;
        int snaplen = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setSnapLen(snaplen);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = fileHeaderBuffer[i + offset];
        }
        offset += 4;
        int linktype = DataUtils.byteArrayToInt(buff_4);
        fileHeader.setLinkType(linktype);

//      LogUtils.printObjInfo(fileHeader);

        return fileHeader;
    }
}
