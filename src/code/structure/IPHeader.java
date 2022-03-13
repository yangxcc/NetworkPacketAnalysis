package code.structure;

public class IPHeader {
    private int headerLen;   // IP头部长度
    private int totalLen;    // IP包的总长度

    private int ttl;         // TTL

    private int protocol;    // 协议类型

    private String checkSum;    // 校验和

    private String srcIP;       // 源IP

    private String dstIP;       // 目标IP

    public IPHeader() {
    }

    public int getHeaderLen() {
        return headerLen;
    }

    public void setHeaderLen(int headerLen) {
        this.headerLen = headerLen;
    }

    public int getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }

    public String getDstIP() {
        return dstIP;
    }

    public void setDstIP(String dstIP) {
        this.dstIP = dstIP;
    }

    @Override
    public String toString() {
        return "IPHeader{" +
                "headerLen=" + headerLen +
                ", totalLen=" + totalLen +
                ", ttl=" + ttl +
                ", protocol=" + protocol +
                ", checkSum=" + checkSum +
                ", srcIP=" + srcIP +
                ", dstIP=" + dstIP +
                '}';
    }
}
