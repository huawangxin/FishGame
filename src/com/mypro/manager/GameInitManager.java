package com.mypro.manager;

import com.mypro.constant.Constant;
import com.mypro.model.GamingInfo;
import com.mypro.model.LoadProgress;
import com.mypro.tools.LogTools;


/**
 * 游戏初始化管理器
 * @author Xiloerfan
 *
 */
public class GameInitManager {
	private static GameInitManager manager;
	private boolean initing = true;
	/**
	 * 是否正在初始化
	 * @return
	 */
	public boolean isIniting(){
		return initing;
	}
	private GameInitManager(){}
	public static GameInitManager getGameInitManager(){
		if(manager == null){
			manager = new GameInitManager();
		}
		return manager;
	}
	public void init(){
		ImageManager.getImageMnagaer().initManager();
		initProgress();
		initGame();//初始化游戏
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	closeProgress();
    	beginGame();
    	initing = false;
	}
	/**
	 * 初始化进度条画面
	 */
	private void initProgress(){
		while(LoadProgress.getLoadProgress()==null);
		GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.LOAD_PROGRESS_LAYER, LoadProgress.getLoadProgress());
	} 
	
	/**
	 * 关闭进度条画面
	 */
	private void closeProgress(){
		try{
			if(GamingInfo.getGamingInfo().getSurface()!=null){
				GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.LOAD_PROGRESS_LAYER, LoadProgress.getLoadProgress());
			}
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
	} 
	 /**
     * 初始化所有组件
     */
    private void initComponents(){
    	LayoutManager.getLayoutManager().init();
//    	
//    	// 绘制提高大炮质量按钮
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new GreateGunQualityButton());
//    	// 绘制降低大炮质量按钮
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new LessGunQualityButton());
//    	// 绘制左下角计分组件
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new BottomLeftComponent());
//    	// 绘制右下角激光蓄力槽组件
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new BottomRightComponent());
    }
    
	/**
	 * 初始化游戏
	 */
	private void initGame(){
			//初始化界面组件
			this.initComponents();
			//初始化得分管理器
			ScoreManager.getScoreManager().init();
			//初始化粒子管理器
			ParticleEffectManager.getParticleEffectManager();
			LoadProgress.getLoadProgress().setProgress(10);
			//初始化大炮管理器
			CannonManager.getCannonManager().init();
			LoadProgress.getLoadProgress().setProgress(20);
			//初始化鱼管理器
	        FishManager.getFishMananger().initFish();
	        LoadProgress.getLoadProgress().setProgress(40);
	        //初始化鱼群管理器
			GamingInfo.getGamingInfo().setShoalManager(new ShoalManager());
			LoadProgress.getLoadProgress().setProgress(60);     
			//初始化关卡管理器
			GamePartManager.getManager().prepare();
			LoadProgress.getLoadProgress().setProgress(80); 
			//初始化音效
			initSound();
	    	LoadProgress.getLoadProgress().setProgress(90);  	
			//初始化大炮
			CannonManager.getCannonManager().initCannon();
			LoadProgress.getLoadProgress().setProgress(100);
	}
	
	/**
	 * 停止游戏
	 */
	public void stop(){
		
		try {
			//设置游戏结束
			GamingInfo.getGamingInfo().setGaming(false);
			Thread.sleep(1000);
			//注销音乐管理器
			MusicManager.release();
			//注销鱼管理器
			FishManager.destroy();
			//注销游戏关卡管理器
			GamePartManager.getManager().destroy();
			//注销音效管理器
			SoundManager.release();
			//注销进度条
			LoadProgress.getLoadProgress().destroy();
			//注销自己
			manager = null;
		} catch (Exception e) {
			LogTools.doLogForException(e);
		}
	}
	
	/**
	 * 开始游戏
	 */
	private void beginGame(){
		//开始
		GamePartManager.getManager().start();
	}
	
	/**
     * 初始化所有音效
     */
	private void initSound(){
		GamingInfo.getGamingInfo().setSoundManager(SoundManager.getSoundManager());
		
	}
}
