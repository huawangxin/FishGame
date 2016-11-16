package com.mypro.manager;

import java.util.ArrayList;
import java.util.HashMap;

import com.mypro.base.graphics.Bitmap;
import com.mypro.constant.Constant;
import com.mypro.model.FishGold;
import com.mypro.model.GamingInfo;
import com.mypro.model.Gold;
import com.mypro.model.GoldParticleEffect;
import com.mypro.model.HighPoint;
import com.mypro.model.HundredPoint;
import com.mypro.tools.LogTools;

/**
 * 得分管理器
 * @author Xiloerfan
 *
 */
public class ScoreManager {
	private static ScoreManager scoreManager;
	private int gold_speed = 1000 / Constant.ON_DRAW_SLEEP; 
	/**
	 * 金币的相关图片
	 */
	private Bitmap[] gold;
	/**
	 * 金币对应的金数字图片
	 * 这个属性比较特殊，在大炮管理器初始化的，这里当初欠考虑了
	 */
	private Bitmap[] goldNum;
	/**
	 * 高分的相关图片
	 * key:分数 value:对应的图片
	 */
	private HashMap<Integer,Bitmap[]> highPoint = new HashMap<Integer,Bitmap[]>();
	/**
	 * 百分的相关图片
	 * key:分数 value:对应的图片
	 */
	private HashMap<Integer,Bitmap[]> hundredPoint = new HashMap<Integer,Bitmap[]>();
	private ScoreManager(){}
	public static ScoreManager getScoreManager(){
		if(scoreManager==null){
			scoreManager = new ScoreManager();
		}
		return scoreManager;
	}
	/**
	 * 加分操作
	 * @param value
	 * @param showX		显示位置的x坐标
	 * @param showY		显示位置的Y坐标
	 */
	public void addScore(int value,final float showX,final float showY){
		GamingInfo.getGamingInfo().setScore(GamingInfo.getGamingInfo().getScore()+value);
		//不同的分数有不同的显示效果
		switch(value){
			case 40:
				showHighPoint(40,showX,showY);
				break;
			case 50:
				showHighPoint(50,showX,showY);
				break;
			case 60:
				showHighPoint(60,showX,showY);
				break;
			case 70:
				showHighPoint(70,showX,showY);
				break;
			case 80:
				showHighPoint(80,showX,showY);
				break;
			case 90:
				showHighPoint(90,showX,showY);
				break;
			case 100:
				showHundredPoint(100,showX,showY);
				break;
			case 120:
				showHundredPoint(120,showX,showY);
				break;
			case 150:
				showHundredPoint(150,showX,showY);
				break;
			default:
				showGoldNum(value,showX,showY);
				goldRun(showX,showY);
		}
	}
	/**
	 * 显示获得的金币数
	 * @param score
	 * @param goldFromX
	 * @param goldFromY
	 */
	private void showGoldNum(final int score,final float goldFromX,final float goldFromY){
		//显示得到的分数
		new Thread(new Runnable() {					
			@Override
			public void run() {
				try {
					FishGold fg = new FishGold(goldNum,score,goldFromX,goldFromY);
					GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GOLD_LAYER, fg);
					Thread.sleep(1000);
					GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.GOLD_LAYER, fg);
				} catch (Exception e) {
					e.printStackTrace();
					LogTools.doLogForException(e);
				}
				
			}
		}).start();
	}
	/**
	 * 产生金币，移动到屏幕底部正中间位置
	 * 这个以后可以变成配置文件，配置位置
	 * @param goldFromX
	 * @param goldFromY
	 */
	private void goldRun(final float goldFromX,final float goldFromY){
		//启动一个线程，来让金币开始移动
		new Thread(new Runnable() {		
			@Override
			public void run() {			
				final Gold gold = getGold();
				float x = Math.abs(GamingInfo.getGamingInfo().getScreenWidth()/2-gold.getPicWidth()/2-goldFromX); 				
				float y = Math.abs(GamingInfo.getGamingInfo().getScreenHeight()-gold.getPicHeight()/2-goldFromY); 	
				float len = (float) Math.sqrt(x * x + y * y); 							// 目标和始发点之间的距离
				float time = len / (Constant.Gold_SPEED / Constant.ON_DRAW_SLEEP); 		// 计算目标与始发之间子弹需要行走的帧数
				x = x / time; 															// 计算子弹沿X轴行进的增量
				y = y / time;
				if (GamingInfo.getGamingInfo().getScreenWidth()/2 < goldFromX) {
					x = -x;
				}			
				float goldX = goldFromX-gold.getPicWidth()/2,goldY = goldFromY-gold.getPicHeight()/2;				
				//创建金币的动作线程
				Runnable goldActThread = new Runnable() {				
					@Override
					public void run() {
						int picIndex = 0;
						gold.setPlayGoldPicAct(true);
						while(GamingInfo.getGamingInfo().isGaming()){
							while(!GamingInfo.getGamingInfo().isPause()&&gold.isPlayGoldPicAct()){
								gold.setCurrentPicId(picIndex);
								picIndex++;
								if(picIndex==gold.getPicLength()){
									picIndex = 0;
								}
								try {
									Thread.sleep(100);
								} catch (Exception e) {
									LogTools.doLogForException(e);
								}
							}
							break;
						}
					}
				};
				gold.setGoldActThread(goldActThread);
				gold.getPicMatrix().setTranslate(goldFromX, goldFromY);
				//将金币放入图层
				GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GOLD_LAYER, gold);
				GoldParticleEffect effect = ParticleEffectManager.getParticleEffectManager().getGoldEffect();
//				float angle = Tool.getAngle(GamingInfo.getGamingInfo().getScreenWidth()/2, GamingInfo.getGamingInfo().getScreenHeight(), goldFromX, goldFromY);
//				int ammoRedius = gold.getPicHeight()/2;//这个半径的作用是用于计算金币尾巴处出现粒子使用
//				effect.playEffect((float)(ammoRedius*Math.cos(Math.toRadians(angle+180))),-(float)(ammoRedius*Math.sin(Math.toRadians(angle+180))),goldFromX-gold.getPicWidth()/2, goldFromY-gold.getPicHeight()/2, x, y);
				float gw = gold.getPicWidth()/2,gh = gold.getPicHeight()/2;//金币中心偏移
				effect.playEffect(0,0,goldFromX+gw, goldFromY+gh, x, y);
				new Thread(gold.getGoldActThread()).start();
				//开始移动金币
				while(GamingInfo.getGamingInfo().isGaming()){
					goldX = goldX+x;
					goldY = goldY+y;
					gold.getPicMatrix().setTranslate(goldX, goldY);
					effect.setEffectMatrix(goldX+gw,goldY+gh);
					if(goldY>GamingInfo.getGamingInfo().getScreenHeight()){
						gold.setPlayGoldPicAct(false);//停止播放金币动画
						GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.GOLD_LAYER, gold);
						effect.stopEffect();
						break;
					}
					try {
						Thread.sleep(gold_speed);
					} catch (Exception e) {
						LogTools.doLogForException(e);
					}
				}
			}
		}).start();
		//播放金币音效
		SoundManager.playSound(SoundManager.SOUND_BGM_GOLD);
	}

	/**
	 * 显示高分区
	 * 40-90分得区间
	 * @param score
	 * @param goldFromX
	 * @param goldFromY
	 */
	private void showHighPoint(final int score,final float goldFromX,final float goldFromY){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HighPoint hp = new HighPoint(highPoint.get(score));
				hp.getPicMatrix().setTranslate(goldFromX, goldFromY);
				GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.HIGH_POINT_LAYER, hp);
				SoundManager.playSound(SoundManager.SOUND_BGM_HIGH_POINT);
				try {
					int showTime = 0;//显示5个循环
					int currentId = 0;
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()){
							hp.setCurrentPicId(currentId);
							currentId++;
							if(currentId==hp.getActPicLength()){
								currentId=0;
								showTime++;
							}
							if(showTime==5){		
								break;
							}
							Thread.sleep(100);
						}
						break;
					}
				} catch (Exception e) {
					LogTools.doLogForException(e);
				}
				GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.HIGH_POINT_LAYER, hp);
			}
		}).start();
	}
	/**
	 * 显示高分区
	 * 40-90分得区间
	 * @param score
	 * @param goldFromX
	 * @param goldFromY
	 */
	private void showHundredPoint(final int score,final float goldFromX,final float goldFromY){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HundredPoint hp = new HundredPoint(hundredPoint.get(score));
				hp.getPicMatrix().setTranslate(goldFromX, goldFromY);
				GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.HUNDRED_POINT_LAYER, hp);
				SoundManager.playSound(SoundManager.SOUND_BGM_HUNDRED_POINT);
				try {
					int showTime = 0;//显示5个循环
					int currentId = 0;
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()){
							hp.setCurrentPicId(currentId);
							currentId++;
							if(currentId==hp.getActPicLength()){
								currentId=0;
								showTime++;
							}
							if(showTime==5){		
								break;
							}
							Thread.sleep(100);
						}
						break;
					}
				} catch (Exception e) {
					LogTools.doLogForException(e);
				}
				GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.HUNDRED_POINT_LAYER, hp);
			}
		}).start();
	}
	/**
	 * 获取一个金币
	 * @return
	 */
	private Gold getGold(){
		return new Gold(this.gold);
	}
	
	/**
	 * 销毁操作
	 */
	public static void destroy(){
		scoreManager = null;
	}
	/**
	 * 初始化操作
	 */
	public void init(){
		try {
			
			//初始化金币
			initGold(ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("score/goldItem"),ImageManager.getImageMnagaer().scaleNum));
			//初始化高分
			initHighPoint(ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("score/highPoint"),ImageManager.getImageMnagaer().scaleNum));
			//初始化百分
			initHundredPoint(ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("score/hundred"),ImageManager.getImageMnagaer().scaleNum));
		} catch (Exception e) {
			LogTools.doLogForException(e);
		}
	}
	/**
	 * 初始化金币
	 * @param golds
	 */
	private void initGold(HashMap<String,Bitmap> golds){
		//效果图全名(gold01.png)
		StringBuffer goldFullName = new StringBuffer();
		//定义名字编号
		int goldtNum = 1;
		String goldName = "gold";
		ArrayList<Bitmap> allGoldList = new ArrayList<Bitmap>();
		while(GamingInfo.getGamingInfo().isGaming()){
			goldFullName.delete(0, goldFullName.length());
			goldFullName.append(goldName+"0"+goldtNum+".png");
			Bitmap gold = golds.get(goldFullName.toString());
			if(gold==null){
				break;
			}
			allGoldList.add(gold);
			goldtNum++;
		}
		//将集合转换为数组
		gold = new Bitmap[allGoldList.size()];
		for(int i =0;i<allGoldList.size();i++){
			gold[i] = ImageManager.getImageMnagaer().rotateImage(90, allGoldList.get(i));
		}
	}
	/**
	 * 初始化高分
	 * @param imgs
	 */
	private void initHighPoint(HashMap<String,Bitmap> highPoint){
		//效果图全名(40_1.png)
		StringBuffer highPointFullName = new StringBuffer();
		//定义名字编号
		int highPointBaseNum = 40;
		int highPointNum;
		ArrayList<Bitmap> allHighPointList = new ArrayList<Bitmap>();
		while(GamingInfo.getGamingInfo().isGaming()){
			highPointNum = 1;
			while(GamingInfo.getGamingInfo().isGaming()){
				highPointFullName.delete(0, highPointFullName.length());
				highPointFullName.append(highPointBaseNum+"_"+highPointNum+".png");
				Bitmap highPointImg = highPoint.get(highPointFullName.toString());
				if(highPointImg==null){
					break;
				}
				highPointNum++;
				allHighPointList.add(highPointImg);
			}
			if(allHighPointList.size()==0){
				break;
			}
			//将集合转换为数组
			Bitmap[] highPointArr = new Bitmap[allHighPointList.size()];
			for(int i =0;i<allHighPointList.size();i++){
				highPointArr[i] = allHighPointList.get(i);
			}	
			allHighPointList.clear();
			this.highPoint.put(highPointBaseNum, highPointArr);
			highPointBaseNum+=10;
		}
		
	}
	/**
	 * 初始化百分
	 * @param imgs
	 */
	private void initHundredPoint(HashMap<String,Bitmap> hundredPoint){
		//效果图全名(40_1.png)
		StringBuffer hundredPointFullName = new StringBuffer();
		//定义名字编号
		int hundredPointBaseNum = 100;
		int hundredPointNum;
		ArrayList<Bitmap> allHundredPointList = new ArrayList<Bitmap>();
		while(GamingInfo.getGamingInfo().isGaming()){
			hundredPointNum = 1;
			while(GamingInfo.getGamingInfo().isGaming()){
				hundredPointFullName.delete(0, hundredPointFullName.length());
				hundredPointFullName.append(hundredPointBaseNum+"_"+hundredPointNum+".png");
				Bitmap hundredPointImg = hundredPoint.get(hundredPointFullName.toString());
				if(hundredPointImg==null){
					break;
				}
				hundredPointNum++;
				allHundredPointList.add(hundredPointImg);
			}
			if(allHundredPointList.size()>0){
				//将集合转换为数组
				Bitmap[] hundredPointArr = new Bitmap[allHundredPointList.size()];
				for(int i =0;i<allHundredPointList.size();i++){
					hundredPointArr[i] = allHundredPointList.get(i);
				}
				allHundredPointList.clear();
				this.hundredPoint.put(hundredPointBaseNum, hundredPointArr);
			}			
			hundredPointBaseNum+=10;
			if(hundredPointBaseNum>=150){
				break;
			}
		}
		
	}
	/**
	 * 设置金币对应的数字图片
	 * @param goldNum
	 */
	public void setGoldNum(Bitmap[] goldNum) {
		this.goldNum = goldNum;
	}
	
}
