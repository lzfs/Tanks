package pp.tanks.client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manage the sounds of the Game
 */

public class Sounds {
    public MediaPlayer mainMenu;
    public MediaPlayer mediaPlayer;
    private boolean muted = false;

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
        mediaPlayer.setMute(muted);
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
     * init SFx
     *
     * @param sfx SFx to play
     */
    public void setSfx(MediaPlayer sfx) {
        mediaPlayer.pause();
        MediaPlayer cls = this.mediaPlayer;
        mediaPlayer = sfx;
        mediaPlayer.setMute(muted);
        mediaPlayer.setVolume(100.0);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = cls;
                //resetSfx();
                mediaPlayer.play();
            }
        });
    }

    /**
     * reset SFx to play them again
     */
    /*
    void resetSfx(){
        this.nameOfSound = new MediaPlayer(new Media(getClass().getResource("sounds/nameOfSound.mp3").toExternalForm()));
    }
     */

    /**
     * Mute sounds
     *
     * @param m mode of the mute (true==mute,false==unmute)
     */
    public void mute(boolean m) {
        muted = m;
        mediaPlayer.setMute(m);
    }

    /**
     * @return the muted attribute
     */
    public boolean getMuted() {
        return muted;
    }
}
