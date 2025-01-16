package tech.mms.cos.io;

public interface OutputWriter {

    void println(Object text);

    void error(String text);

}

// ComposedOutputWriter -> File & Console ohne Codeduplication
// Composition over Inheritance
// Log File erstellen und zuweisen (log.txt)
// Composed Class erstellen und am Ende Code ausführen und testen ob geloggt wurde