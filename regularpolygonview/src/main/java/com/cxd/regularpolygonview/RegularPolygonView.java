package com.cxd.regularpolygonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

/**
 * create by chenxiaodong on 2020/4/23
 * 正多边形ImageView
 */
public class RegularPolygonView extends android.support.v7.widget.AppCompatImageView {

    private int W , H ;
    private int mCount ; //几边形
    private int mRound ; //圆角半径
    private int mBroderWidth ; //边框厚度
    private int mBroderColor ; //边框颜色
    private Vertex[] mVertexs ;
    private Vertex[] mStrokes ; //边框顶点集合

    private Bitmap mBitmap ;


    public RegularPolygonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setScaleType(ScaleType.CENTER_CROP);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.rpv);
        mRound = typedArray.getDimensionPixelSize(R.styleable.rpv_round_radius,0);
        mBroderWidth = typedArray.getDimensionPixelSize(R.styleable.rpv_border_width,0);
        mBroderColor = typedArray.getColor(R.styleable.rpv_border_color,Color.TRANSPARENT);
        mCount = typedArray.getInteger(R.styleable.rpv_sides,0);
        if(mCount < 3){
            mCount = 3 ;
        }
        mVertexs = new Vertex[mCount];
        mStrokes = new Vertex[mCount];
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        W = getMeasuredWidth();
        H = getMeasuredHeight();

        fillVertexs();
        fillStrokes();

        mBitmap = Bitmap.createBitmap(W,H, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(mBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPath(getRegularPolygonPath(),paint);

    }


    /**
     * 填充6个顶点以及附近点集合
     */
    private void fillVertexs(){
        float angle = -360 / mCount ;
        Point vertex = new Point(W/2 , 0) ;
//        Point center = new Point(W/2 , (int) (mRound * 2 / Math.sqrt(3)));
        int h = (int) ((mRound/Math.sqrt(3))/2);
        Point vertexL = new Point(W/2 - mRound/2 , h);
        Point vertexR = new Point(W/2 + mRound/2 , h);

        for (int i = 0; i < mCount; i++) {
            Vertex v = new Vertex(i,
                    calcNewPoint(vertex,angle*i),
                    calcNewPoint(vertexL,angle*i),
                    calcNewPoint(vertexR,angle*i));
            mVertexs[i] = v ;
        }
    }


    /**
     * 填充6个边框顶点
     */
    private void fillStrokes(){
        float angle = -360 / mCount ;
        Point vertex = new Point(W/2 , mBroderWidth / 2) ;
        int h = (int) ((mRound/Math.sqrt(3))/2);
        Point vertexL = new Point(W/2 - mRound/2 , h + mBroderWidth / 2);
        Point vertexR = new Point(W/2 + mRound/2 , h + mBroderWidth / 2);

        for (int i = 0; i < mCount; i++) {
            Vertex v = new Vertex(i,
                    calcNewPoint(vertex,angle*i),
                    calcNewPoint(vertexL,angle*i),
                    calcNewPoint(vertexR,angle*i));
            mStrokes[i] = v ;
        }
    }

    /**
     * 某个点旋转一定角度后，得到一个新的点
     * @param p 初始点
     * @param angle 旋转角度
     * @return
     */
    private Point calcNewPoint(Point p ,float angle) {
        Point pCenter = new Point(W/2,W/2) ;
        // calc arc
        float l = (float) ((angle * Math.PI) / 180);

        //sin/cos value
        float cosv = (float) Math.cos(l);
        float sinv = (float) Math.sin(l);

        // calc new point
        float newX = (p.x - pCenter.x) * cosv - (p.y - pCenter.y) * sinv + pCenter.x;
        float newY = (p.x - pCenter.x) * sinv + (p.y - pCenter.y) * cosv + pCenter.y;
        return new Point((int) newX, (int) newY);
    }

    Path mPath = null ;
    private Path getRegularPolygonPath(){
        if(mPath == null){
            mPath = new Path();
            mPath.moveTo(mVertexs[0].getVertexR().x,mVertexs[0].getVertexR().y);
            for(int i = 0 ; i < mVertexs.length ; i++){
                Vertex v = mVertexs[i];
                mPath.cubicTo(v.getVertexR().x,v.getVertexR().y,v.getVertex().x,v.getVertex().y,v.getVertexL().x,v.getVertexL().y);
                if(i < mVertexs.length -1){
                    mPath.lineTo(mVertexs[i+1].getVertexR().x,mVertexs[i+1].getVertexR().y);
                }else {
                    mPath.close();
                }
            }
        }
        return mPath;
    }

    Path mStrokePath = null ;
    private Path getStrokePath(){
        if(mStrokePath == null){
            mStrokePath = new Path();
            mStrokePath.moveTo(mStrokes[0].getVertexR().x,mStrokes[0].getVertexR().y);
            for(int i = 0 ; i < mStrokes.length ; i++){
                Vertex v = mStrokes[i];
                mStrokePath.cubicTo(v.getVertexR().x,v.getVertexR().y,v.getVertex().x,v.getVertex().y,v.getVertexL().x,v.getVertexL().y);
                if(i < mStrokes.length -1){
                    mStrokePath.lineTo(mStrokes[i+1].getVertexR().x,mStrokes[i+1].getVertexR().y);
                }else {
                    mStrokePath.close();
                }
            }
        }
        return mStrokePath;
    }

    Paint mStrokePaint = null ;
    private Paint getStrokePaint(){
        if(mStrokePaint == null){
            mStrokePaint = new Paint();
            mStrokePaint.setColor(mBroderColor);
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setAntiAlias(true);
            mStrokePaint.setStrokeWidth(mBroderWidth);
        }
        return mStrokePaint;
    }

    Paint mFermodePaint = null ;
    private Paint getFermodePaint(){
        if(mFermodePaint == null){
            mFermodePaint = new Paint();
            mFermodePaint.setAntiAlias(true);
            mFermodePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }
        return mFermodePaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 新建一个图层
        int saveLayer = canvas.saveLayer(0, 0, W,H,null, Canvas.ALL_SAVE_FLAG);

        super.onDraw(canvas);

        /*混合*/
        canvas.drawBitmap(mBitmap,0,0,getFermodePaint());

        /*绘制边框*/
        canvas.drawPath(getStrokePath(),getStrokePaint());

        // 将新建的图层绘制到默认图层上
        canvas.restoreToCount(saveLayer);
    }
}
