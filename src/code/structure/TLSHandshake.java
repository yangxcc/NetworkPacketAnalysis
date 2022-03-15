package code.structure;

import java.util.Arrays;

public class TLSHandshake {
    private int type;    // handshake type
    private int len;
    private int version;
    private String random;
    private int sessionID;
    private int cipherSuitesLen;
    private String[] cipherSuites;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getCipherSuitesLen() {
        return cipherSuitesLen;
    }

    public void setCipherSuitesLen(int cipherSuitesLen) {
        this.cipherSuitesLen = cipherSuitesLen;
    }

    public String[] getCipherSuites() {
        return cipherSuites;
    }

    public void setCipherSuites(String[] cipherSuites) {
        this.cipherSuites = cipherSuites;
    }

    @Override
    public String toString() {
        return "TLSHandshake{" +
                "type=" + type +
                ", len=" + len +
                ", version=" + version +
                ", random='" + random + '\'' +
                ", sessionID=" + sessionID +
                ", cipherSuitesLen=" + cipherSuitesLen +
                ", cipherSuites=" + Arrays.toString(cipherSuites) +
                '}';
    }
}
