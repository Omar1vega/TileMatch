package vegao1.onyxassignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.*;

/**
 * Created by vegao1 on 5/4/2017.
 */

public class BoardView extends SurfaceView implements SurfaceHolder.Callback {
    private int prevX;
    private int prevY;
    private int startRowNum;
    private int startColNum;
    Rect rect = new Rect();
    Paint p = new Paint(Color.BLUE);
    private String status = "NA";

    private final int gridSize = 9;
    private final int numOfTypes = 4;
    private final int bottomSpace = 300;

    private Bitmap[] bitmaps = new Bitmap[numOfTypes];
    private Grid g = new Grid(gridSize,gridSize);



    BoardView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        setWillNotDraw(false);
        System.out.println("Constructor");
    }
    @Override
    public void onDraw(Canvas canvas) {
        //g.updateAsk();
        if(g.score >99){
            canvas.drawColor(Color.WHITE);
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setTextSize(100);
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(("YOU WIN!!!"), getWidth()/2, (getHeight()/4), p);
            canvas.drawText(("YOUR SCORE: "+g.score), getWidth()/2, ((getHeight()/4)+100), p);

            return;


        }
        int width = getWidth();
        int height = getHeight()-bottomSpace;
        int rowSize = width/gridSize;
        int columnSize = height/gridSize;

        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(100);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(("SCORE: "+g.score+"/100"), width/2, (getHeight()-(bottomSpace/2)), p);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                /*
                System.out.println("x:"+i+" y:"+j+" blockx:"+g.getBlock(i,j).getX()+ " blockY:"+g.getBlock(i,j).getY());
                g.getBlock(i,j).setX_cord(i);
                g.getBlock(i,j).setY_cord(j);
                System.out.println("CHANGES");
                System.out.println("x:"+i+" y:"+j+" blockx:"+g.getBlock(i,j).getX()+ " blockY:"+g.getBlock(i,j).getY());
                System.out.println("\n\n");
                */
                switch (g.getBlockType(i, j)){
                    case 0:
                        rect.set(i * rowSize, j * columnSize, (i + 1) * rowSize, (j + 1) * columnSize);
                        canvas.drawBitmap(bitmaps[0], null, rect, null);
                        break;
                    case 1:
                        rect.set(i * rowSize, j * columnSize, (i + 1) * rowSize, (j + 1) * columnSize);
                        canvas.drawBitmap(bitmaps[1], null, rect, null);
                        break;
                    case 2:
                        rect.set(i * rowSize, j * columnSize, (i + 1) * rowSize, (j + 1) * columnSize);
                        canvas.drawBitmap(bitmaps[2], null, rect, null);
                        break;
                    case 3:
                        rect.set(i * rowSize, j * columnSize, (i + 1) * rowSize, (j + 1) * columnSize);
                        canvas.drawBitmap(bitmaps[3], null, rect, null);
                        break;
                }
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("SurfaceCreated()");
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_0);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_1);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_2);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_3);
        //g.initialize();
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                Block b = new Block(g);
                b.random();
                b.setX_cord(i);
                b.setY_cord(j);
                g.addBlock(b,i,j);
                System.out.println("Block Created at x:"+i+" y: "+j+ "Check: getx: "+ b.getX()+ " gety: "+b.getY());
            }
        }
        g.updateAsk();
        g.score = 0;
        invalidate();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(g.score>99)
            return false;
        System.out.println("Touch event occured");

        int currX;
        int currY;
        int endRowNum = 0;
        int endColNum = 0;
        int width = getWidth();
        int height = getHeight()-bottomSpace;

        int rowHeight = height / gridSize;
        int columnWidth = width / gridSize;
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            prevX = (int) event.getX();
            prevY = (int) event.getY();

            startRowNum = prevY / rowHeight;
            startColNum = prevX / columnWidth;


        }
        else if(event.getAction() == MotionEvent.ACTION_UP) {
            currX = (int) event.getX();
            currY = (int) event.getY();

            endRowNum = currY / rowHeight;
            endColNum = currX / columnWidth;

            System.out.println("StartRowNum : " + startRowNum + " StartColNum: " + startColNum);
            System.out.println("EndRowNum: " + endRowNum + " EndColNum: " + endColNum);

            //try {//try to getBlock()
            if(startRowNum == endRowNum) {
                if(startColNum > endColNum) {
                    System.out.println("R to L");
                    g.switchBlock(g.getBlock(startColNum,startRowNum),g.getBlock(startColNum-1,startRowNum));
                    g.getBlock(startColNum,startRowNum).ask();
                    g.getBlock(startColNum-1,startRowNum).ask();
                    if(!((g.getBlock(startColNum,startRowNum).eval())||(g.getBlock(startColNum-1,startRowNum).eval()))){
                        g.switchBlock(g.getBlock(startColNum,startRowNum),g.getBlock(startColNum-1,startRowNum));
                    }

                }
                else if(startColNum < endColNum) {
                    System.out.println("L to R");
                    g.switchBlock(g.getBlock(startColNum,startRowNum),g.getBlock(startColNum+1,startRowNum));
                    g.getBlock(startColNum,startRowNum).ask();
                    g.getBlock(startColNum+1,startRowNum).ask();
                    if(!((g.getBlock(startColNum,startRowNum).eval())||(g.getBlock(startColNum+1,startRowNum).eval()))){
                        g.switchBlock(g.getBlock(startColNum,startRowNum),g.getBlock(startColNum+1,startRowNum));
                    }
                }
                else {
                    System.out.println("Unrecognized action");
                }
            }
            else if(startColNum == endColNum) {
                if(startRowNum < endRowNum) {
                    System.out.println("Top to bottom");
                    g.switchBlock(g.getBlock(startColNum,startRowNum),g.getBlock(startColNum,startRowNum+1));
                    g.getBlock(startColNum,startRowNum).ask();
                    g.getBlock(startColNum,startRowNum+1).ask();
                    if(!((g.getBlock(startColNum,startRowNum).eval())||(g.getBlock(startColNum,startRowNum+1).eval()))){
                        g.switchBlock(g.getBlock(startColNum,startRowNum),g.getBlock(startColNum,startRowNum+1));
                    }
                }
                else if(startRowNum > endRowNum) {
                    System.out.println("Bottom to top");
                    g.switchBlock(g.getBlock(startColNum, startRowNum), g.getBlock(startColNum, startRowNum - 1));
                    g.getBlock(startColNum,startRowNum).ask();
                    g.getBlock(startColNum,startRowNum-1).ask();
                    if (!((g.getBlock(startColNum, startRowNum).eval()) || (g.getBlock(startColNum, startRowNum - 1).eval()))) {
                        g.switchBlock(g.getBlock(startColNum, startRowNum), g.getBlock(startColNum, startRowNum - 1));
                    }
                }

            }
            else {
                System.out.println("Invalid move");
            }//catch for getBlock
            //} catch (Exception e){
            //    System.out.println("posible out of bounds when switching:"+e);
            //}finally {
            //System.out.print("posible out of bounds when switching:\n");
            //}
            status = ("StartRow:"+startRowNum+" StartCol: "+startColNum+"\nEndRow: "+endRowNum+" EndCol:" + endColNum);
            g.updateAsk();
            invalidate();
        }
        return true;
    }
}