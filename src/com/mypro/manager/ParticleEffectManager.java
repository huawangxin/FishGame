package com.mypro.manager;

import java.awt.Color;

import com.mypro.base.graphics.Bitmap;
import com.mypro.constant.Constant;
import com.mypro.model.AmmoParticleEffect;
import com.mypro.model.GoldParticleEffect;
import com.mypro.model.NetParticleEffect;
import com.mypro.tools.LogTools;

/**
 * 粒子管理器
 * @author Xiloer
 *
 */
public class ParticleEffectManager {
	private static ParticleEffectManager manager;
	/**
	 * 金色星星粒子图片
	 */
	private Bitmap goldStartParticleImg;
	/**
	 * 星星粒子图片
	 */
	private Bitmap startParticleImg;
	/**
	 * 星星粒子彩色图
	 */
	private Bitmap[] startParticleImgs;
	
	private ParticleEffectManager(){
		try {
			startParticleImg = ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("cannon/stars.png");
			goldStartParticleImg = ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("cannon/tenstar.png");
			createColorfulParticleImgs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogTools.doLogForException(e);
		}
	}
	/**
	 * 随机产生几个颜色的粒子
	 */
	private void createColorfulParticleImgs(){
		startParticleImgs = new Bitmap[Constant.PARTICLE_COLOR_SUM];
		for(int i =0;i<Constant.PARTICLE_COLOR_SUM;i++){
			startParticleImgs[i] = createColorfulParticleImg(getColor(i));
		}
	}
	/**
	 * 随机产生1个颜色的粒子
	 */
	private Bitmap createColorfulParticleImg(int color){
		Bitmap img = Bitmap.createBitmap(startParticleImg);
//		int color =  Color.rgb((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
		for(int r =0;r<img.getWidth();r++){
			for(int l =0;l<img.getHeight();l++){
				if(img.getPixel(r, l)==0x00000000){
					continue;
				}
				img.setPixel(r, l,color);
			}
		}
		return img;
	}
	private int getColor(int i){   
			switch(i){  
	            case 0:  
	                return 0xffffd700;  
	            case 1:  
	            	return 0xffff7f00;   
	            case 2:  
	            	return 0xffff3e96;  
	            case 3:  
	            	return 0xffc7c7c7;  
	            case 4:  
	            	return 0xffbfefff;
	            case 5:  
	            	return 0xff76eec6;
	            case 6:  
	            	return 0xff4b0082;
	            case 7:  
	            	return 0xff00b2ee;
	            case 8:  
	            	return 0xff9f79ee;
	            case 9:  
	            	return 0xffd1d1d1;
			}  
			return 0xffffffff;
	}  

	public static ParticleEffectManager getParticleEffectManager(){
		if(manager==null){
			manager = new ParticleEffectManager();
		}
		return manager;
	}

	/**
	 * 获取一个渔网粒子效果实例
	 * @return
	 */
	public NetParticleEffect getNetEffect(){
		return new NetParticleEffect(startParticleImgs);
	}
	/**
	 * 获取一个子弹粒子效果实例
	 * @return
	 */
	public AmmoParticleEffect getAmmoEffect(){
		return new AmmoParticleEffect(startParticleImgs);
	}
	/**
	 * 获取一个金币粒子效果实例
	 * @return
	 */
	public GoldParticleEffect getGoldEffect(){
		return new GoldParticleEffect(goldStartParticleImg);
	}
}
