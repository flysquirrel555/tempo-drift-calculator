import javax.sound.sampled.LineUnavailableException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.concurrent.*;

public class Metronome {
    public static long currentTime;
    static int count = 0;
    static int beat;
    public static long diff;

    public static void main(String[] args) {

    }

    /**
     * @param bpm
     * @param hz
     * @param msecs
     */
    public static ScheduledFuture<?> play(int bpm, int hz, int msecs) throws LineUnavailableException {
        final long waitDuration = (long) ((60.0 / bpm) * 1000);
        if (waitDuration < msecs) {
            throw new InvalidParameterException("Beats per minute duration (" + waitDuration + "ms) must not be " +
                    "less than the audio duration (" + msecs + "ms).");
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        ExecutorService soundThread = Executors.newSingleThreadExecutor();
        SoundUtils.Tone tone = SoundUtils.Tone.make(hz, msecs);
        return executorService.scheduleAtFixedRate(() -> {
            try {
                diff = System.currentTimeMillis();
                if (count < 10) {
                diff += tone.play();
                }
                currentTime = System.currentTimeMillis() - (System.currentTimeMillis() - diff);

                count++;

            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }, 0, waitDuration, TimeUnit.MILLISECONDS);


    }

    public static void writeFile(String time) throws FileNotFoundException {
        PrintWriter outFile = new PrintWriter("metlogs.txt");
        outFile.println(time);
        outFile.close();
    }

    public static int getBeat() {
        System.out.println(beat);
        return beat;
    }

    public static int getCount() {
        System.out.println(count);
        return count;
    }

    public static String getCurrentTime() {
        return String.valueOf(diff);
    }
}
