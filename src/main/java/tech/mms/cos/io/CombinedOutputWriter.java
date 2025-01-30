package tech.mms.cos.io;

import org.springframework.stereotype.Component;

@Component
public class CombinedOutputWriter implements OutputWriter {

    private final FileOutputWriter fileOutputWriter;
    private final ConsoleOutputWriter consoleOutputWriter;

    public CombinedOutputWriter(FileOutputWriter fileOutputWriter, ConsoleOutputWriter consoleOutputWriter) {
        this.fileOutputWriter = fileOutputWriter;
        this.consoleOutputWriter = consoleOutputWriter;
    }

    @Override
    public void println(Object text) {
        fileOutputWriter.println(text);
        consoleOutputWriter.println(text);
    }

    @Override
    public void error(String text) {
        fileOutputWriter.error(text);
        consoleOutputWriter.error(text);
    }

}
