package code.structure;

import code.utils.DataUtils;

public class PacketHeader {
    // 无论是GlobalHeader还是PacketHeader，因为他们其中的字段最多是4B，所以使用int存储足以
    // 即使是时间戳，也可以使用int
    private int timestampHigh;      // 时间戳高位
    private int timestampLow;       // 时间戳低位
    private int capLen;             // 当前数据区的长度
    private int len;                // 离线数据长度

    public PacketHeader(){}

    public int getTimestampHigh() {
        return timestampHigh;
    }

    public void setTimestampHigh(int timestampHigh) {
        this.timestampHigh = timestampHigh;
    }

    public int getTimestampLow() {
        return timestampLow;
    }

    public void setTimestampLow(int timestampLow) {
        this.timestampLow = timestampLow;
    }

    public int getCapLen() {
        return capLen;
    }

    public void setCapLen(int capLen) {
        this.capLen = capLen;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    @Override
    public String toString() {
        return "PacketHeader{" +
                "timestampHigh=" + DataUtils.unixTimeStampToDate(String.valueOf(timestampHigh)) +
                ", timestampLow=" + timestampLow +
                ", capLen=" + capLen +
                ", len=" + len +
                '}';
    }
}
