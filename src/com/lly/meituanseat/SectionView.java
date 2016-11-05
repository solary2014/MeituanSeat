package com.lly.meituanseat;

import java.util.ArrayList;
import java.util.Collections;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Space;
import android.widget.Toast;

public class SectionView extends View {

	private float mSpaceX;
	private float mSpaceY;

	private float tranlateX;
	private float tranlateY;

	/**
	 * 座位已售
	 */
	private static final int SEAT_TYPE_SOLD = 1;

	/**
	 * 座位已经选中
	 */
	private static final int SEAT_TYPE_SELECTED = 2;

	/**
	 * 座位可选
	 */
	private static final int SEAT_TYPE_AVAILABLE = 3;

	/**
	 * 座位不可用
	 */
	private static final int SEAT_TYPE_NOT_AVAILABLE = 4;

	float scaleX, scaleY;

	private int mViewW;
	private int mViewH;

	private int row = 10;
	private int column = 10;

	private float defSeatWidth = 40.0f;
	private float defSeatHight = 40.0f;

	private int SeatWidth;
	private int SeatHight;

	private int leftmargin;
	private int rigthmargin;
	private int middenmargin;
	private int topmargin;
	private int spaceing = 5;

	private Bitmap SeatChecked;
	private Bitmap SeatLock;
	private Bitmap SeatNormal;
	
	private Bitmap mBitMapOverView;
	private float mOverViewWidth;
	private float mOverViewHight;

	private Matrix mMatrix = new Matrix();

	private float scale;
	protected boolean firstScale = true;
	private float scalef = 1.0f;
	
	private Paint mTextPaint;
	private int mTextHeight;
	private float mTextWidth;
	private float mNumberHeight;
	
	Paint.FontMetrics lineNumberPaintFontMetrics;
	
	private float scale1;
	ArrayList<String> lineNumbers = new ArrayList<String>();
	private Paint OverRectPaint;
	private float overRectLineWidth = 2;
	

