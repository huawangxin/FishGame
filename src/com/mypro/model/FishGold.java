package com.mypro.model;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.graphics.Canvas;
import com.mypro.base.graphics.Paint;
import com.mypro.tools.LogTools;

/**
 * 显示捕捉到鱼后获得的金币数量
 * @author Xiloerfan
 *
 */
public class FishGold extends DrawableAdapter{
	private int gold;//这个值记录当前组件应显示的金币数
	private int[] num_index = new int[1];//所有数字的索引，这里第一个元素代表得分的最大位数，往后类推
	private Bitmap[] num;
	private int layout_Y;				//Y轴向上移动的像素数
	private float numShowX,numShowY;//数字显示的X和Y坐标
	private int numPicWidth;	 //数字宽度，所有数字宽度是一样的
	public FishGold(Bitmap[] num,int gold,float numShowX,float numShowY){
		try {
			this.numShowX = numShowX;
			this.numShowY = numShowY;
			this.num = num;
			this.gold = gold;
			numPicWidth = num[0].getWidth();
			updateNumIndex();
		} catch (Exception e) {
			e.printStackTrace();
			LogTools.doLogForException(e);
		}
	
	}
	@Override
	public void onDraw(Canvas canvas, Paint paint) {
		try {
			canvas.drawBitmap(num[10], numShowX, numShowY-layout_Y, paint);
			for(int i=1;i<=num_index.length;i++){
				canvas.drawBitmap(num[num_index[i-1]], numShowX+(i*numPicWidth), numShowY-layout_Y, paint);
			}
			layout_Y++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 更新数字索引
	 */
	private void updateNumIndex(){
		String num = gold+"";
		num_index = new int[num.length()];
		int index = 0;
		for(char n:num.toCharArray()){
			num_index[index] = n-48;
			index++;
		}		
	}	
	
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPicWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPicHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
