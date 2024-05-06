package com.example.se2_projekt_app.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.se2_projekt_app.R;
import com.example.se2_projekt_app.enums.Element;

public class CardDrawView extends SurfaceView implements SurfaceHolder.Callback {

    private final Bitmap plantBitmap;
    private final Bitmap robotBitmap;
    private final Bitmap energyBitmap;
    private final Bitmap planningBitmap;
    private final Bitmap spacesuitBitmap;
    private final Bitmap waterBitmap;
    private int[] xPositions;
    private CardCombination[] currentCombination;

    public CardDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        plantBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plant);
        robotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.robot);
        energyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.energy);
        planningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.planning);
        spacesuitBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spacesuit);
        waterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        this.xPositions=new int[3];
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        drawTest(0);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        drawTest(0);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
           for (int i=0; i<3;i++){
               if(event.getX()>xPositions[0]&&event.getX()<xPositions[1]){
                   CardCombination combination1=currentCombination[0];
                   drawTest(5);
               } else if(event.getX()>xPositions[1]&&event.getX()<xPositions[2]){
                   CardCombination combination2=currentCombination[1];
                   drawTest(10);
               } else {
                   CardCombination combination3=currentCombination[2];
                   drawTest(100);
               }

           }
        }
        return true;
    }




    private void drawTest(int a) {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                // Clear canvas
                canvas.drawColor(Color.WHITE);

                // Calculate image dimensions
                float screenHeight = (float) 1 / 5;
                int imageHeight = (int) (canvas.getHeight() * screenHeight);
                int imageWidth = canvas.getWidth() / 3; // Limit width to one third of the screen width

                // Draw the image and number three times with appropriate spacing
                for (int i = 0; i < 3; i++) {
                    int offsetPerSymbol = 20;
                    xPositions[i] = i * (imageWidth + offsetPerSymbol); // Adjust spacing as needed
                    drawCombinationTest( canvas, xPositions[i], plantBitmap, robotBitmap,i+a, imageWidth, imageHeight);
                }
            }
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
    private void drawCombinationTest(Canvas canvas, int offsetX, Bitmap currentSymbol, Bitmap nextSymbol, int currentNumber, int symbolWidth, int symbolHeight) {
        // Draw current Symbol
        canvas.drawBitmap(currentSymbol, null, new android.graphics.Rect(offsetX, 0, offsetX + symbolWidth, symbolHeight), null);

        // Draw nextSymbol on top of Current symbol at 1/4 the size
        int robotWidth = symbolWidth / 4;
        int robotHeight = symbolHeight / 4;
        canvas.drawBitmap(nextSymbol, null, new android.graphics.Rect(offsetX, 0, offsetX + robotWidth, robotHeight), null);

        // Draw number on top of current Symbol
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        // Calculate position to center number on Symbol
        int numberX = offsetX + (symbolWidth / 2);
        int numberY = symbolHeight / 2 + 25; // Adjust vertical position as needed
        canvas.drawText(String.valueOf(currentNumber), numberX, numberY, paint);
    }

    public void updateCanvas(CardCombination[] combination){
        this.currentCombination=combination;
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                // Clear canvas
                canvas.drawColor(Color.WHITE);

                // Calculate image dimensions
                int imageHeight = canvas.getHeight() / 5;
                int imageWidth = canvas.getWidth() / 3; // Limit width to one third of the screen width

                // Draw the image and number three times with appropriate spacing
                for (int i = 0; i < 3; i++) {
                    int left = i * (imageWidth + 20); // Adjust spacing as needed
                    drawCombination(canvas, left, combination[i], imageWidth, imageHeight);
                }
            }
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
    private void drawCombination(Canvas canvas, int offsetX, CardCombination combination, int symbolWidth, int symbolHeight) {
        if(combination==null||combination.getCurrentNumber()==null||combination.getCurrentSymbol()==null||combination.getNextSymbol()==null)throw new IllegalArgumentException("Cannot draw from empty combination");
        Bitmap currentSymbol=getBitMapFromElement(combination.getCurrentSymbol());
        Bitmap nextSymbol=getBitMapFromElement(combination.getNextSymbol());
        int currentNumber=combination.getCurrentNumber().getValue();

        // Draw current Symbol
        canvas.drawBitmap(currentSymbol, null, new android.graphics.Rect(offsetX, 0, offsetX + symbolWidth, symbolHeight), null);

        // Draw nextSymbol on top of Current symbol at 1/4 the size
        int robotWidth = symbolWidth / 3;
        int robotHeight = symbolHeight / 3;
        canvas.drawBitmap(nextSymbol, null, new android.graphics.Rect(offsetX, 0, offsetX + robotWidth, robotHeight), null);

        // Draw number on top of current Symbol
        Paint paint = new Paint();
        paint.setTextSize(75);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        // Calculate position to center number on Symbol
        int numberX = offsetX + (symbolWidth / 2);
        int numberY = symbolHeight / 2 + 25;
        canvas.drawText(String.valueOf(currentNumber), numberX, numberY, paint);
    }
    private Bitmap getBitMapFromElement(Element element){
        switch(element){
            case ENERGY:return energyBitmap;
            case PLANNING:return planningBitmap;
            case SPACESUIT: return spacesuitBitmap;
            case WATER: return waterBitmap;
            case PLANT: return plantBitmap;
            case ROBOT: return robotBitmap;
            default: return null;
        }
    }
}
