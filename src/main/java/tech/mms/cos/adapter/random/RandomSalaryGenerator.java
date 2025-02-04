package tech.mms.cos.adapter.random;

import java.util.Random;

public class RandomSalaryGenerator {

    private static final Random random = new Random();


    public static double randomHourlyRate() {
        return 1 + (30 - 1) * random.nextDouble();
    }

    public static int randomHoursPerWeek() {
        return 2 + random.nextInt(40 - 2 + 1);
    }

}
