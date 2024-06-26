package com.example.se2_projekt_app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.example.se2_projekt_app.enums.FieldCategory;
import com.example.se2_projekt_app.enums.FieldValue;
import com.example.se2_projekt_app.game.Floor;
import com.example.se2_projekt_app.game.GameBoard;

/**
 * Provides a view that supports scaling and touch interactions to manage and display game elements.
 */
public class GameBoardView extends SurfaceView implements SurfaceHolder.Callback, ScaleGestureDetector.OnScaleGestureListener {
    private float scaleFactor = 1.0f;
    private float translateX = 0f;
    private float translateY = 0f;
    private float lastTouchX;
    private float lastTouchY;
    private GameBoard gameboard = new GameBoard();
    private ScaleGestureDetector scaleGestureDetector;
    // will be set by cardview
    private FieldValue currentSelection = FieldValue.FIVE;
    private int lastAccessedFloor = 0;

    /**
     * Constructs the game board view with necessary context and attributes.
     *
     * @param context The context of the application.
     * @param attrs   The set of attributes from XML.
     */
    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        init(context);

        Floor floor = new Floor(0, 0, FieldCategory.PLANNING);
        floor.addChamber(5);
        gameboard.addFloor(floor);
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameboard = gameBoard;
        this.drawGameBoard();
    }

    /**
     * Initializes the scale gesture detector and sections of the game.
     *
     * @param context The application context.
     */
    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // empty because I had to implement it and don't need it yet
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        drawGameBoard();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // empty because I had to implement it and don't need it yet
    }

    /**
     * Redraws the game board on the canvas, applying translation and scaling transformations.
     */
    public void drawGameBoard() {
        final Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            try {
                synchronized (getHolder()) {
                    canvas.save();
                    canvas.drawColor(Color.WHITE); // Clear with white background
                    canvas.translate(translateX, translateY);
                    canvas.scale(scaleFactor, scaleFactor);

                    for (Floor floor : gameboard.getFloors()) {
                        floor.draw(canvas);
                    }

                    canvas.restore();
                }
            } finally {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            float adjustedX = (event.getX() - translateX) / scaleFactor;
            float adjustedY = (event.getY() - translateY) / scaleFactor;

            for (int i = 0; i < gameboard.getFloors().size(); i++) {
                Floor floor = gameboard.getFloors().get(i);
                if (floor.handleClick(adjustedX, adjustedY, this, currentSelection)) {
                    lastAccessedFloor = i;
                    break;
                }
            }
            lastTouchX = event.getX();
            lastTouchY = event.getY();
        } else if (action == MotionEvent.ACTION_MOVE && !scaleGestureDetector.isInProgress()) {
            final float dx = (event.getX() - lastTouchX) * (1 / scaleFactor);
            final float dy = (event.getY() - lastTouchY) * (1 / scaleFactor);

            translateX += dx;
            translateY += dy;
            lastTouchX = event.getX();
            lastTouchY = event.getY();
            drawGameBoard();
        }
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f)); // Limit zooming
        drawGameBoard();
        return true;
    }

    @Override
    public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
        // empty because I had to implement it and don't need it yet
    }

    public void setFieldValue(FieldValue fieldValue) {
        this.currentSelection = fieldValue;
    }

    public int getLastAccessedFloor() {
        return lastAccessedFloor;
    }
}
