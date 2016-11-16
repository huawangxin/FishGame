package com.mypro.manager;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import com.mypro.model.GamingInfo;




/**
 * 音乐管理器
 * @author Xiloerfan
 *
 */
public class MusicManager {
	private static MusicManager manager;
//	private MediaPlayer mediaPlayer;
	private AudioInputStream audioInputStream;// 文件流

	private AudioFormat audioFormat;// 文件格式

	private SourceDataLine sourceDataLine;// 输出设备
	
	public static MusicManager getMusicManager(){
		if(manager==null){
			manager = new MusicManager();
		}
		return manager;
	}
	/**
	 * 私有的构造器
	 */
	private MusicManager(){
//		this.mediaPlayer = new MediaPlayer();
	}
	/**
	 * 根据R文件中对应的ID属性名来播放音效
	 * @param resId
	 */
	public void playMusicByR(String resId,boolean isLoop){
		try {
			File file = new File("bgm"+File.separator+resId);


			

			Thread playThread = new Thread(new PlayThread(file,true));
			playThread.start();
			
		} catch (Exception e) {

			e.printStackTrace();

		}
	}
	/**
	 * 获取R类中raw中的静态属性的值
	 * @param fieldName		属性名
	 * @return				-1:没有找到对应的属性
	 */
	private int getStaticFieldValueByR(String fieldName){
//		try{
//			for(Field f:R.raw.class.getFields()){
//				if(f.getName().equals(fieldName)){
//					return f.getInt(null);
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return -1;
	}
	
	/**
	 * 释放资源
	 */
	public static void release(){
//		if(manager!=null&&manager.mediaPlayer!=null){
//			if(manager.mediaPlayer.isPlaying()){
//				manager.mediaPlayer.stop();			
//			}
//			manager.mediaPlayer.release();
//		}
//		manager=null;
	}
	
	class PlayThread extends Thread {
		public boolean isLoop;
		File musicFile;
		byte tempBuffer[] = new byte[320];
		public PlayThread(File musicFile,boolean isLoop){
			this.musicFile = musicFile;
			this.isLoop = isLoop;
		}
		public void run() {

			try {

				int cnt;
				do{	
					// 取得文件输入流

					audioInputStream = AudioSystem.getAudioInputStream(musicFile);

					audioFormat = audioInputStream.getFormat();

					// 转换mp3文件编码

					if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {

						audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,

						audioFormat.getSampleRate(), 16, audioFormat

						.getChannels(), audioFormat.getChannels() * 2,

						audioFormat.getSampleRate(), false);

						audioInputStream = AudioSystem.getAudioInputStream(audioFormat,

						audioInputStream);

					}
					// 打开输出设备

					DataLine.Info dataLineInfo = new DataLine.Info(

					SourceDataLine.class, audioFormat,

					AudioSystem.NOT_SPECIFIED);

					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

					sourceDataLine.open(audioFormat);

					sourceDataLine.start();
					// 读取数据到缓存数据
	
					while ((cnt = audioInputStream.read(tempBuffer, 0,
	
					tempBuffer.length)) != -1) {
	
						if (!GamingInfo.getGamingInfo().isGaming())
	
							break;
	
						if (cnt > 0) {
	
							// 写入缓存数据
	
							sourceDataLine.write(tempBuffer, 0, cnt);
	
						}
	
					}
	
					// Block等待临时数据被输出为空
	
					sourceDataLine.drain();	
					sourceDataLine.close();
				}while(isLoop&&GamingInfo.getGamingInfo().isGaming());
			} catch (Exception e) {

				e.printStackTrace();

				System.exit(0);

			}

		}

	}
}
