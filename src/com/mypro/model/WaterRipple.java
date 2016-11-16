package com.mypro.model;

import com.mypro.base.graphics.Bitmap;

/**
 * Ë®²¨ÎÆ
 * @author Xiloer
 *
 */
public class WaterRipple extends DrawableAdapter{
	private Bitmap[] ripple;
	private int currentId;
	public WaterRipple(Bitmap[] ripple){
		this.ripple = ripple;
	}
	
	public void setCurrentId(int currentId) {
		this.currentId = currentId;
	}

	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return ripple[currentId];
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return getCurrentPic().getWidth();
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return getCurrentPic().getHeight();
	}

}
