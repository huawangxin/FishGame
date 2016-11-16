package com.mypro.basecomponet;

/*
 *	DecodingAudioPlayer.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999, 2000 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine;

public class DecodingAudioPlayer
{
	private static final int	EXTERNAL_BUFFER_SIZE = 128000;



	public static void main(String[] args)
	{
//		if (args.length != 1)
//		{
//			printUsageAndExit();
//		}
		String	strFilename = "d:\\fishbg2.mp3";
		File file = new File (strFilename);
		byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
		AudioInputStream	audioInputStream = null;
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (audioInputStream == null)
		{
			out("###  cannot read input file: " + strFilename);
		}
		AudioFormat	sourceFormat = audioInputStream.getFormat();
		AudioFormat.Encoding	targetEncoding = AudioFormat.Encoding.PCM_SIGNED;
		audioInputStream = AudioSystem.getAudioInputStream(targetEncoding, audioInputStream);
		AudioFormat	audioFormat = audioInputStream.getFormat();

		SourceDataLine	line = null;
		DataLine.Info	info = new DataLine.Info(
			SourceDataLine.class,
			audioFormat);
		try
		{
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		line.start();
		int	nBytesRead = 0;
		while (nBytesRead != -1)
		{
			try
			{
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (nBytesRead >= 0)
			{
				int	nBytesWritten = line.write(abData, 0, nBytesRead);
			}
		}
		// line.close();
	}



	public static void printUsageAndExit()
	{
		out("DecodingAudioPlayer: usage:");
		out("\tjava DecodingAudioPlayer <soundfile>");
		System.exit(1);
	}



	private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}
}



/*** DecodingAudioPlayer.java ***/

