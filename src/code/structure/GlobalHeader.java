package code.structure;

import code.utils.DataUtils;

/**
 * code.structure.GlobalHeader 文件头结构
 */

public class GlobalHeader {
    public static final int LINK_TYPE_ETHERNET = 1;

    private String magic;      // Pcap 文件是大端模式存储（0xa1b2c3d4）还是小端存储（0xd4c3b2a1）
    private int major;      // 主版本号
    private int minor;      // 次版本号
    private int thisZone;   // 当地标准时间
    private int sigFigs;    // 时间戳精度
    private int snapLen;    // 最长存储长度
    private int linkType;   // 链路类型，以太网则为1，一般为1

    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public long getThisZone() {
        return thisZone;
    }

    public void setThisZone(int thisZone) {
        this.thisZone = thisZone;
    }

    public long getSigFigs() {
        return sigFigs;
    }

    public void setSigFigs(int sigFigs) {
        this.sigFigs = sigFigs;
    }

    public int getSnapLen() {
        return snapLen;
    }

    public void setSnapLen(int snapLen) {
        this.snapLen = snapLen;
    }

    public GlobalHeader() {
    }


    @Override
    public String toString() {
        return "GlobalHeader{" +
                "magic=" + magic +
                ", major=" + major +
                ", minor=" + minor +
                ", thisZone=" + thisZone +
                ", sigFigs=" + sigFigs +
                ", snapLen=" + snapLen +
                ", linkType=" + DataUtils.convertFromIntToHexa(linkType) +
                '}';
    }
}
