package com.mypro.model.componets;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.tools.Log;
import com.mypro.constant.Constant;
import com.mypro.model.DrawableAdapter;
import com.mypro.model.GamingInfo;

/**
 * 更换大炮时的变换效果
 * @author Xiloerfan
 *
 */
public class ChangeCannonEffect extends DrawableAdapter{
	private Bitmap[] effect;
	private int currentId;
	public ChangeCannonEffect(Bitmap[] effect){
		this.effect = effect;
	}
	/**
	 * 播放效果
	 */
	public void playEffect(){
		this.getPicMatrix().setTranslate(GamingInfo.getGamingInfo().getCannonLayoutX()-this.getPicWidth()/2, GamingInfo.getGamingInfo().getCannonLayoutY()-this.getPicHeight()/2);
		GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.CHANGE_CANNON_EFFECT_LAYER, this);
		for(int i =0;i<effect.length;i++){
			currentId = i;
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("ChangeCannonEffect", e.toString());
			}
		}
		GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.CHANGE_CANNON_EFFECT_LAYER, this);
	}
	
	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return effect[currentId];
	}
	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return effect[currentId].getWidth();
	}
	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return effect[currentId].getHeight();
	}
	
}
