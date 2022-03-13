package code.structure;

public class TCPHeader {
    private int sourcePort;   // 源端口，16位，2字节
    private int dstPort;      // 目的端口，16位，2字节
    private int SequenceNumber;   // 序列号,32位，4字节
    private int AcknowledgmentNumber;   // 确认序列号，32位，4字节
    private int dataOffset;        // 这个数其实代表的就是TCP包头的长度
    private int flag;              // flags共6位，看看哪一位被标记成了1
    // dataOffset + 保留位 + flag = 4 + 6 + 6，共16位，2个字节
    private int windowSize;        // 2个字节，窗口大小
    private int checkSum;          // 校验和，共16位，2个字节
    private TCPHeaderOptions tcpHeaderOptions;

    public TCPHeader(){
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    public int getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public int getAcknowledgmentNumber() {
        return AcknowledgmentNumber;
    }

    public void setAcknowledgmentNumber(int acknowledgmentNumber) {
        AcknowledgmentNumber = acknowledgmentNumber;
    }

    public int getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public TCPHeaderOptions getTcpHeaderOptions() {
        return tcpHeaderOptions;
    }

    public void setTcpHeaderOptions(TCPHeaderOptions tcpHeaderOptions) {
        this.tcpHeaderOptions = tcpHeaderOptions;
    }

    @Override
    public String toString() {
        return "TCPHeader{" +
                "sourcePort=" + sourcePort +
                ", dstPort=" + dstPort +
                ", SequenceNumber=" + SequenceNumber +
                ", AcknowledgmentNumber=" + AcknowledgmentNumber +
                ", dataOffset=" + dataOffset +
                ", flag=" + flag +
                ", windowSize=" + windowSize +
                ", checkSum=" + checkSum +
                ", tcpHeaderOptions=" + tcpHeaderOptions +
                '}';
    }
}
