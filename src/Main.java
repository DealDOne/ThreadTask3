import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger threeChars = new AtomicInteger();
    static AtomicInteger fourChars = new AtomicInteger();
    static AtomicInteger fiveChars = new AtomicInteger();

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean firstCase(String someWord) {
        for (int i = 0; i < someWord.length() / 2; i++) {
            if (someWord.charAt(i) != someWord.charAt(someWord.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean secondCase(String someWord) {
        for (int i = 0; i < someWord.length() - 1; i++) {
            if (someWord.charAt(i) != someWord.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static boolean thirdCase(String someWord) {
        for (int i = 0; i < someWord.length() - 1; i++) {
            if (someWord.charAt(i) != someWord.charAt(i + 1) && someWord.charAt(i) > someWord.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static void addCaseCount(String someWord) {
        if (someWord.length() == 3){
            threeChars.addAndGet(1);
        } else if (someWord.length() == 4){
            fourChars.addAndGet(1);
        } else if (someWord.length() == 5){
            fiveChars.addAndGet(1);
        }
    }


    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

       Thread firstCase = new Thread(() -> {
            for (String someWord: texts){
                if (Main.firstCase(someWord)){
                    Main.addCaseCount(someWord);
                }
            }
        });

        Thread secondCase = new Thread(() -> {
            for (String someWord: texts){
                if (Main.secondCase(someWord)){
                    Main.addCaseCount(someWord);
                }
            }
        });
        Thread thirdCase = new Thread(() -> {
            for (String someWord: texts){
                if (Main.thirdCase(someWord)){
                    Main.addCaseCount(someWord);
                }
            }
        });
        firstCase.start();
        secondCase.start();
        thirdCase.start();
        try {
            firstCase.join();
            secondCase.join();
            thirdCase.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Красивых слов с длиной 3: " + threeChars + " шт.");
        System.out.println("Красивых слов с длиной 4: " + fourChars + " шт.");
        System.out.println("Красивых слов с длиной 5: " + fiveChars + " шт.");
    }

}