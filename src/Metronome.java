import javax.sound.sampled.LineUnavailableException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.concurrent.*;

public class Metronome {
    public static long currentTime;
    static String previous = "999999999";
    public static void main(String[] args) {

    }
    /**
     *
     * @param bpm
     * @param duration The amount of times the sound will play. 0 to play infinitely
     * @param hz
     * @param msecs
     */
    public static ScheduledFuture<?> play(int bpm, int duration, int hz, int msecs) throws LineUnavailableException {
        final long waitDuration = (long)((60.0 / bpm) * 1000);
        if(waitDuration < msecs) {
            throw new InvalidParameterException("Beats per minute duration (" + waitDuration + "ms) must not be " +
                    "less than the audio duration (" + msecs + "ms).");
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        ExecutorService soundThread = Executors.newSingleThreadExecutor();
        SoundUtils.Tone tone = SoundUtils.Tone.make(hz, msecs);
        return executorService.scheduleAtFixedRate(() -> {
            try {
                long diff = System.currentTimeMillis();
                diff += tone.play();
                currentTime = System.currentTimeMillis() - (System.currentTimeMillis() - diff);
                String stepOne = String.valueOf(diff);
                stepOne = stepOne.substring(6);
                try {
                    previous = timeKeeper(stepOne,previous);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }, 0, waitDuration, TimeUnit.MILLISECONDS);

    }
    public static String timeKeeper(String time, String previous) throws FileNotFoundException {
        previous = previous +"\n" + time;
        writeFile(previous);
        return previous;
    }
    public static void writeFile(String time) throws FileNotFoundException {
        PrintWriter outFile = new PrintWriter("metlogs.txt");
        outFile.println(time);
        outFile.close();
}}
