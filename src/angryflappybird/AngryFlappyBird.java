package angryflappybird;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

//The Application layer
/**
 * @author bybird
 * AngryFlappyBird game made by team bybird
 * You collect gift box and wreath, while avoiding thief
 * Gift box: +5 points
 * Wreath: auto pilot mode for 6 seconds
 * 3 lives total
 * Don't collide with thief / pipes / floor!
 */
public class AngryFlappyBird extends Application {
	
	private Defines DEF = new Defines();
    
    // time related attributes
	private long clickTime, startTime, elapsedTime;
    private AnimationTimer timer;
    
    // game components
    private Sprite blob;
    private ArrayList<Sprite> floors;
    private ArrayList<Sprite> pipes;
    private ArrayList<Sprite> whiteEggs;
    private ArrayList<Sprite> goldenEggs;
    private ArrayList<Sprite> pigs;
    
    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER, GAME_SNOOZE;
    
    // scene graphs
    private Group gameScene;	 // the left half of the scene
    private VBox gameControl;	 // the right half of the GUI (control)
    private GraphicsContext gc;		
 
    // score and live variables
    int score = 0;
    Label scoreLabel = new Label("0");
    
    int live = 3;
    Label liveLabel = new Label("3");
    
    // autopilot mode variables
    private static final Integer COUNTDOWN = 6;
    private Timeline autopilot;
    private Label timerLabel = new Label();
    private Integer timeSeconds = COUNTDOWN;
   
    // pipe sound effect
    String pipeFile = "pipe.mp3";    
    Media pipeSound = new Media(new File(pipeFile).toURI().toString());
    MediaPlayer addPoints = new MediaPlayer(pipeSound);
    
    // autopilot mode sound effect
    String pilotFile = "pilot.wav";    
    Media pilotSound = new Media(new File(pilotFile).toURI().toString());
    MediaPlayer pilotMode = new MediaPlayer(pilotSound);
    
    // collision with pig sound effect  
    String pigFile = "pig.wav"; 
    Media pigSound = new Media(new File(pigFile).toURI().toString());   
    MediaPlayer hitPig = new MediaPlayer(pigSound);
    
    // background music
    String backgroundFile = "bgMusic.mp3";
    Media bgMusic = new Media(new File(backgroundFile).toURI().toString());
    MediaPlayer backgroundMusic = new MediaPlayer(bgMusic);

    // difficulty levels radio button
    RadioButton difficultyButton1 = new RadioButton("Easy");
    RadioButton difficultyButton2 = new RadioButton("Medium");
    RadioButton difficultyButton3 = new RadioButton("Hard");
    String toogleGroupValue = "Default";
    
    // default difficulty control integer
    int whiteEggAppearance = 4;
    int goldenEggAppearance = 7;
    int pigAppearance = 5;
    
    // Description text
    Text gameDescription1 = new Text("Bonus points");
    Text gameDescription2 = new Text("Lets you snooze");
    Text gameDescription3 = new Text("Avoid thief");

    // Start game title
    ImageView getReady = DEF.IMVIEW.get("ready");
    ImageView gameOver = DEF.IMVIEW.get("over");
    
    /** 
     * the mandatory main method
     * @param args
     * launching the game
     */
    public static void main(String[] args) {
        launch(args);
    }
       
    /** 
     * the start method sets the Stage layer
     * @param primaryStage start the game
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

    	// play background music
   	 	backgroundMusic.play();
   	 	backgroundMusic.seek(backgroundMusic.getStartTime());
    	
    	// initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
    	resetGameScene(true);  // resets the gameScene
    	
        HBox root = new HBox();
		HBox.setMargin(gameScene, new Insets(0,0,0,15));
		getReady.setLayoutX(120);
		getReady.setLayoutY(100);
		root.getChildren().add(gameScene);
		root.getChildren().add(gameControl);
		gameScene.getChildren().add(getReady);
		
		// add scene graphs to scene
        Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
        
        // finalize and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /** 
     * The getContent method sets the Scene layer
     * Set UI for game control
     * RadioButton for difficulty levels
     * game description with images
     */
    private void resetGameControl(){
        DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
        
        ToggleGroup radioGroup = new ToggleGroup();
        difficultyButton1.setToggleGroup(radioGroup);
        difficultyButton2.setToggleGroup(radioGroup);
        difficultyButton3.setToggleGroup(radioGroup);
        
        RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            toogleGroupValue = selectedRadioButton.getText();
        }
        
