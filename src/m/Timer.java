package m;

import javafx.application.Platform;

public class Timer implements Runnable {

    Runnable updating;
    boolean running;

    public Timer(Runnable updating) {
        this.updating = updating;
        this.running = true;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run(){
        while(running) {
            try {
                Thread.sleep(1000);
            } catch ( InterruptedException e ) {
            }
            Platform.runLater(updating);
        }
    }
}
