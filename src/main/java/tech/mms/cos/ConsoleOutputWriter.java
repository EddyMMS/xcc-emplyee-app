package tech.mms.cos;

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
