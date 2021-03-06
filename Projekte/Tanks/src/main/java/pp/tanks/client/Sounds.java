package pp.tanks.client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manage the sounds of the Game
 */

public class Sounds {
    public MediaPlayer mainMenu;
    public MediaPlayer mediaPlayer;
    private boolean mutedMusic = false;

    /**
     * load all SFx and Music
     */
    public Sounds() {
        this.mainMenu = new MediaPlayer(new Media(getClass().getResource("sounds/MainMenuSound.mp3").toExternalForm()));
    }

    /**
     * change played music
     *
     * @param music next music
     */
    public void setMusic(MediaPlayer music) {
        double n = 0.0;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = music;
        mediaPlayer.setMute(mutedMusic);
        mediaPlayer.setVolume(n);
        mediaPlayer.play();
        mediaPlayer.setAutoPlay(true);
        while (n < 80.0) {
            n += 0.2;
            mediaPlayer.setVolume(n);
        }
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                setMusic(mediaPlayer);
            }
        });
    }

    /**
     * Mute sounds
     *
     * @param m mode of the mute (true==mute,false==unmute)
     */
    public void mute(boolean m) {
        mutedMusic = m;
        mediaPlayer.setMute(m);
    }

    /**
     * @return the muted attribute
     */
    public boolean getMutedMusic() {
        return mutedMusic;
    }
}
