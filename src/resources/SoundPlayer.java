package resources;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SoundPlayer implements LineListener{
	File soundFile;
	File soundFile2;
	String songPath;
	private AudioFormat format;
	Clip clip;
	public SoundPlayer (){
	}
	public boolean playing = false;
	/**
	 * plays the given song
	 * @param songPath a filepath that leads to the song
	 * @param volume the volume to play it at
	 */
	public void play (String songPath, float volume){
		try {
		clip.stop();
		} catch (NullPointerException e) {
			
		}
		this.songPath = songPath;
		soundFile = new File (songPath);
		try {
		AudioInputStream ais;
		ais = AudioSystem.getAudioInputStream(soundFile);
		format = ais.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		clip = (Clip) AudioSystem.getLine (info);
		clip.addLineListener(this);
		clip.open (ais);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume);
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		playing = true;
		} catch (Exception e){
		System.out.println("whoops (error message) ");
		System.out.println(songPath);
		}
		
	}
	public void setVolume(float volume) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume);
	}
	/**
	 * gets the path of the currently playing song
	 * @return that ya dingus
	 */
	 public String getSongPath () {
		if (songPath == null) {
			return "";
		}
		return songPath;
		 
	 }
	/**
	 * returns true if the song is playing
	 * @return true if the song is playing
	 */
	public boolean isPlaying () {
		return playing;
	}
	/**
	 * plays a sound effect (nessasary becasue songs and sounds need to be played on diffrent clips to play at the same time)
	 * @param volume the volume to play the effect at
	 * @param effectName the name of the sound effect
	 */
	public void playSoundEffect (float volume, String effectName){
		Clip clip2;
		soundFile2 = new File (effectName);
		try {
			AudioInputStream ais2;
			AudioFormat format2;
		ais2 = AudioSystem.getAudioInputStream(soundFile2);
		format2 = ais2.getFormat();
		DataLine.Info info2 = new DataLine.Info(Clip.class, format2);
		clip2 = (Clip) AudioSystem.getLine (info2);
		clip2.open (ais2);
		FloatControl gainControl2 = (FloatControl) clip2.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl2.setValue(volume);
		clip2.start();
		
		} catch (Exception e){
		System.out.println("whoops (error message) ");
		}
	}
	@Override
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.START){
			playing = true;
		} else {
			if (event.getType() == LineEvent.Type.STOP){
				clip.stop ();
				clip.flush ();
				clip.setFramePosition(0);
				playing = false;
		}
			
		}
		
	}

}