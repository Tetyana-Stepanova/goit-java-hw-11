import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

interface NumberProcessor {
    String process(String number);
}

public class FizzBuzzNew {
    public static void main(String[] args) {

        ProcessThread fizzBuzz = new ProcessThread((n) -> {
            Integer k;
            try {
                k = Integer.valueOf(n);
            } catch (Exception ex) {
                return n;
            }
            if (k % 15 == 0) {
                return "FizzBuzz";
            } else {
                return n;
            }
        });

        ProcessThread fizz = new ProcessThread((n) -> {
            Integer k;
            try {
                k = Integer.valueOf(n);
            } catch (Exception ex) {
                return n;
            }
            if (k % 3 == 0 & k % 5 != 0) {
                return "Fizz";
            } else {
                return n;
            }
        });

        ProcessThread buzz = new ProcessThread((n) -> {
            Integer k;
            try {
                k = Integer.valueOf(n);
            } catch (Exception ex) {
                return n;
            }
            if (k % 5 == 0 & k % 3 != 0) {
                return "Buzz";
            } else {
                return n;
            }
        });

        ProcessThread number = new ProcessThread((n) -> n);

        List<ProcessThread> threads = new ArrayList<>();
        threads.add(fizzBuzz);
        threads.add(fizz);
        threads.add(buzz);
        threads.add(number);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input number: ");
        int total = scanner.nextInt();
        List<String> result = new CopyOnWriteArrayList<>();
        for (int i = 1; i < total + 1; i++) {
            result.add(String.valueOf(i));
        }
        for (ProcessThread thread : threads) {
            thread.process(result);
            thread.start();
        }

        for (ProcessThread thread : threads) {
            while (!Thread.State.TERMINATED.equals(thread.getState())) {  //we are waiting for all threads are finished
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    System.out.println("Interrupted");
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            if( i == result.size()-1){
                System.out.print(result.get(i));
            }
            else{
                System.out.print(result.get(i)+", ");
            }
        }
    }
}

class ProcessThread extends Thread {
    List<String> result;
    private AtomicBoolean processed = new AtomicBoolean(true);
    private NumberProcessor processor;

    public ProcessThread(NumberProcessor processor) {
        this.processor = processor;
    }

    public void process(List<String> result) {
        this.result = result;
        processed.set(false);
    }

    @Override
    public void run() {
        for (int i = 0; i < result.size(); i++) {
            String value = result.get(i);
            result.set(i, processor.process(value));
        }
    }
}
