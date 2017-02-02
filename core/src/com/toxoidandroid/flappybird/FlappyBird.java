package com.toxoidandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture topTube;
    Texture bottomTube;

    Texture[] birds;
    int flapState = 0;
    int gameState = 0;
    float velocity = 0;
    float birdY = 0;
    float gravity = 2;
    float gap = 400;
    float maxTubeOffset;
    float tubeVelocity = 5;

    Random mRandom;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;
    Circle birdCircle;
    Rectangle topPipe, bottomPipe;
    int score = 0;
    int scoringTube = 0;
    BitmapFont mFont;
    Texture gameOver;
//    ShapeRenderer mShapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird_1.png");
        birds[1] = new Texture("bird_2.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
        maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 250;
        mRandom = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
        birdCircle = new Circle();
        topPipe = new Rectangle();
        bottomPipe = new Rectangle();
        mFont = new BitmapFont();
        mFont.setColor(Color.WHITE);
        mFont.getData().scale(10);
        gameOver = new Texture("gameover.png");
//        mShapeRenderer = new ShapeRenderer();

        startGame();

//        tubeX = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2;
    }

    public void startGame() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;

        for (int i = 0; i< numberOfTubes; i++) {
            tubeOffset[i] = (mRandom.nextFloat() - 0.5f) * maxTubeOffset * 2;
            tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + (i + 1) * distanceBetweenTubes;
        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2) {

                score++;
                Gdx.app.log("Score", String.valueOf(score));

                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }
            if (Gdx.input.justTouched()) {
                velocity = -30;
            }
            for (int i = 0; i< numberOfTubes; i++) {
                if (tubeX[i] < - topTube.getWidth()) {
                    tubeOffset[i] = (mRandom.nextFloat() - 0.5f) * maxTubeOffset * 2;
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                } else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

//                mShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                mShapeRenderer.setColor(Color.RED);
                topPipe.set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomPipe.set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
//                mShapeRenderer.rect(topPipe.x, topPipe.y, topPipe.width, topPipe.height);
//                mShapeRenderer.rect(bottomPipe.x, bottomPipe.y, bottomPipe.width, bottomPipe.height);
//                mShapeRenderer.end();

                if (Intersector.overlaps(birdCircle, topPipe) || Intersector.overlaps(birdCircle, bottomPipe)) {
                    gameState = 2;
                }

            }

            if (flapState == 0) {
                flapState = 1;
            } else {
                flapState = 0;
            }


            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            } else {
                gameState = 2;
            }
        } else if (gameState == 0){
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;
            }
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        mFont.draw(batch, String.valueOf(score), 100, 200);
        batch.end();

//        mShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        mShapeRenderer.setColor(Color.RED);
        birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2);
//        mShapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
//        mShapeRenderer.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