	public SectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	private void init() {
		
		mTextPaint = new Paint();
		mNumberHeight = mTextPaint.measureText("4");
		mTextWidth = getResources().getDisplayMetrics().density * 20;
		
		lineNumberPaintFontMetrics = mTextPaint.getFontMetrics();
		
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(bacColor);
		mTextPaint.setTextSize(getResources().getDisplayMetrics().density * 16);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		if(getWidth()>0&&getHeight()>0){
			mOverViewWidth = (int) (getResources().getDisplayMetrics().density*getWidth() * 2/5);
			mOverViewHight = (int) (getResources().getDisplayMetrics().density*getHeight()*2/5);
		}else{
			mOverViewWidth = (int) (getResources().getDisplayMetrics().density*140);
			mOverViewHight = (int) (getResources().getDisplayMetrics().density*100);
		}
		
		
		SeatChecked = BitmapFactory.decodeResource(getResources(),
				R.drawable.seat_checked);
		SeatLock = BitmapFactory.decodeResource(getResources(),
				R.drawable.seat_lock);
		SeatNormal = BitmapFactory.decodeResource(getResources(),
				R.drawable.seat_normal);

		SeatWidth = SeatChecked.getWidth();
		SeatHight = SeatChecked.getHeight();

		scale = defSeatWidth / SeatChecked.getWidth();

		mSpaceX = getResources().getDisplayMetrics().density * 10;
		mSpaceY = getResources().getDisplayMetrics().density * 10;
		mViewW = (int) (column * SeatWidth * scale + mSpaceX * (column - 1)+mTextWidth);
		mViewH = (int) (row * SeatHight * scale + mSpaceY * (row - 1));
		
		 if(lineNumbers==null){
	            lineNumbers=new ArrayList<String>();
	        }else if(lineNumbers.size()<=0) {
	            for (int i = 0; i < row; i++) {
	                lineNumbers.add((i + 1) + "");
	            }
	        }
		 
		 matrix.postTranslate(mTextWidth+mSpaceX, 0);

	}

	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mViewH != 0 && mViewW != 0) {
		}

		drawSeat(canvas);
		drawText(canvas);
		drawOverView(canvas);
		drawOvewRect(canvas);

	}
	float scaleoverX;
	float scaleoverY ;
	private void drawOvewRect(Canvas canvas) {

		OverRectPaint = new Paint();
		OverRectPaint.setColor(Color.RED);
		OverRectPaint.setStyle(Paint.Style.STROKE);
		OverRectPaint.setStrokeWidth(overRectLineWidth);
		int tempViewW ;
		int tempViewH;
		if(getMeasuredWidth()<mViewW){
			tempViewW = getMeasuredWidth();
		}else{
			tempViewW = mViewW;
		}
		if(getMeasuredHeight()<mViewH){
			tempViewH = getMeasuredHeight();
		}else{
			tempViewH = mViewH;
		}
		
		try{
			Rect rect ;
			if(getMatrixScaleX()>= 1.0f){
				 rect = new Rect((int)(scaleoverX*Math.abs(getTranslateX())/getMatrixScaleX()), 
									 (int)(scaleoverY*Math.abs(getTranslateY())/getMatrixScaleX()),
									 (int)(scaleoverX*Math.abs(getTranslateX())/getMatrixScaleX()+tempViewW*scaleoverX/getMatrixScaleX()),
									 (int)(scaleoverY*Math.abs(getTranslateY())/getMatrixScaleX()+tempViewH*scaleoverY/getMatrixScaleX()));
			}else{
				 rect = new Rect((int)(scaleoverX*Math.abs(getTranslateX())), 
						 (int)(scaleoverY*Math.abs(getTranslateY())),
						 (int)(scaleoverX*Math.abs(getTranslateX())+tempViewW*scaleoverX),
						 (int)(scaleoverY*Math.abs(getTranslateY())+tempViewH*scaleoverY));
			}
		canvas.drawRect(rect, OverRectPaint);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	int bacColor = Color.parseColor("#7e000000");
	
	private void drawOverView(Canvas canvas) {
		mBitMapOverView = Bitmap.createBitmap((int)mOverViewWidth,(int)mOverViewHight,Bitmap.Config.ARGB_8888);
		Canvas OverViewCanvas = new Canvas(mBitMapOverView);
		Paint paint = new Paint();
		paint.setColor(bacColor);
		scaleoverX = mOverViewWidth / mViewW;
		scaleoverY = mOverViewHight / mViewH;
		float tempX = mViewW * scaleoverX;
		float tempY = mViewH * scaleoverY;
		OverViewCanvas.drawRect(0, 0, (float)tempX, (float)tempY, paint);
		
		Matrix tempoverMatrix = new Matrix();

		for (int i = 0; i < row; i++) {
			float top =  i * SeatHight * scale * scaleoverY+ i * mSpaceY * scaleoverY;
			for (int j = 0; j < column; j++) {
				float left = j * SeatWidth * scale * scaleoverX + j * mSpaceX * scaleoverX+mTextWidth*scaleoverX;
				tempoverMatrix.setTranslate(left, top);
				tempoverMatrix.postScale(scale*scaleoverX, scale*scaleoverY, left, top);
				
				int state = getSeatType(i, j);
				switch (state) {
				case SEAT_TYPE_SOLD:
					OverViewCanvas.drawBitmap(SeatLock, tempoverMatrix, null);
					break;
				case SEAT_TYPE_SELECTED:
					OverViewCanvas.drawBitmap(SeatChecked, tempoverMatrix, null);
					break;
				case SEAT_TYPE_AVAILABLE:
					OverViewCanvas.drawBitmap(SeatNormal, tempoverMatrix, null);
					break;
				case SEAT_TYPE_NOT_AVAILABLE:
					break;

				}
				
				
			}
		}
		
		canvas.drawBitmap(mBitMapOverView,0,0,null);
		
	}
	
	private void drawText(Canvas canvas) {
		mTextPaint.setColor(bacColor);
		RectF rectF = new RectF();
		rectF.top = getTranslateY() - mNumberHeight/2;
		rectF.bottom = getTranslateY()+ mViewH* getMatrixScaleX() + mNumberHeight/2;
		rectF.left = 0;
		rectF.right = mTextWidth;
		
		
		canvas.drawRoundRect(rectF, mTextWidth/2, mTextWidth/2, mTextPaint);
		mTextPaint.setColor(Color.WHITE);
		 for (int i = 0; i < row; i++) {
			 float top = (i *SeatHight*scale + i * mSpaceY) * getMatrixScaleX() + getTranslateY();
	         float bottom = (i * SeatHight*scale + i * mSpaceY + SeatHight) * getMatrixScaleX() + getTranslateY();
	         float baseline = (bottom + top  - lineNumberPaintFontMetrics.bottom - lineNumberPaintFontMetrics.top ) / 2-6;
	         canvas.drawText(lineNumbers.get(i), mTextWidth / 2, baseline, mTextPaint);
	      }

		
		
		
	}

	Matrix tempMatrix = new Matrix();
	Paint paint  = new Paint();
	

	private void drawSeat(Canvas canvas) {
		float zoom = getMatrixScaleX();
		Log.i("lly","getMatrixScaleX = "+getMatrixScaleX()+",getMatrixScaleY = "+getMatrixScaleY());
		scale1 = zoom;
		tranlateX = getTranslateX();
		tranlateY = getTranslateY();
		
		paint.setColor(Color.RED);
		paint.setTextSize(getResources().getDisplayMetrics().density * 16);
		
		
		Log.i("lly", "scale1 ===" + scale1);
		for (int i = 0; i < row; i++) {
			float top = i * SeatHight * scale * scale1 + i * mSpaceY * scale1
					+ tranlateY;
			for (int j = 0; j < column; j++) {
				
				float left = j * SeatWidth * scale * scale1 + j * mSpaceX
						* scale1 + tranlateX;

				tempMatrix.setTranslate(left, top);
				tempMatrix.postScale(scale, scale, left, top);
				tempMatrix.postScale(scale1, scale1, left, top);
				
				

				int state = getSeatType(i, j);
				switch (state) {
				case SEAT_TYPE_SOLD:
					canvas.drawBitmap(SeatLock, tempMatrix, null);
					break;
				case SEAT_TYPE_SELECTED:
					canvas.drawBitmap(SeatChecked, tempMatrix, null);
					break;
				case SEAT_TYPE_AVAILABLE:
					canvas.drawBitmap(SeatNormal, tempMatrix, null);
					break;
				case SEAT_TYPE_NOT_AVAILABLE:
					break;

				}
			}
		}

	}

	private int getSeatType(int row, int column) {

		if (isHave(getID(row, column)) >= 0) {
			return SEAT_TYPE_SELECTED;
		}

		return SEAT_TYPE_AVAILABLE;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		scaleGestureDetector.onTouchEvent(event);
		gestureDetector.onTouchEvent(event);

		return true;
	}

	float[] m = new float[9];

	Matrix matrix = new Matrix();

	private float getTranslateX() {
		matrix.getValues(m);
		return m[2];
	}

	private float getTranslateY() {
		matrix.getValues(m);
		return m[5];
	}

	private float getMatrixScaleY() {
		matrix.getValues(m);
		return m[4];
	}

	private float getMatrixScaleX() {
		matrix.getValues(m);

		Log.i("lly", "zoom ==g= " + m[Matrix.MSCALE_X]);
		return m[Matrix.MSCALE_X];
	}
	
	private void setMatrixScale(float scale){
		
		m[Matrix.MSCALE_X] = scale;
		m[Matrix.MSCALE_Y] = scale;
	}

	ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(
			getContext(), new ScaleGestureDetector.OnScaleGestureListener() {

				@Override
				public boolean onScale(ScaleGestureDetector detector) {
					float scaleFactor = detector.getScaleFactor();
					//scaleX = detector.getCurrentSpanX();
					//scaleY = detector.getCurrentSpanY();
					//直接判断大于2会导致获取的matrix缩放比例继续执行一次从而导致变成2.000001之类的数从而使
					//判断条件一直为真从而不会执行缩小动作
					//判断相乘大于2 可以是当前获得的缩放比例即使是1.9999之类的数如果继续放大即使乘以1.0001也会比2大从而
					//避免上述问题。
				
					 if (getMatrixScaleY() * scaleFactor > 2) {
			                scaleFactor = 2 / getMatrixScaleY();
			          }
					  if (getMatrixScaleY() * scaleFactor < 0.8) {
			                scaleFactor = 0.8f / getMatrixScaleY();
			          }
					matrix.postScale(scaleFactor, scaleFactor);
					

					invalidate();

					return true;
				}

				@Override
				public boolean onScaleBegin(ScaleGestureDetector detector) {

					return true;
				}

				@Override
				public void onScaleEnd(ScaleGestureDetector detector) {

				}

			});

	GestureDetector gestureDetector = new GestureDetector(getContext(),
			new GestureDetector.SimpleOnGestureListener() {

				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					float tempMViewW = column * SeatWidth*scale*scale1+(column -1)*mSpaceX*scale1+mTextWidth-getWidth();
					float tempmViewH = row * SeatHight * scale * scale1 + (row -1) * mSpaceY * scale1 - getHeight();
					if((getTranslateX()>mTextWidth+mSpaceX)&& distanceX<0){
						distanceX = 0;
					}
					if((Math.abs(getTranslateX())>tempMViewW)&&(distanceX>0)){
						distanceX = 0;
					}
					
					if((getTranslateY()>0)&&distanceY<0){
						distanceY=0;
					}
					if((Math.abs(getTranslateY())>tempmViewH)&&(distanceY>0)){
						distanceY = 0;
					}
					
					matrix.postTranslate(-distanceX, -distanceY);
					
					Log.i("lly","distanceX =="+distanceX+",distanceY ="+distanceY+",tempMViewW ="+tempMViewW
							+",getTranslateX()="+getTranslateX());
					
					invalidate();
					return false;

				}

				public boolean onSingleTapConfirmed(MotionEvent e) {
					int x = (int) e.getX();
					int y = (int) e.getY();

					for (int i = 0; i < row; i++) {
						for (int j = 0; j < column; j++) {
							int tempX = (int) ((j * SeatWidth * scale + j * mSpaceX) * getMatrixScaleX() + getTranslateX());
							int maxTemX = (int) (tempX + SeatWidth * scale * getMatrixScaleX());

							int tempY = (int) ((i * SeatHight * scale + i * mSpaceX) * getMatrixScaleY() + getTranslateY());
							int maxTempY = (int) (tempY + SeatHight * scale * getMatrixScaleY());
							
							
							if (x >= tempX && x <= maxTemX && y >= tempY
									&& y <= maxTempY) {
								int id = getID(i, j);
								int index = isHave(id);
								if (index >= 0) {
									remove(index);
								} else {
									addChooseSeat(i, j);
									
									Log.i("lly","i === "+i+",j === "+j);

								}
								float currentScaleY = getMatrixScaleY();
								if (currentScaleY < 1.7f) {
									scaleX = x;
									scaleY = y;
									zoomAnimate(currentScaleY, 1.9f);
								}
								invalidate();
								break;

							}
						}
					}

					return super.onSingleTapConfirmed(e);
				}
			});

	private float zoom;

	private void zoom(float zoom) {
		float z = zoom / getMatrixScaleX();
		matrix.postScale(z, z, scaleX, scaleY);
		invalidate();
	}

	private void zoomAnimate(float cur, float tar) {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(cur, tar);
		valueAnimator.setInterpolator(new DecelerateInterpolator());
		ZoomAnimation zoomAnim = new ZoomAnimation();
		valueAnimator.addUpdateListener(zoomAnim);
		valueAnimator.addListener(zoomAnim);
		valueAnimator.setDuration(400);
		valueAnimator.start();
	}

	class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener,
			Animator.AnimatorListener {

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			zoom = (Float) animation.getAnimatedValue();
			zoom(zoom);

		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationStart(Animator animation) {
		}

	}

	ArrayList<Integer> selects = new ArrayList<Integer>();
	

	private int getID(int row, int column) {
		return row * this.column + (column + 1);
	}

	private int isHave(Integer seat) {
		return Collections.binarySearch(selects, seat);
	}

	private void remove(int index) {
		selects.remove(index);
	}

	private void addChooseSeat(int row, int column) {
		int id = getID(row, column);
		for (int i = 0; i < selects.size(); i++) {
			int item = selects.get(i);
			if (id < item) {
				selects.add(i, id);
				return;
			}
		}

		selects.add(id);
	}

}
