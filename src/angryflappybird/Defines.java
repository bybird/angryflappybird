package angryflappybird;

import java.io.File;
import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Defines {
    
	// dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;
    
    // coefficients of start and over title
    final int SCENE_TITLE_HEIGHT = 50;
    final int SCENE_TITLE_WIDTH =178;

    // coefficients related to the blob
    final int BLOB_WIDTH = 60;
    final int BLOB_HEIGHT = 45;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -40;
    final int BLOB_IMG_LEN = 8;
    final int BLOB_IMG_PERIOD = 9;
    
    // coefficients related to the floors
    final int FLOOR_WIDTH = 400;
    final int FLOOR_HEIGHT = 60;
    final int FLOOR_COUNT = 2;
    
    // coefficients related to the pipes
    final int PIPE_WIDTH = 60;
    final int PIPE_HEIGHT1 = 160;
    final int PIPE_HEIGHT2 = 100;
    final int PIPE_COUNT = 200;
    final int PIPE_VEL = 120;
    
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.3;
    final int TRANSITION_CYCLE = 2;
    
    // coefficients related to egg
    final int EGG_WIDTH = 60;
    final int EGG_HEIGHT = 60;
    final int EGG_IMG_LEN = 3;
    
    // coefficients related to pig
    final int PIG_WIDTH = 80;
    final int PIG_HEIGHT = 80;
    
    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Bird";
	private final String IMAGE_DIR = "../resources/images/";
	
    final String[] IMAGE_FILES = {"bird1","bird2","bird3","bird4","bird5","bird6","bird7","bird8","floor","pig", "whiteEgg","goldenEgg","pipe1","pipe2","pipe3","pipe4","day","night","over","ready"};

    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    
    //nodes on the scene graph
    Button startButton;
    String flapFile = "flap.mp3";     
    Media flapSound = new Media(new File(flapFile).toURI().toString());
    MediaPlayer flap = new MediaPlayer(flapSound);
    
    
    // constructor
	Defines() {
		
		// initialize images
        for(int i=0; i<IMAGE_FILES.length; i++) {
            Image img;
            if (i < 8) {
                img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
            }
            else if (i == 8) {
                img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
            }
            else if (i == 9) {
                img = new Image(pathImage(IMAGE_FILES[i]), PIG_WIDTH, PIG_HEIGHT, false, false);
            }
            else if (i == 10 || i == 11) {
                img = new Image(pathImage(IMAGE_FILES[i]), EGG_WIDTH, EGG_HEIGHT, false, false);
            }
            else if (i == 12 || i == 15) {
                img = new Image(pathImage(IMAGE_FILES[i]), PIPE_WIDTH, PIPE_HEIGHT2, false, false);
            }
            else if (i == 13 || i == 14) {
                img = new Image(pathImage(IMAGE_FILES[i]), PIPE_WIDTH, PIPE_HEIGHT1, false, false);
            }
            else if (i == 18 || i == 19) {
            	img = new Image(pathImage(IMAGE_FILES[i]), SCENE_TITLE_WIDTH, SCENE_TITLE_HEIGHT, false, false);
            }
            else {
                img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
            }
            IMAGE.put(IMAGE_FILES[i],img);
        }
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		
		// initialize scene nodes
		startButton = new Button("Start Game");
		startButton.setOnAction(event -> {
			flap.play();
		});
	}
	
	public String pathImage(String filepath) {
    	String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }
}