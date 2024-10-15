package tech.mms.cos.io;

import tech.mms.cos.io.OutputWriter;

public class FileOutputWriter implements OutputWriter {
    @Override
    public void println(Object text) {
        // Write to file
    }

    @Override
    public void error(String text) {
        // Write to error file
    }
}
