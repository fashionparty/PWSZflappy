package com.protonmail.maykie.pwszflappy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameView extends View {

    private Display display;
    private Runnable runnable;
    private Handler handler;
    private Bitmap background;
    private Bitmap topTube;
    private Bitmap botTube;
    private Bitmap bird1;
    private Bitmap bird2;
    private Bitmap birdFrame;
    private int displayWidth;
    private int displayHeight;
    private Rect rect;
    private Point birdLocation;
    private int frameCounter;
    private int velocity;
    private int gravity;
    private Tube[] tubes;
    private int tubeVelocity;
    private int score;
    private GameConfig gameConfig;
    private String character;
    private boolean gameRunning;
    private boolean gameLost;
    private RealtimeDatabase realtimeDatabase;
    private ArrayList<User> userList;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private Paint paint;
    private User user;

    private void startRestart() {
        realtimeDatabase = RealtimeDatabase.getInstanceOf();
        userList = new ArrayList<>();

        gameRunning = false;
        gameLost = false;
        score = 0;
        velocity = 0;
        gravity = 1;
        tubeVelocity = 5;

        realtimeDatabase.readData(new OnGetDataListener() {
            @Override
            public void onSuccess(ArrayList<User> list) {
                userList = list;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        handler = new Handler();
        gameConfig = GameConfig.getInstanceOf();
        character = gameConfig.getPostac();
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        topTube = BitmapFactory.decodeResource(getResources(), R.drawable.toptube);
        botTube = BitmapFactory.decodeResource(getResources(), R.drawable.bottube);
        paint = new Paint();

        if (gameConfig.getPostac() == null) character = "maykie";

        if (character.equals("maykie")) {
            bird1 = BitmapFactory.decodeResource(getResources(), R.drawable.maykie1);
            bird2 = BitmapFactory.decodeResource(getResources(), R.drawable.maykie2);
        } else if (character.equals("krystian")) {
            bird1 = BitmapFactory.decodeResource(getResources(), R.drawable.krystian1);
            bird2 = BitmapFactory.decodeResource(getResources(), R.drawable.krystian2);
        } else {
            bird1 = BitmapFactory.decodeResource(getResources(), R.drawable.klaudia1);
            bird2 = BitmapFactory.decodeResource(getResources(), R.drawable.klaudia2);
        }

        birdFrame = bird1;


        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        displayWidth = point.x;
        displayHeight = point.y;
        rect = new Rect(0, 0, displayWidth, displayHeight);
        birdLocation = new Point(displayWidth / 4 - birdFrame.getWidth() / 2, displayHeight / 2 - birdFrame.getHeight() / 2);

        tubes = new Tube[2];
        tubes[0] = new Tube(new Point(displayWidth, generateTubeHeight()));
        tubes[1] = new Tube(new Point(displayWidth * 2, generateTubeHeight()));

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("users");
    }

    public GameView(Context context) {
        super(context);
        startRestart();
    }

    private int generateTubeHeight() {

        Random random = new Random();
        return random.nextInt(displayHeight - 500) + 500;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rect, null);
        canvas.drawBitmap(birdFrame, birdLocation.x, birdLocation.y, null);

        paint.setColor(Color.BLACK);
        paint.setTextSize(200);

        //game running
        if (gameRunning) {

            canvas.drawText(Integer.toString(score), displayWidth / 2, 250, paint);

            //switching bird frames
            if (frameCounter < 5) birdFrame = bird1;
            else birdFrame = bird2;
            if (frameCounter == 10) frameCounter = 0;

            frameCounter++;

            //physics
            velocity += gravity;
            birdLocation.y += velocity;

            //tubes
            for (int i = 0; i < 2; i++) {
                canvas.drawBitmap(botTube, tubes[i].getBotTubePeakLocation().x, tubes[i].getBotTubePeakLocation().y, null);
                canvas.drawBitmap(topTube, tubes[i].getBotTubePeakLocation().x, tubes[i].getBotTubePeakLocation().y - topTube.getHeight() - 800, null);
                tubes[i].getBotTubePeakLocation().x -= tubeVelocity;

                if (tubes[i].getBotTubePeakLocation().x + botTube.getWidth() < 0) {
                    tubes[i] = new Tube(new Point(displayWidth * 2, generateTubeHeight()));
                    score++;
                }
            }

            if (score == 3) tubeVelocity = 6;
            if (score == 6) tubeVelocity = 7;
            if (score == 9) tubeVelocity = 8;
            if (score == 12) tubeVelocity = 9;
            if (score == 15) tubeVelocity = 10;
            if (score == 18) tubeVelocity = 11;
            if (score == 21) tubeVelocity = 12;
            if (score == 24) tubeVelocity = 13;
            if (score == 27) tubeVelocity = 14;
            if (score == 30) tubeVelocity = 15;
        }
        if(detectCollision()) {

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(80);
            paint.setColor(Color.RED);
            paint.setFakeBoldText(true);

            canvas.drawText("KONIEC GRY",displayWidth/2,displayHeight/3,paint);
            if(compareScore()) {
                String message = "Pobiłeś/aś swoj rekord: " + score + " pkt";
                canvas.drawText(message,displayWidth/2,(displayHeight/3) + 100,paint);
            }
            paint.setTextSize(50);
            canvas.drawText("Dotknij ekranu aby spróbować jeszcze raz",displayWidth/2,(displayHeight/3) + 200,paint);
            gameOver();
        }

        if (!gameLost) handler.postDelayed(runnable, 10);

            /*  //SHOW HITBOX
            Paint paint1 = new Paint();
            paint.setColor(Color.RED);
            Rect rect1 = new Rect(birdLocation.x-75+birdFrame.getWidth()/2 , birdLocation.y-175+birdFrame.getHeight()/2, birdLocation.x+75+birdFrame.getWidth()/2, birdLocation.y+175+birdFrame.getHeight()/2);
            canvas.drawRect(rect1, paint1);*/
    }

    private boolean detectCollision() {

        Point birdCenter = new Point(birdLocation.x + birdFrame.getWidth() / 2, birdLocation.y + birdFrame.getHeight() / 2);

        boolean collision = false;

        if (birdLocation.y > displayHeight - birdFrame.getHeight() / 2 || birdLocation.y + birdFrame.getHeight() / 2 < 0) {
            collision = true;
        }

        for (int i = 0; i < tubes.length; i++) {
            if ((birdCenter.x - 75 > tubes[i].getBotTubePeakLocation().x && birdCenter.x - 75 < tubes[i].getBotTubePeakLocation().x + topTube.getWidth())
                    || (birdCenter.x + 75 > tubes[i].getBotTubePeakLocation().x && birdCenter.x + 75 < tubes[i].getBotTubePeakLocation().x + topTube.getWidth())) {
                if (birdCenter.y - 175 < tubes[i].getBotTubePeakLocation().y - 800 || birdCenter.y - 175 > tubes[i].getBotTubePeakLocation().y) {
                    collision = true;
                } else if (birdCenter.y + 175 < tubes[i].getBotTubePeakLocation().y - 800 || birdCenter.y + 175 > tubes[i].getBotTubePeakLocation().y) {
                    collision = true;
                }
            }
        }
        return collision;
    }

    private void gameOver() {
        gameLost = true;
        gameRunning = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {

            if(gameLost==false) {
                velocity = -20;
                gameRunning = true;
            } else if(gameLost==true && gameRunning==false) {
                startRestart();
                this.invalidate();
            }
        }
        return true;
    }

    private boolean compareScore() {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                user = userList.get(i);
            }
        }
        if(score>user.getHighscore()) {
            updateScore();
            return true;
        } else return false;
    }

    public void updateScore() {
        dbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highscore").setValue(score);
    }
}