package tech.mms.cos.io;

import org.springframework.stereotype.Component;
import tech.mms.cos.config.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class FileOutputWriter implements OutputWriter {

    private final String logFilePath;

    public FileOutputWriter(Config config) {
        this.logFilePath = config.getOutputFilePath();
    }

    @Override
    public void println(Object text) {
        writeToFile(text.toString());
    }

    @Override
    public void error(String text) {
        writeToFile(text);
    }

    private void writeToFile(String text) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            writer.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
