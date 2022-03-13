package code.test;

import code.PCAPFileParse;

import java.io.File;

public class TestParse {

    public static void main(String[] args) {
        File file = new File("E:\\idea-workspace\\network\\src\\file\\Line2.pcap");
        PCAPFileParse fileParse = new PCAPFileParse();
        fileParse.parsePCAP(file);
    }
}