        ImageView gameDescriptionImg1 = DEF.IMVIEW.get("whiteEgg");
        gameDescriptionImg1.setFitWidth(30);
        gameDescriptionImg1.setFitHeight(30);
        HBox whiteEggBox = new HBox(gameDescriptionImg1, gameDescription1);
        whiteEggBox.setSpacing(10);
        
        ImageView gameDescriptionImg2 = DEF.IMVIEW.get("goldenEgg");
        gameDescriptionImg2.setFitWidth(30);
        gameDescriptionImg2.setFitHeight(30);
        HBox goldenEggBox = new HBox(gameDescriptionImg2, gameDescription2);
        goldenEggBox.setSpacing(10);
        
        ImageView gameDescriptionImg3 = DEF.IMVIEW.get("pig");
        gameDescriptionImg3.setFitWidth(30);
        gameDescriptionImg3.setFitHeight(30);
        HBox pigBox = new HBox(gameDescriptionImg3, gameDescription3);
        pigBox.setSpacing(10);
        
        gameControl = new VBox();
        gameControl.setSpacing(20);
        gameControl.setPadding(new Insets(10, 10, 10, 10));
        gameControl.getChildren().addAll(DEF.startButton);
        gameControl.getChildren().addAll(difficultyButton1, difficultyButton2, difficultyButton3);
        // display game descriptions
        gameControl.getChildren().add(whiteEggBox);
        gameControl.getChildren().add(goldenEggBox);
        gameControl.getChildren().add(pigBox);
    }
    
    private void mouseClickHandler(MouseEvent e) {
    	if (GAME_OVER) {
            resetGameScene(false);
        }
    	else if (GAME_START){
            clickTime = System.nanoTime();   
        }
    	GAME_START = true;
        CLICKED = true;
    }
    
    /** 
     * reset the game scene
     * @param firstEntry
     */
    private void resetGameScene(boolean firstEntry) {
    	
    	// reset variables
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
        GAME_SNOOZE = false;
        floors = new ArrayList<>();
        pipes = new ArrayList<>();
        whiteEggs = new ArrayList<>();
        goldenEggs = new ArrayList<>();
        pigs = new ArrayList<>();
        Random rand = new Random();
        
    	if(firstEntry) {
    	    if (toogleGroupValue == "Easy") {
    	        whiteEggAppearance = 2;
    	        goldenEggAppearance = 2;
    	        pigAppearance = 20;
    	    } else if (toogleGroupValue == "Medium") {
    	        whiteEggAppearance = 6;
    	        goldenEggAppearance = 10;
    	        pigAppearance = 10;
            } else if (toogleGroupValue == "Hard") {
                whiteEggAppearance = 10;
                goldenEggAppearance = 20;
                pigAppearance = 2;
            }
    	   
    	    
    	    Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // create a background
            ImageView background = DEF.IMVIEW.get("day");
            ImageView background2 = DEF.IMVIEW.get("night");
            
            // create the game scene
            gameScene = new Group();
            
            
            // add score text - data / font / location
            scoreLabel.setText(Integer.toString(score));
            scoreLabel.setStyle("-fx-font: 30 arial;");
            
            scoreLabel.setLayoutX(12);
            scoreLabel.setLayoutY(8);
            
            // add live text - data / font / location
            liveLabel.setText(Integer.toString(live)+" lives left");
            liveLabel.setStyle("-fx-font: 20 arial;");
            liveLabel.setTextFill(Color.web("#EB2866"));
            liveLabel.setLayoutX(300);
            liveLabel.setLayoutY(530);
      
            gameScene.getChildren().addAll(background, canvas, scoreLabel, liveLabel,timerLabel);
            
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
             gameScene.getChildren().removeAll(background, canvas, scoreLabel, liveLabel,timerLabel);
             gameScene.getChildren().addAll(background2, canvas, scoreLabel, liveLabel,timerLabel);
         }));
          timeline.setCycleCount(Animation.INDEFINITE);
          timeline.play();
         
    
         Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(20), ev -> {
             gameScene.getChildren().removeAll(background2,canvas, scoreLabel, liveLabel,timerLabel);
             gameScene.getChildren().addAll(background, canvas, scoreLabel, liveLabel,timerLabel);
         }));
          timeline2.setCycleCount(Animation.INDEFINITE);
          timeline2.play();
     }
    	
    	// initialize floor
    	for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    		
    		int posX = i * DEF.FLOOR_WIDTH;
    		int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    		
    		Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor"));
    		floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		floor.render(gc);
    		
    		floors.add(floor);
    	}
    	
    	// initialize pipes, eggs, and pigs
    	int posX_pipe;
    	int posY_pipe_1 = DEF.SCENE_HEIGHT - DEF.PIPE_HEIGHT1;
        int posY_pipe_2 = DEF.SCENE_HEIGHT - DEF.PIPE_HEIGHT2;
        
        int posY_egg_1 = DEF.SCENE_HEIGHT - DEF.PIPE_HEIGHT1 - 60;
        int posY_egg_2 = DEF.SCENE_HEIGHT - DEF.PIPE_HEIGHT2 - 60;

        int posY_pig = 50;
        
    	for (int i=0; i<DEF.PIPE_COUNT; i++) {
    	    posX_pipe = (i+1) * 180;
    	    
    	    int rand_num1 = rand.nextInt(2); 
            // rand_num1 could be 0 or 1. This random number is to randomly determine the pipe height.

    	    if (rand_num1 == 0) {
    	        Sprite pipe_bottom = new Sprite(posX_pipe, posY_pipe_1, DEF.IMAGE.get("pipe3"));
                pipe_bottom.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                pipe_bottom.render(gc);
                pipes.add(pipe_bottom);
                
                Sprite pipe_top = new Sprite(posX_pipe, 0, DEF.IMAGE.get("pipe4"));
                pipe_top.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                pipe_top.render(gc);
                pipes.add(pipe_top);
                
                int rand_num2 = rand.nextInt(whiteEggAppearance);
                // rand_num2 could be 0, 1, 2, or 3. This random number is to randomly determine if the white egg appear on the pipe.
                // There is a chance of 1/4 for the white egg to show up.
                if (rand_num2 == 0) {
                    Sprite egg = new Sprite(posX_pipe, posY_egg_1, DEF.IMAGE.get("whiteEgg"));
                    egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                    egg.render(gc);
                    whiteEggs.add(egg);
                } 
                else {
                    int rand_num3 = rand.nextInt(goldenEggAppearance);
                    // rand_num3 could be 0, 1, 2, 3, 4, 5, or 6. 
                    // If there is no white egg, this random number is initialized to determine if the golden egg appears on the pipe.
                    // If the white egg is not here, there is a chance of 1/7 for the golden egg to show up.
                    if (rand_num3 == 0) {
                        Sprite egg = new Sprite(posX_pipe, posY_egg_1, DEF.IMAGE.get("goldenEgg"));
                        egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                        egg.render(gc);
                        goldenEggs.add(egg);
                    } 
                }
                
                int rand_num4 = rand.nextInt(pigAppearance);
                // rand_num4 could be 0, 1, 2, 3, or 4. This random number is to determine if the pig appears and drops from the upper pipe.
                // There is a chance of 1/5 for the pig to show up.
                if (rand_num4 == 0) {                        
                    posY_pig = 65 - i * 45;
                    
                    Sprite pig = new Sprite(posX_pipe-10, posY_pig, DEF.IMAGE.get("pig"));
                    pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                    pig.render(gc);                
                    pigs.add(pig);
                }
    	    } 
    	    
    	    else {
    	        Sprite pipe_bottom = new Sprite(posX_pipe, posY_pipe_2, DEF.IMAGE.get("pipe1"));
                Sprite pipe_top = new Sprite(posX_pipe, 0, DEF.IMAGE.get("pipe2"));
                
                pipe_bottom.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                pipe_top.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                pipe_bottom.render(gc);
                pipe_top.render(gc);
                
                pipes.add(pipe_bottom);
                pipes.add(pipe_top);
                
                int rand_num2 = rand.nextInt(whiteEggAppearance);
                // rand_num2 could be 0, 1, 2, or 3. This random number is to randomly determine if the white egg appear on the pipe.
                // There is a chance of 1/5 for the white egg to show up.
                if (rand_num2 == 0) {
                    Sprite egg = new Sprite(posX_pipe, posY_egg_2, DEF.IMAGE.get("whiteEgg"));
                    egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                    egg.render(gc);
                    whiteEggs.add(egg);
                } 
                else {
                    int rand_num3 = rand.nextInt(goldenEggAppearance);
                    // rand_num3 could be 0, 1, 2, 3, 4, 5, or 6. 
                    // If there is no white egg, this random number is initialized to determine if the golden egg appears on the pipe.
                    // If the white egg is not here, there is a chance of 1/7 for the golden egg to show up.
                    if (rand_num3 == 0) {
                        Sprite egg = new Sprite(posX_pipe, posY_egg_2, DEF.IMAGE.get("goldenEgg"));
                        egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                        egg.render(gc);
                        goldenEggs.add(egg);
                    } 
                }
                
                int rand_num4 = rand.nextInt(pigAppearance);
                // rand_num4 could be 0, 1, 2, 3, or 4. This random number is to determine if the pig appears and drops from the upper pipe.
                // There is a chance of 1/5 for the pig to show up.
                if (rand_num4 == 0) {                        
                    posY_pig = 125 - i * 45;
                    
                    Sprite pig = new Sprite(posX_pipe-10, posY_pig, DEF.IMAGE.get("pig"));
                    pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                    pig.render(gc);
                    
                    pigs.add(pig);
                }
    	    }
    	}
        
    	// initialize blob
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y,DEF.IMAGE.get("bird1"));
        blob.render(gc);
        
        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
    }
    
    /** 
     * timer stuff
     */
    class MyTimer extends AnimationTimer {
    	
    	int counter = 0;
    	
    	 @Override
    	 public void handle(long now) {   		 
    		 // time keeping
    	     elapsedTime = now - startTime;
    	     startTime = now;
    	     
    	     // clear current scene
    	     gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
    	     if (GAME_START && !GAME_SNOOZE) {
    	    	 gameScene.getChildren().remove(getReady);
    	    	 gameScene.getChildren().remove(gameOver);
    	         
    	         if (live == 0) {
                     live = 3;
                     score = 0;
                     scoreLabel.setText(Integer.toString(score));
                     liveLabel.setText(Integer.toString(live)+" lives left");
                 }
    	    	 // step1: update floor
    	    	 moveFloor();
    	    	 
    	    	 // step2: update blob
    	    	 moveBlob();
    	    	 
    	    	 // step3: update pipes
                 movePipe();
    	    	 
    	    	 // step4: show eggs
    	    	 moveWhiteEgg();
                 moveGoldenEgg();

    	    	 // step5: drop pigs
    	    	 movePig();
    	    	 
    	    	 checkCollisionFloor();
    	    	 checkCollisionPipe();
    	    	 checkCollisionWhiteEgg();
    	    	 checkCollisionGoldenEgg();
    	    	 checkCollisionPig();
    	    	 checkCollisioPigAndEgg();
    	     } 
    	     
    	     if (GAME_START && GAME_SNOOZE) {
    	    	 gameScene.getChildren().remove(getReady);


                   // step1: update floor
                   moveFloor();
                   // step2: update blob
                   snoozeBlob();
                   // step3: update pipes
                   movePipe();
                   // step4: show eggs
                   moveWhiteEgg();
                   moveGoldenEgg();
    	     }
    	 }

    	 /** 
	     * step 1: update floor
	     */
    	 private void moveFloor() {
    		
    		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    			if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
    				double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
    	        	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    	        	floors.get(i).setPositionXY(nextX, nextY);
    			}
    			floors.get(i).render(gc);
    			floors.get(i).update(DEF.SCENE_SHIFT_TIME);
    		}
    	 }

    	 /** 
          * step 2: update blob
          */
    	 private void moveBlob() {
    		 
			long diffTime = System.nanoTime() - clickTime;
			
			// blob flies upward with animation
			if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
				
				int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
				imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
				blob.setImage(DEF.IMAGE.get("bird"+String.valueOf(imageIndex+1)));
				blob.setVelocity(0, DEF.BLOB_FLY_VEL);
			}
			// blob drops after a period of time without button click
			else {
			    blob.setVelocity(0, DEF.BLOB_DROP_VEL); 
			    CLICKED = false;
			}

			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			blob.render(gc);
    	 }
    	 
    	 /** 
          * step 3: move pipes
          */
    	 private void movePipe() {
             for(Sprite pipe: pipes) {
                 pipe.setVelocity(-DEF.PIPE_VEL, 0);
                 pipe.update(elapsedTime * DEF.NANOSEC_TO_SEC);
                 pipe.render(gc);
             }
         }
    	  
    	 /** 
          * step 4: update white eggs
          */
    	 private void moveWhiteEgg() {
             for(Sprite egg: whiteEggs) {
                 egg.setVelocity(-DEF.PIPE_VEL, 0);
                 egg.update(elapsedTime * DEF.NANOSEC_TO_SEC);
                 egg.render(gc);
             }
         }
    	 
    	 /** 
          * step 5: update golden eggs
          */
    	 private void moveGoldenEgg() {
             for(Sprite egg: goldenEggs) {
                 egg.setVelocity(-DEF.PIPE_VEL, 0);
                 egg.update(elapsedTime * DEF.NANOSEC_TO_SEC);
                 egg.render(gc);
             }
         }
    	 
    	 /** 
          * snooze blob
          */
    	 private void snoozeBlob() {
    	    blob.setPositionXY(DEF.BLOB_POS_X, 200);
    	    blob.setImage(DEF.IMAGE.get("bird1"));
    	    blob.setVelocity(DEF.BLOB_FLY_VEL,0);
    	    blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
    	    blob.render(gc);
    	 }
    	 
    	 /** 
          * step 6: update pig
          */
    	 private void movePig() {
    	     for(Sprite pig: pigs) {
    	         pig.setVelocity(-DEF.PIPE_VEL, 30);
    	         pig.update(elapsedTime * DEF.NANOSEC_TO_SEC);
    	         pig.render(gc);
    	     }	
    	 }

    	 private void stopBlob() {
    		 double posX = blob.getPositionX();
    		 double posY = blob.getPositionY();
    		 blob.setImage(DEF.IMAGE.get("bird1"));
    		 blob.setPositionXY(posX, posY);
    		 blob.setVelocity(-2,2);
    		 blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
    		 blob.render(gc);
    		 }

    	 
    	 /** 
          * check collision between bird and floor
          */
    	 public void checkCollisionFloor() {
             // check collision w/ floor
             for (Sprite floor: floors) {
                 GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
             }
             
             // end the game when blob hit stuff
             if (GAME_OVER) {
                 showHitEffect(); 
                 for (Sprite floor: floors) {
                     floor.setVelocity(0, 0);
                 }
                 
                 live = 0;
                 liveLabel.setText(Integer.toString(live)+" lives left");
                 resetGameScene(false);
                 timer.stop();
             }
             
          }
    	 
    	 /** 
          * check collision between bird and pipe
          */
    	 public void checkCollisionPipe() {
             // check collision w/ pipe
             for (Sprite pipe: pipes) {
            	 if (blob.intersectsSprite(pipe)) {
            		 stopBlob();
            		 hitPig.play(); 
                     hitPig.seek(hitPig.getStartTime());
            	 }
                 GAME_OVER = GAME_OVER || blob.intersectsSprite(pipe);
             }

             // end the game when blob hit stuff
             if (GAME_OVER) {
            	 
                 showHitEffect(); 
                 for (Sprite floor: floors) {
                     floor.setVelocity(0, 0);
                 }
                 
                 if (live == 1) {
                     // !!!!! display game over
                     live = 0;
                     liveLabel.setText(Integer.toString(live)+" lives left");
                     gameOver.setLayoutX(120);
                     gameOver.setLayoutY(100);
                     gameScene.getChildren().add(gameOver);
                     resetGameScene(false);
                 } else {
                     live = live - 1;
                     liveLabel.setText(Integer.toString(live)+" lives left");
                     resetGameScene(false);
                 }
                 
                 timer.stop();
             }
             
          }
    	 
    	 /** 
          * check collision between bird and white egg
          */
    	 public void checkCollisionWhiteEgg() {
         // check collision w/ egg
            for (Sprite egg: whiteEggs) {
                if (blob.intersectsSprite(egg)) {
                    score = score + 5;
                    scoreLabel.setText(Integer.toString(score));
                    egg.setPositionXY(10000,10000);
                    addPoints.play();
                    addPoints.seek(addPoints.getStartTime());
                }
            }
    	 }
    	 
    	 /** 
          * check collision between bird and golden egg
          */
    	 public void checkCollisionGoldenEgg() {
         // check collision w/ egg
            for (Sprite egg: goldenEggs) {
                if (blob.intersectsSprite(egg)) {
                    egg.setPositionXY(10000,10000);
                    countdown();
                }
            }
         }
    	 
    	 /** 
          * check collision between pig and egg
          */
    	 
    	 public void checkCollisioPigAndEgg() {
             // check collision pig and egg
    	     for (Sprite pig: pigs) {
    	         for (Sprite egg: whiteEggs) {
                     if (pig.intersectsSprite(egg)) {
                         score = score - 5;
                         egg.setPositionXY(10000,10000);
                         scoreLabel.setText(Integer.toString(score));
                     }
                 }
                 for (Sprite egg: goldenEggs) {
                     if (pig.intersectsSprite(egg)) {
                         score = score - 5;
                         egg.setPositionXY(10000,10000);
                         scoreLabel.setText(Integer.toString(score));
                     }
                 }
    	     }
         }
    	 
    	 /** 
          * check collision between bird and pig
          */
    	 public void checkCollisionPig() {
             // check collision w/ pig
             for (Sprite pig: pigs) {
                 GAME_OVER = GAME_OVER || blob.intersectsSprite(pig);
                 if (blob.intersectsSprite(pig)) {
                     hitPig.play(); 
                     hitPig.seek(hitPig.getStartTime());
                 }
             }
             
             // !!!!! display game over
             
             // end the game when blob hit stuff
             if (GAME_OVER) {
                 showHitEffect(); 
                 for (Sprite pig: pigs) {
                     pig.setVelocity(0, 0);
                 }
                 live = 0;
                 liveLabel.setText(Integer.toString(live)+" lives left");
                 resetGameScene(false);
                 
                 timer.stop();
             }
         }
		
    	 /** 
          * countdown method for auto pilot mode
          */
		private void countdown() { 
			// stop background music
			backgroundMusic.stop();
            GAME_SNOOZE = true; 
            timeSeconds = COUNTDOWN;    
            timerLabel.setLayoutX(12);  
            timerLabel.setLayoutY(35);  
            timerLabel.setText(timeSeconds.toString()+" secs to go");   
            timerLabel.setTextFill(Color.WHITE);    
            timerLabel.setStyle("-fx-font: 30 arial;");     
            autopilot = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {  
                timeSeconds--;  
                // update timerLabel    
                timerLabel.setText(timeSeconds.toString()+" secs to go");           
                if (timeSeconds <= 0) { 
                    autopilot.stop();   
                    GAME_SNOOZE = false;    
                    timerLabel.setText("");    
                    gameScene.getChildren().remove(timerLabel); 
                    // replay background music
               	 	backgroundMusic.play();
               	 	backgroundMusic.seek(backgroundMusic.getStartTime());
                }   
            }));    
            autopilot.setCycleCount(Timeline.INDEFINITE);   
            autopilot.playFromStart();  
            pilotMode.play();       
            pilotMode.seek(pilotMode.getStartTime());
        }
    	 
		/** 
         * hit effect when there is a collision
         */
	     private void showHitEffect() {
	        ParallelTransition parallelTransition = new ParallelTransition();
	        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
	        
	        fadeTransition.setToValue(0);
	        fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
	        fadeTransition.setAutoReverse(true);
	        parallelTransition.getChildren().add(fadeTransition);
	        parallelTransition.play();
	     }
    	 
    } // End of MyTimer class
                                                          
} // End of AngryFlappyBird Class
