import java.io.IOException;
//import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// import javazoom.jl.decoder.JavaLayerException;
// import javazoom.jl.player.*;

public class TextToSpeech {
    public void getSpeech(String target) {
        if (target == null || target.equals("")) {
            return;
        }
        try {
            String url = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=en&q="
                    + URLEncoder.encode(target, "UTF-8");
            URL myURL = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            System.setProperty("http.agent", "Chrome");

            // InputStream audioSrc = urlConnection.getInputStream();

            // Player player = new Player(audioSrc);

            // player.play();

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(myURL);
            Clip clip = AudioSystem.getClip();

            clip.open(audioIn);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("Cannot access URL");
            e.printStackTrace();
        }
    }
}
