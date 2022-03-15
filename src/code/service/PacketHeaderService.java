package code.service;

import code.structure.PacketHeader;
import code.utils.DataUtils;

import java.util.Arrays;

/**
 * packetHeader 有16字节组成
 * timestamp，占4B，时间戳高位，精确到seconds，这是Unix时间戳。捕获数据包的时间一般是根据这个值。
 * timestamp，占4B，时间戳低位，能够精确到microseconds。
 * capLen，占4B，当前数据区的长度，即抓取到的数据帧长度，由此可以得到下一个数据帧的位置。
 * len，占4B，离线数据长度，网路中实际数据帧的长度，一般不大于capLen，
 * 多数情况下和capLen值一样。如果文件中保存不是完整的数据包，那么这个值可能要比前面的数据包长度的值大。
 */
public class PacketHeaderService {

    // 第一，因为是小端，所以他需要reverse，才能够得到正确的值，但是在小端模式中，地址不用反转，时间戳和长度需要反转
    // 第二，对于unix时间戳，要给转换成毫秒，所以要乘1000，这里使用int是不行的，必须要使用字符串
    public PacketHeader parseFilePacketHeader(byte[] packetHeaderBuffer, boolean isBigEndian, int index) {
        System.out.println("这是第" + index + "个数据包");
//        byte[] packetHeaderBuffer = Arrays.copyOfRange(this.data, 24, 40);
        int offset = 0;
        PacketHeader packetHeader = new PacketHeader();

        byte[] buff_4 = new byte[4];    // 4 字节的数组

        for (int i = 0; i < 4; i++) {
            buff_4[i] = packetHeaderBuffer[i + offset];
        }
        offset += 4;
        if (!isBigEndian) {   // 是小端模式
            buff_4 = DataUtils.reverseArray(buff_4);
        }
        int timestampHigh = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setTimestampHigh(timestampHigh);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = packetHeaderBuffer[i + offset];
        }
        offset += 4;
        if (!isBigEndian) {   // 是小端模式
            buff_4 = DataUtils.reverseArray(buff_4);
        }
        int timestampLow = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setTimestampLow(timestampLow);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = packetHeaderBuffer[i + offset];
        }
        offset += 4;
        if (!isBigEndian) {   // 是小端模式
            buff_4 = DataUtils.reverseArray(buff_4);
        }
        int capLen = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setCapLen(capLen);

        for (int i = 0; i < 4; i++) {
            buff_4[i] = packetHeaderBuffer[i + offset];
        }
        offset += 4;
        if (!isBigEndian) {   // 是小端模式
            buff_4 = DataUtils.reverseArray(buff_4);
        }
        int len = DataUtils.byteArrayToInt(buff_4);
        packetHeader.setLen(len);

        return packetHeader;
    }
}
