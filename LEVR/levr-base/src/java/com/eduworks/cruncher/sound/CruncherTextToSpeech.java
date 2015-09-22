package com.eduworks.cruncher.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

public class CruncherTextToSpeech extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		VoiceManager voiceManager = VoiceManager.getInstance();

		/*
		 * The VoiceManager manages all the voices for FreeTTS.
		 */
		Voice helloVoice = voiceManager.getVoices()[0];

		if (helloVoice == null)
		{
			System.err.println("Cannot find a voice.");
		}

		helloVoice.allocate();

		File tempFile = null;
		InMemoryFile file = null;
		try
		{
			File tempFile2 = File.createTempFile("foo", "");
			AudioPlayer player = new SingleFileAudioPlayer(tempFile2.getAbsolutePath(), Type.WAVE);
			helloVoice.setAudioPlayer(player);
			tempFile = new File(tempFile2.getAbsolutePath() + ".wav");
			helloVoice.speak(getObj(c, parameters, dataStreams).toString());
			player.close();
			helloVoice.deallocate();
			file = new InMemoryFile(tempFile);
			file.name = optAsString("name", tempFile2.getAbsolutePath(), c, parameters, dataStreams);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (tempFile != null)
				if (!tempFile.delete())
					tempFile.deleteOnExit();
		}
		return file;
	}

	@Override
	public String getDescription()
	{
		return "Generates a generic Text to Speech voice file out of the given text. Returns as WAV.";
	}

	@Override
	public String getReturn()
	{
		return "InMemoryFile";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "String");
	}

}
