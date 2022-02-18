public class Threads {
    public static void main (String [] args) throws InterruptedException{
        Chronometer start=new Chronometer();
        Messenger t1=new Messenger( start);
        Messenger t2=new Messenger( start);
        t1.countTime=10;
        t1.isEverySecond=true;
        t2.countTime=10;
        t2.isEverySecond=false;
        new Thread(t1, "t1").start();
        new Thread(t2, "t2").start();
        start.countTime(t1, t2, 10);
    }
}

class Chronometer {
    public int time=0;
    public void countTime (Messenger m, Messenger m1, int period) {
        for (int i=0; i<period; i++){
            synchronized(this) {
                time++;
                System.out.println(time);
                if (i==period-1) Messenger.finish=true;
                m.flag=false;
                this.notify();
                m1.flag=false;
                this.notify();
            }
            try {   Thread.sleep(1000);}
            catch (InterruptedException e) {};
        }
    }
}
class Messenger implements Runnable{
    public final Chronometer ch;
    public static boolean finish=false;
    public boolean flag=true;
    public int countTime;
    public boolean isEverySecond;
    Messenger(Chronometer ch) {
        this.ch=ch;
    }

   public void waitForTime() {
       while (true) {
           synchronized (ch) {
               try {
                   if (isEverySecond)
                       System.out.println("Thread at intervals 1");
                   if(!isEverySecond && ch.time % 5 ==0)
                       System.out.println("5 seconds have passed");
                   ch.wait();
                   if (countTime == ch.time) return;
               } catch (InterruptedException e) {
               }

           }
       }
   }

    public void run()  {
        waitForTime();
        System.out.println("The end");
    }
}
