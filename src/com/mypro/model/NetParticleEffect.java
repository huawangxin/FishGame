package com.mypro.model;

import java.util.ArrayList;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.graphics.Canvas;
import com.mypro.base.graphics.Matrix;
import com.mypro.base.graphics.Paint;
import com.mypro.basecomponet.JMatrix;
import com.mypro.constant.Constant;
import com.mypro.tools.LogTools;
/**
 * 渔网粒子效果
 * @author Xiloer
 *
 */
public class NetParticleEffect extends DrawableAdapter{
	private static byte ADD = 1;
	private static byte REMOVE = 2;
	private static byte UPDATE = 3;
	//粒子彩色图
	private Bitmap effectImgs[];
	private ArrayList<Particle> effects = new ArrayList<Particle>(); 
	private ArrayList<Particle> news =  new ArrayList<Particle>(); 
	private ArrayList<Particle> removes =  new ArrayList<Particle>(); 
	private int indexByDraw;//这个值用于绘制方法循环使用
	private Particle particle;//这个值用于绘制方法循环使用
	private boolean isPlay;//是否播放粒子效果
	public NetParticleEffect(Bitmap effectImgs[]){
		this.effectImgs = effectImgs;
	}
	
	/**
	 * 播放一次粒子效果
	 * @param x				粒子的生成位置X
	 * @param y				粒子的生成位置Y
	 * @param level			粒子等级
	 */
	public void playEffect(float x,float y,int level){
		try {
			isPlay = true;
			startCreateEffectThread(x,y,level);
			startSetEffectThread();			
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.PARTICLE_EFFECT_LAYER, this);
		} catch (Exception e) {
			LogTools.doLogForException(e);
		}	
	}
	private void updateEffect(byte mode,Particle p){
		if(mode==ADD){
			news.add(p);
		}else if(mode==REMOVE){
			removes.add(p);
		}else if(mode == UPDATE){
			if(news.size()>0){
				effects.addAll(news);
				news.clear();
			}
			if(removes.size()>0){
				effects.removeAll(removes);
				removes.clear();
			}
		}
	}
	private void startSetEffectThread(){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try{
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()&&isPlay){
							setEffectMatrix();
							Thread.sleep(50);
						}
						break;
					}
				}catch(Exception e){
					LogTools.doLogForException(e);
				}
			}
		}).start();
	}
	/**
	 * 启动产生粒子的线程
	 */
	private void startCreateEffectThread(final float x,final float y,final int level){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try{
					byte sum = 0;
					float scale;
					while(GamingInfo.getGamingInfo().isGaming()&&isPlay){
						while(!GamingInfo.getGamingInfo().isPause()&&isPlay){
							scale = (float)((Math.random()*level+1)/10);
							switch(sum){
								case 0:
									updateEffect(ADD,new Particle(x,y,-(float)(Math.random()*6+1),(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
								case 1:
									updateEffect(ADD,new Particle(x,y,(float)(Math.random()*6+1),-(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
								case 2:
									updateEffect(ADD,new Particle(x,y,(float)(Math.random()*6+1),(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
								case 3:
									updateEffect(ADD,new Particle(x,y,-(float)(Math.random()*6+1),-(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
							}
							sum++;
							if(sum>3){
								sum = 0;
							}
							Thread.sleep((long)(Math.random()*51));
						}
						break;
					}
				}catch(Exception e){
					LogTools.doLogForException(e);
				}
			}
		}).start();
	}
	@Override
	public void onDraw(Canvas canvas, Paint paint) {
		updateEffect(UPDATE,null);
		indexByDraw = 0;
		while(GamingInfo.getGamingInfo().isGaming()){
			while(!GamingInfo.getGamingInfo().isPause()&&isPlay&&indexByDraw<effects.size()){
				particle = effects.get(indexByDraw);
				canvas.drawBitmap(particle.effect, particle.matrix, paint);
				indexByDraw++;
			}
			break;
		}
	}
	/**
	 * 停止播放粒子
	 */
	public void stopEffect(){
		this.isPlay = false;
		GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.PARTICLE_EFFECT_LAYER, this);
	}
	/**
	 * 设置粒子位置
	 */
	public void setEffectMatrix(){
		Particle particle;
		for(int i =0;i<effects.size();i++){			
			particle = effects.get(i);
			if(particle.currentLen>=particle.maxLen){
				updateEffect(REMOVE,particle);
			}
//			particle.offX -=particle.offX*0.05f;
//			particle.offY -=particle.offY*0.05f;
//			particle.scale -=particle.scale*0.05f;
			particle.currentX = particle.currentX+particle.offX;
			particle.currentY = particle.currentY+particle.offY;
			particle.matrix.setTranslate(particle.currentX, particle.currentY);
			particle.matrix.preScale(particle.scale, particle.scale);
			particle.currentLen++;
		}
	}
	
	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * 粒子对象
	 * @author Xiloer
	 *
	 */
	private class Particle{
		private Bitmap effect;
		/**
		 * 当前粒子坐在坐标X
		 */
		public float currentX;
		/**
		 * 当前粒子坐在坐标Y
		 */
		public float currentY;
		/**
		 * 偏移量X
		 */
		public float offX;
		/**
		 * 偏移量Y
		 */
		public float offY;
		/**
		 * 缩放
		 */
		public float scale;//缩放基数
		/**
		 * 粒子矩阵
		 */
		public Matrix matrix = new JMatrix();
		/**
		 * 最大行走次数
		 */
		public int maxLen = (int)(Math.random()*20);
		/**
		 * 当前行走次数
		 */
		public int currentLen;
		/**
		 * 
		 * @param currentX
		 * @param currentY
		 * @param offX
		 * @param offY
		 */
		public Particle(float currentX,float currentY,float offX,float offY,float scale,Bitmap effect){
			this.offX = offX*scale;
			this.offY = offY*scale;
			this.scale = scale;
			this.currentX = currentX-effect.getWidth()/2*scale;
			this.currentY = currentY-effect.getHeight()/2*scale;	
			this.matrix.setTranslate(this.currentX, this.currentY);
			this.matrix.preScale(scale, scale);
			this.effect = effect;
		}
	}
}
