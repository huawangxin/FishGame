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
 * 金币粒子效果
 * @author Xiloer
 *
 */
public class GoldParticleEffect extends DrawableAdapter{
	private static byte ADD = 1;
	private static byte REMOVE = 2;
	private static byte UPDATE = 3;
	//粒子图
	private Bitmap effectImg;
	private ArrayList<Particle> effects = new ArrayList<Particle>(); 
	private ArrayList<Particle> news =  new ArrayList<Particle>(); 
	private ArrayList<Particle> removes =  new ArrayList<Particle>(); 
	private int indexByDraw;//这个值用于绘制方法循环使用
	private Particle particle;//这个值用于绘制方法循环使用
	private boolean isPlay;//是否播放粒子效果
	private float targetOffsetX,targetOffsetY;//距离当前坐标的偏移量,这两个值加上currentX,currentY来得到粒子初始位置
	private float currentX,currentY;
	public GoldParticleEffect(Bitmap effectImg){
		this.effectImg = effectImg;
	}
	
	/**
	 * 播放一次粒子效果
	 * @param x				粒子的生成位置X
	 * @param y				粒子的生成位置Y
	 * @param offX			粒子偏移量X 这两个值是生成粒子时的行动路线，这个应该和给定的物体的偏移量相反
	 * @param offY			粒子偏移量Y
	 */
	public void playEffect(float targetOffsetX,float targetOffsetY,float x,float y,float offX,float offY){
		try {
			isPlay = true;
			this.targetOffsetX = targetOffsetX;
			this.targetOffsetY = targetOffsetY;
			startCreateEffectThread(x,y,offX,offY);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.PARTICLE_EFFECT_LAYER, this);
		}catch(Exception e){
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
	/**
	 * 启动产生粒子的线程
	 */
	private void startCreateEffectThread(final float x,final float y,final float offX,final float offY){
		this.currentX = x;
		this.currentY = y;
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try{
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()&&isPlay){
							updateEffect(ADD,new Particle(currentX,currentY,offX+(float)(Math.random()*5),offY+(float)(Math.random()*5),0.5f,effectImg));
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
	public void setEffectMatrix(float currentX,float currentY){
		this.currentX = currentX+targetOffsetX;
		this.currentY = currentY+targetOffsetY;
		Particle particle;
		for(int i =0;i<effects.size();i++){			
			particle = effects.get(i);
			particle.offX -=particle.offX*0.1f;
			particle.offY -=particle.offY*0.1f;
			particle.scale -=particle.scale*0.1f;
			particle.currentX = particle.currentX-particle.offX;
			particle.currentY = particle.currentY-particle.offY;
			particle.matrix.setTranslate(particle.currentX, particle.currentY);
			particle.matrix.preScale(particle.scale, particle.scale);
			if(particle.scale<0.1){
				updateEffect(REMOVE,particle);
			}
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
		 * 
		 * @param currentX
		 * @param currentY
		 * @param offX
		 * @param offY
		 */
		public Particle(float currentX,float currentY,float offX,float offY,float scale,Bitmap effect){
			this.offX = offX;
			this.offY = offY;
			this.scale = scale;
			this.currentX = currentX-effect.getWidth()/2*scale+targetOffsetX;
			this.currentY = currentY-effect.getHeight()/2*scale+targetOffsetY;	
			this.matrix.setTranslate(this.currentX, this.currentY);
			this.matrix.preScale(scale, scale);
			this.effect = effect;
		}
	}
}
