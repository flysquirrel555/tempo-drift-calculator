import javax.sound.sampled.*;

public class SoundUtils {

    public static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs)
            throws LineUnavailableException {
        tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol)
            throws LineUnavailableException {
    }

    public static class Tone {
        private final AudioFormat af;
        private final int hz;
        private final int msecs;
        private final double vol;
        byte[] buffer;

        private Tone(int hz, int msecs, double vol, AudioFormat af, byte[] buffer) {
            this.af = af;
            this.hz = hz;
            this.msecs = msecs;
            this.vol = vol;
            this.buffer = buffer;
        }

        public static Tone make(int hz, int msecs) throws LineUnavailableException {
            return Tone.make(hz, msecs, 1.0);
        }

        public static Tone make(int hz, int msecs, double vol) throws LineUnavailableException {
            byte[] buffer = new byte[msecs * 8];
            AudioFormat af =
                    new AudioFormat(
                            SAMPLE_RATE, // sampleRate
                            8,           // sampleSizeInBits
                            1,           // channels
                            true,        // signed
                            false);      // bigEndian


            for (int i = 0; i < msecs * 8; i++) {
                double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
                buffer[i] = (byte) (Math.sin(angle) * 127.0 * vol);
            }

            return new Tone(hz, msecs, vol, af, buffer);
        }

        public long play() throws LineUnavailableException {
            long diff = System.currentTimeMillis();
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            sdl.write(buffer, 0, msecs * 8);
            diff = System.currentTimeMillis() - diff;
            sdl.drain();
            sdl.stop();
            sdl.close();

            return diff;
        }
    }
}
