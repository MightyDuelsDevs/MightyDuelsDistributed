/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controller;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Martijn
 */
public class SoundController {

    /**
     * Enumerator for all the different sound files.
     */
        public static enum SoundFile {

        /**
         * The sound when a button is pressed.
         */
        BUTTONPRESS("src/Client/Resources/Sound/buttonPress.wav"),

        /**
         * The sound when a turn ends.
         */
        ENDTURN("src/Client/Resources/Sound/endTurn.wav"),

        /**
         * The sound when heal is the highest value played.
         */
        HEAL("src/Client/Resources/Sound/heal.wav"),

        /**
         * The sound when magical attack is the highest value played.
         */
        MAGICALATTACK("src/Client/Resources/Sound/magicalAttack.wav"),

        /**
         * The sound when magical block is the highest value played.
         */
        MAGICALBLOCK("src/Client/Resources/Sound/magicalBlock.wav"),

        /**
         * The sound when a minion is played.
         */
        MINION("src/Client/Resources/Sound/minion.wav"),

        /**
         * The sound when physical attack is the highest value played.
         */
        PHYSICALATTACK("src/Client/Resources/Sound/physicalAttack.wav"),

        /**
         * The sound when physical block is the highest value played.
         */
        PHYSICALBLOCK("src/Client/Resources/Sound/physicalBlock.wav"),

        /**
         * The sound when a turn starts.
         */
        STARTTURN("src/Client/Resources/Sound/startTurn.wav");

        private final String audioFilePath;

        private SoundFile(String audioFilePath) {
            this.audioFilePath = audioFilePath;
        }

        /**
         * Method that returns the String that corresponds to the file path of the file.
         * @return The file path of the audio file.
         */
        public String getAudioFilePath() {
            return audioFilePath;
        }
    }

    /**
     * Play a given audio file.
     *
     * @param audioFile the audio file.
     */
    public static void play(SoundFile audioFile) {

        class playAudioRunnable implements Runnable, LineListener {

            /**
             * this flag indicates whether the playback completes or not.
             */
            boolean playCompleted;
            String audioFilePath;

            playAudioRunnable(String audioFilePath) {
                this.audioFilePath = audioFilePath;
            }

            @Override
            public void run() {
                File audioFile = new File(audioFilePath);

                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    AudioFormat format = audioStream.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);
                    Clip audioClip = (Clip) AudioSystem.getLine(info);
                    audioClip.addLineListener(this);
                    audioClip.open(audioStream);
                    audioClip.start();

                    while (!playCompleted) {
                        // wait for the playback completes
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                    audioClip.close();

                } catch (UnsupportedAudioFileException ex) {
                    System.out.println("The specified audio file is not supported.");
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    System.out.println("Audio line for playing back is unavailable.");
                    ex.printStackTrace();
                } catch (IOException ex) {
                    System.out.println("Error playing the audio file.");
                    ex.printStackTrace();
                }
            }

            /**
             * Listens to the START and STOP events of the audio line.
             */
            @Override
            public void update(LineEvent event) {
                LineEvent.Type type = event.getType();

                if (type == LineEvent.Type.START) {
                    System.out.println("Playback started.");

                } else if (type == LineEvent.Type.STOP) {
                    playCompleted = true;
                    System.out.println("Playback completed.");
                }
            }
        }

        Thread audioPlayThread = new Thread(new playAudioRunnable(audioFile.getAudioFilePath()));
        audioPlayThread.start();
    }
}
