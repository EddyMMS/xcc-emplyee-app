package tech.mms.cos.io;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Primary
@Component
public class ConsoleOutputWriter implements OutputWriter {

    @Override
    public void println(Object text) {
        System.out.println("SYSTEM: " + text.toString());
    }

    @Override
    public void error(String text) {
        System.out.println("ERROR: " + text);
    }


}
