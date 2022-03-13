package code.structure;

import code.utils.DataUtils;

public class DataLinkLayer {
    // 包中数据链路层共有14个字节，6个字节的源MAC，6个字节的目标MAC，2个字节的协议类型
    private int sourceMac;
    private int dstMac;
    private int protocol;

    public DataLinkLayer() {
    }

    public int getSourceMac() {
        return sourceMac;
    }

    public void setSourceMac(int sourceMac) {
        this.sourceMac = sourceMac;
    }

    public int getDstMac() {
        return dstMac;
    }

    public void setDstMac(int dstMac) {
        this.dstMac = dstMac;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "DataLinkLayer{" +
                "sourceMac=" + DataUtils.convertFromIntToHexa(sourceMac) +
                ", dstMac=" + DataUtils.convertFromIntToHexa(dstMac) +
                ", protocol=" + protocol +
                '}';
    }
}
