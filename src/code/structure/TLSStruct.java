package code.structure;

/**
 * TLS层的报文格式
 */
public class TLSStruct {
    private int content_type;   // 握手类型，22代表client_hello
    private int version;        // TLS版本号
    private int length;         // TLS数据包的长度
    private TLSHandshake handshake;

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public TLSHandshake getHandshake() {
        return handshake;
    }

    public void setHandshake(TLSHandshake handshake) {
        this.handshake = handshake;
    }

    @Override
    public String toString() {
        return "TLSStruct{" +
                "content_type=" + content_type +
                ", version=" + version +
                ", length=" + length +
                ", handshake=" + handshake +
                '}';
    }
}
