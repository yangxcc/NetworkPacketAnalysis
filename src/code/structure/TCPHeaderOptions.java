package code.structure;

import code.utils.DataUtils;

import java.util.Arrays;

public class TCPHeaderOptions {
    private byte[] bytes;

    private int MSS;     //  4字节
    private int sack;    //  重传相关，2字节
    private long timeStamp;  // 时间戳，10字节
    private int nop;        // tcp-option : NO-Operation，1字节
    private int windowScale;   // 窗口规模，需要除128，3字节

    // optionsLen = TCP头部的长度（dataOffset - 20字节）
    public TCPHeaderOptions(byte[] bytes) {
        this.bytes = bytes;
        this.MSS = DataUtils.byteArray2Int(Arrays.copyOfRange(bytes, 2, 4), 2);
        this.sack = Arrays.copyOfRange(bytes, 4, 6)[0];
        this.timeStamp = Long.parseLong(DataUtils.convertFromIntToHexa(DataUtils.byteArrayToInt(Arrays.copyOfRange(bytes, 8, 12))), 16);
        // System.out.println(Long.parseLong(DataUtils.convertFromIntToHexa(DataUtils.byteArrayToInt(Arrays.copyOfRange(bytes, 8, 12))), 16));
        this.nop = Arrays.copyOfRange(bytes, 16, 17)[0];
        this.windowScale = Arrays.copyOfRange(bytes, 17, 20)[2];
}

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getMSS() {
        return MSS;
    }

    public void setMSS(int MSS) {
        this.MSS = MSS;
    }

    public int getSack() {
        return sack;
    }

    public void setSack(int sack) {
        this.sack = sack;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNop() {
        return nop;
    }

    public void setNop(int nop) {
        this.nop = nop;
    }

    public int getWindowScale() {
        return windowScale;
    }

    public void setWindowScale(int windowScale) {
        this.windowScale = windowScale;
    }

    @Override
    public String toString() {
        return "TCPHeaderOptions{" +
//                "bytes=" + Arrays.toString(bytes) +
                " MSS=" + MSS +
                ", sack=" + sack +
                ", timeStamp=" + timeStamp +
                ", nop=" + nop +
                ", windowScale=" + windowScale +
                '}';
    }
}
