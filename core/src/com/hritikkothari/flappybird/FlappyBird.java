package com.hritikkothari.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;

import sun.rmi.runtime.Log;

import static java.awt.Color.RED;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;

	Texture[] birds;
	int flapstate=0;
	float birdY=0;
	float velocity=0;
	Circle birdcircle;

	int gameState=0;
	float gravity=2;

	Texture toptube;
	Texture bottomtube;
	float gap=400;
	float maxTubeOffset;
	Random r=new Random();

	float tubeVelocity=4;
	int numberOfTubes=4;
	float[] tubeX= new float[numberOfTubes];
	float[] tubeOffset= new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] toptuberectangles;
	Rectangle[] bottomtuberectangles;

	int score=0;
	int scoringtube=0;
	BitmapFont font;
	Texture gameover;


	
	@Override
	public void create () {

		batch = new SpriteBatch();
		background=new Texture("bg.png");
		birdcircle=new Circle();
		//shapeRenderer=new ShapeRenderer();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		gameover=new Texture("gameover.png");

		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");


		toptube=new Texture("toptube.png");
		bottomtube= new Texture("bottomtube.png");
		maxTubeOffset=Gdx.graphics.getHeight()/2 - gap/2 - 100;
		distanceBetweenTubes=Gdx.graphics.getWidth() * 3/4;
		toptuberectangles=new Rectangle[numberOfTubes];
		bottomtuberectangles=new Rectangle[numberOfTubes];

		startGame();

	}
	public void startGame(){
		birdY=Gdx.graphics.getHeight()/2 -birds[0].getHeight()/2;

		for(int i=0;i<numberOfTubes;i++)
		{
			tubeOffset[i]=(r.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap - 200 );

			tubeX[i]=Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 +Gdx.graphics.getWidth()+ i*distanceBetweenTubes;

			toptuberectangles[i]=new Rectangle();
			bottomtuberectangles[i]=new Rectangle();

		}

	}

	@Override
	public void render () {

		batch.begin();

		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState==1){

			if(tubeX[scoringtube]<Gdx.graphics.getWidth()/2){
				score++;

				if(scoringtube<numberOfTubes-1){
				    scoringtube++;   }
				else {
                    scoringtube = 0;
                }
			}

			if(Gdx.input.justTouched())
			{
				velocity=velocity-30;

			}

			for(int i=0;i<numberOfTubes;i++) {

				if(tubeX[i]<-toptube.getWidth()){

					tubeX[i]=tubeX[i]+ numberOfTubes * distanceBetweenTubes;
					tubeOffset[i]=(r.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap - 200 );

				}else {

					tubeX[i] = tubeX[i] - tubeVelocity;

				}
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);

				toptuberectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
			    bottomtuberectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i],bottomtube.getWidth(),bottomtube.getHeight());

			}


			if(birdY>0)
			{
				velocity=velocity+gravity;
				birdY-=velocity;
			}
			else{
			    gameState=2;
            }

		}else if(gameState==0)
		{
			if(Gdx.input.justTouched())
			{

				gameState=1;

			}

		}
		else if(gameState==2)
          {
              batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
              font.draw(batch,"Your Score: " + String.valueOf(score),Gdx.graphics.getWidth()/2-gameover.getWidth()/2-150,Gdx.graphics.getHeight()/2-gameover.getHeight()/2 - 50);

              if(Gdx.input.justTouched())
			  {
			  	gameState=1;
			  	startGame();
			  	score=0;
			  	scoringtube=0;
			  	velocity=0;
			  }
          }

		if(flapstate==0){
			flapstate=1;
		}
		else{
			flapstate=0;
		}

		batch.draw(birds[flapstate],Gdx.graphics.getWidth()/2 - birds[flapstate].getWidth()/2,birdY);

		font.draw(batch,String.valueOf(score),100,200);

		birdcircle.set(Gdx.graphics.getWidth()/2,birdY+ birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);



		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
		//shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);

		for(int i=0;i<numberOfTubes;i++){

			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i],bottomtube.getWidth(),bottomtube.getHeight());

			if(Intersector.overlaps(birdcircle,toptuberectangles[i])|| Intersector.overlaps(birdcircle,bottomtuberectangles[i]))
			 {
			                          //Game over
                 gameState=2;

			 }
		}

		batch.end();
		//shapeRenderer.end();

	}



	@Override
	public void dispose () {
		batch.dispose();
	}
}
