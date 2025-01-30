package tech.mms.cos.io;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleInputReader {

    Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine();
    }

    public int readInt() {
        String input = scanner.nextLine();
        return Integer.parseInt(input);
    }

    public double readDouble() {
        String input = scanner.nextLine();
        return Double.parseDouble(input);
    }

}
