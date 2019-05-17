package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Main extends Application {
    //music
    private String gameSong = "src/sound/song1.mp3";
    private javafx.scene.media.Media gameMedia = new javafx.scene.media.Media(new File(gameSong).toURI().toString());
    private MediaPlayer gameMediaPlayer = new MediaPlayer(gameMedia);

    private String menuSong = "src/sound/menuSong.mp3";
    private javafx.scene.media.Media menuMedia = new javafx.scene.media.Media(new File(menuSong).toURI().toString());
    private MediaPlayer menuMediaPlayer = new MediaPlayer(menuMedia);

    //sounds
    private AudioClip defeatSound = new AudioClip(Paths.get("src/sound/defeat.wav").toUri().toString());

    //global variables
    private final int WIDTH = 960, HEIGHT = 640;
    private final int PLAYERSTARTX = 450, PLAYERSTARTY = 300;
    private final int PLAYER_START_LIVES = 4;
    private final int TIME_LIMIT = 60 * 20 * 1; // 20 seconds
    private Mode mode = Mode.STARTMENU;
    private int counter;
    private int timeCounter = 0;
    private KeyPad keyPad = new KeyPad(WIDTH, HEIGHT);
    private HelpPopUp helpPopUp;

    private Pane appRoot = new Pane();
    private Map map;
    private Scene mainScene;
    private GameOver gameOver = new GameOver(WIDTH, HEIGHT);
    private GameMenu gameMenu = new GameMenu(WIDTH, HEIGHT);
    private InstructionScreen instructionScreen = new InstructionScreen(WIDTH, HEIGHT);

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private Player player = new Player("Hero", PLAYERSTARTX, PLAYERSTARTY, PLAYER_START_LIVES);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        menuMediaPlayer.play();
        menuMediaPlayer.setVolume(20);
        initContent(1);
        //initialise Scene/game
        mainScene = new Scene(gameMenu.returnRoot(), WIDTH, HEIGHT);
        mainScene.setFill(Color.BLACK);
        mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setScene(mainScene);
        primaryStage.show();
        runGame(primaryStage);
        gameMenu.instructions().setOnAction(instructionButtonHandler);
        gameMenu.startGame().setOnAction(startButtonHandler);
        instructionScreen.returnButton().setOnAction(returnButtonHandler);
    }

    private void initContent(int level) {
        if (gameMediaPlayer.getStatus() != MediaPlayer.Status.PLAYING && mode == Mode.PLATFORMGAME)
            gameMediaPlayer.play();
        appRoot.getChildren().clear();
        //initialise background
        Image back1 = new Image("/graphics/background1.png", true);
        BackgroundImage background = new BackgroundImage(back1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        appRoot.setBackground(new Background(background));

        //initialise map / contents of the appRoot (i.e. the player, platforms, blocks, etc.)
        map = new Map(level); //initialises level based on level number input
        appRoot.getChildren().addAll(map.mapRoot());
        //for anything that is animated, add them here, e.g. spinning blocks, player, clouds.
        map.addPlayer(player, PLAYERSTARTX, PLAYERSTARTY);

        /*Initialise help pop up*/
        try {
            helpPopUp = new HelpPopUp("Press 'E' to open door", WIDTH - 400, HEIGHT - 70);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        appRoot.getChildren().add(helpPopUp.getPopUp());
        helpPopUp.setVisible(false);
    }


    /*Changes the level based on level number. New levels can be created in Grid class*/
    private void changeLevel(int level) {
        map.removePlayer(player);
        initContent(level);
    }

    private void runGame(Stage stage) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(stage);
            }
        };
        timer.start();
    }


    /***************************************************************************
     *                           GAME UPDATE HANDLER
     * **********************************************************************/

    private void update(Stage stage) {

        if (keyPad.isCodeCorrect()) {
            changeLevel(2);
            keyPad.setCodeCorrect(false);
        }
        if (mode == Mode.GAMEOVER) gameOver();
        if (mode == Mode.KEYPAD) handleKeyPad();
        if (mode == Mode.MINIGAME) {
            if (isPressed(KeyCode.ESCAPE)) {
                mode = Mode.PLATFORMGAME;
                mainScene.setRoot(appRoot);
            }
        }

        if (mode == Mode.PLATFORMGAME) {

            /*  Handles all the game events, including player motion and interaction with items  */
            if (isPressed(KeyCode.A)) {
                player.setFacingRight(false);
                if (player.move_X(-5, map)) moveScreenX(-5);
            } else if (isPressed(KeyCode.D)) {
                player.setFacingRight(true);
                if (player.move_X(5, map)) moveScreenX(5);
            }
            if (isPressed(KeyCode.W)) {
                player.jump();
            }

            moveScreenY();
            ListenerEnemies();
            ListenerItemsEvent();
            ListenerPlayerLives();
            ListenerPlayerUseWeapon();
            ListenerPlayerKillEnemy();
            ListenerButtons();
            UpdateAnimatedObjects();
            ListenerGameOver();
            ListenerTimeBeforeNewEnemyWave();
            ListenerKeyPad();
            ListenerHelpPopUp();
        }
    }

    /*Use this method if you want to change the content and position of the popup*/
    private void handlePopUp(String content, double posX, double posY, int radius, boolean visible) {
        helpPopUp.setPopUpText(content);
        helpPopUp.setPosX(posX + radius);
        helpPopUp.setPosY(posY - radius);
        helpPopUp.setVisible(visible);
    }

    //updates the screen X based on player position
    private void moveScreenX(int movement) {
        if (player.getX() > WIDTH / 2 + 8 && player.getX() < map.level().width() - WIDTH / 2 - 32) {
            counter++;
            //this moves everything in the screen with the character
            map.mapRoot().setTranslateX(map.level().width() - player.getX() - WIDTH);
            //this is for parallax scrolling of background elements
            if (counter == 5 || counter == 10) {
                for (Object object : map.animatedObjects())
                    if (object.type == Type.LAYER2) object.setX(object.getX() - movement / 5);
            }
            if (counter == 10) {
                for (Object object : map.animatedObjects())
                    if (object.type == Type.LAYER4) object.setX(object.getX() - movement / 5);
                counter = 0;
            }
        }
    }

    //updates the screen Y based on player position
    private void moveScreenY() {

        if (player.getY() > HEIGHT / 2 + 40 && player.getY() < map.level().height() - HEIGHT / 2 - 64) {
            map.mapRoot().setTranslateY(map.level().height() - player.getY() - HEIGHT);
        }
    }

    private void UpdateAnimatedObjects() {
        for (Object object : map.animatedObjects()) {
            object.update(map);
        }
    }

    /***************************************************************************
     *                              LISTENERS FOR EVENTS
     * **********************************************************************/

    private void ListenerKeyPad() {
        if (map.getCurrentLevel() == 1 && player.getX() > 1270 && player.getY() == 644 && isPressed(KeyCode.E)) {
            openKeyPad();
        }
    }

    private void ListenerHelpPopUp() {
        if (map.getCurrentLevel() == 1 && player.getX() > 1270 && player.getY() == 644) {
            helpPopUp.setVisible(true);
        } else helpPopUp.setVisible(false);
    }


    /* ---------- PLAYER ----------- */

    private void ListenerItemsEvent() {
        for (Collectable item : map.getItems()) {
            if ((this.player.box.getBoundsInParent()).intersects(item.box.getBoundsInParent())) {
                /* pickup item */
                if (item.isAlive() && isPressed(KeyCode.E)) {
                    player.getLuggage().take(item);
                    map.hideEntity(item);
                    if (item.getItemType() == Type.BLOCK) {
                        miniGameKey();
                    }
                }
            }
        }
        /* drop item  TODO: see how to drop WEAPONS - do we still want to drop blocks ? */
        Block myBlock = player.getLuggage().getblock();
        if (myBlock != null) {
            if (!myBlock.isAlive()) {
                if (isPressed(KeyCode.Z)) {
                    double new_X = player.getX();
                    double new_Y = player.getY();
                    player.getLuggage().drop(myBlock, new_X, new_Y);
                    map.addItem(myBlock);
                }
            }
        }
    }

    private void ListenerPlayerLives() {
        int collision = 0;
        for (Enemy enemy : map.getEnemies()) {
            if (enemy.isAlive()) {
                if (player.box.getBoundsInParent().intersects(enemy.box.getBoundsInParent())) {
                    collision++;
                    // Waits that player moves out from the enemy to loose another life
                    if (player.isCanDie()) {
                        player.LooseOneLive();
                        player.setCanDie(false);
                    }
                }
            }
        }
        // if player not on any enemy
        if (collision == 0) {
            player.setCanDie(true);
        }
    }

    private void ListenerPlayerUseWeapon() {
        if (player.hasWeapon()) {
            // If player allowed to use weapon and has bullets left
            if (isPressed(KeyCode.SPACE) && player.canUseWeapon()) {
                Bullet bullet = new Bullet(player.getX() + 5, player.getY() + player.height / 4, player.isFacingRight());
                appRoot.getChildren().add(bullet.label());
                map.animatedObjects().add(bullet);
                map.addAnimatedObjects(bullet);
                player.getLuggage().getWeapon().looseOneBullet();
                player.getLuggage().getWeapon().setCanShoot(false);
            }
            // User can only shoot 1 bullet when press key (un-press & press again to shoot)
            if (!isPressed(KeyCode.SPACE)) {
                player.getLuggage().getWeapon().setCanShoot(true);
            }
        }
    }

    private void ListenerPlayerKillEnemy() {
        ArrayList<Object> deads = new ArrayList<>();
        // moves existing bullets
        for (Object obj : map.animatedObjects()) {
            if (obj instanceof Bullet && obj.isAlive()) {
                ((Bullet) obj).move(map);

                // check if bullet collide with any enemy
                // TODO: remove bullets & enemy who are dead from animatedObjects array
                for (Enemy enemy : map.getEnemies()) {
                    if (enemy.isCanDie() && enemy.box.getBoundsInParent().intersects(obj.box.getBoundsInParent())) {
                        deads.add(obj);
                        enemy.looseOneLife();
                        enemy.setCanDie(false);
                    } else {
                        enemy.setCanDie(true);
                    }
                    if (enemy.getLives() == 0) {
                        deads.add(enemy);
                    }
                }
            }
        }
        for (Object dead : deads) {
            map.hideEntity(dead);
            map.animatedObjects().remove(dead);
        }
        map.getEnemies().removeIf(e -> !e.isAlive());
    }

    /* ----------- ENEMIES ------------ */

    private void ListenerEnemies() {
        for (Enemy enemy : map.getEnemies()) {

            // check if enemy died
            if (isObjectOutOfBounds(enemy)) {
                enemy.setAlive(false);
            }
            // if enemy alive - give motion
            if (enemy.isAlive() && enemy.getCanMove()) {
                enemy.giveMotion(map);
            }
        }
    }

    // Sends a new wave of enemy to the game every so often
    // New enemies are sent 1 by 1 (every second)
    // will keep going until there is more 10 enemies on the map
    private void ListenerTimeBeforeNewEnemyWave() {
        timeCounter++;
        int WAVE_SIZE = 3;
        if (map.getEnemies().size() < 10) {
            if (timeCounter >= TIME_LIMIT && timeCounter % 60 == 0) addRandomEnemy();
            if (timeCounter > TIME_LIMIT + WAVE_SIZE * 60) timeCounter = 0;
        }
    }

    private void addRandomEnemy() {
        // randomise position and behaviour of enemy created
        int rand = new Random().nextInt(3);
        map.addEnemy(rand);
        Enemy newEnemy = map.getEnemies().get(map.getEnemies().size() - 1); //retrieve last enemy added
        map.animatedObjects().add(newEnemy);
    }

    /* ----------- BUTTONS ------------ */
    private void ListenerButtons() {
        for (PlatformButton button : map.getLevel().buttons()) {
            if (!(this.player.box.getBoundsInParent()).intersects(button.box.getBoundsInParent())) {
                button.setPressed(false);
            }
            if (!button.isPressed() && (this.player.box.getBoundsInParent()).intersects(button.box.getBoundsInParent()) && player.movingDown && !player.isLanded) {
                for (Platform platform : map.getLevel().platforms()) {
                    if (platform.canDisappear() && platform.isAlive() && platform.getColour().equals(button.getColour())) {
                        platform.setAlive(false);
                        platform.setCollisionBox(0, 0, 0, 0, Color.DARKORANGE);
                        map.setButton(button.getColour());
                    } else if (platform.canDisappear() && !platform.isAlive() && !platform.getColour().equals(button.getColour())) {
                        platform.restoreCollisionBox();
                        platform.setAlive(true);
                        map.setButton(button.getColour());
                    }
                }
                button.setPressed(true);
            }
        }
    }


    /***************************************************************************
     *                              GAME OVER
     * **********************************************************************/

    private boolean isObjectOutOfBounds(Object object) {
        if (object.getY() > map.level().height()
                || object.getX() >= map.level().width() - map.level().getOBJ_WIDTH()
                || object.getX() < map.level().getOBJ_WIDTH()) {
            return true;
        }
        return false;
    }

    private void ListenerGameOver() {
        if (player.getLives() > 0 && !isObjectOutOfBounds(player)) {
            return;
        }
        if (mode != mode.GAMEOVER) { //This get called when you're playing and then you DIE!
            mainScene.setRoot(gameOver.returnRoot());
            defeatSound.play();
            mode = Mode.GAMEOVER;
            changeLevel(1);
        }
    }

    private void gameOver() {
        if (isPressed(KeyCode.SPACE)) {
            mainScene.setRoot(appRoot);
            player.setLives(PLAYER_START_LIVES);
            keys.clear(); /**added to prevent input from previous game being called on reset**/
            mode = Mode.PLATFORMGAME;
        }
    }


    /***************************************************************************
     *                              MENU BUTTONS
     * **********************************************************************/


    EventHandler<ActionEvent> startButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            mode = Mode.PLATFORMGAME;
            mainScene.setRoot(appRoot);
            menuMediaPlayer.stop();
            timeCounter = 0;
        }
    };

    EventHandler<ActionEvent> instructionButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            mainScene.setRoot(instructionScreen.returnRoot());
        }
    };

    EventHandler<ActionEvent> returnButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            mainScene.setRoot(gameMenu.returnRoot());
        }
    };

    private Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }



    /***************************************************************************
     *                              MINI GAME
     * **********************************************************************/

    private void miniGameKey() {
        /* Mini games activated once player collects a block on the map */
        try {
            KeyGame mini = new KeyGame();
            AnchorPane game = mini.returnRoot();
            mainScene.setRoot(game);
            mainScene.getRoot().requestFocus();
            mode = Mode.MINIGAME;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /***************************************************************************
     *                            KEY PAD
     * **********************************************************************/

    /*Creates the keyPad and changes game mode*/
    private void openKeyPad() {
        keyPad = new KeyPad(WIDTH, HEIGHT);
        Group keyPadRoot = new Group();
        keyPad.initialise();
        keyPadRoot.getChildren().add(keyPad.getRoot());
        mainScene.setRoot(keyPadRoot);
        mode = Mode.KEYPAD;
    }


    /*Handles all button clicks on the keyPad*/
    private void handleKeyPad() {
        keyPad.getOne().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getOne().getValue()))));
        keyPad.getTwo().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getTwo().getValue()))));
        keyPad.getThree().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getThree().getValue()))));
        keyPad.getFour().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getFour().getValue()))));
        keyPad.getFive().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getFive().getValue()))));
        keyPad.getSix().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getSix().getValue()))));
        keyPad.getSeven().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getSeven().getValue()))));
        keyPad.getEight().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getEight().getValue()))));
        keyPad.getNine().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getNine().getValue()))));
        keyPad.getZero().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(Integer.toString(keyPad.getZero().getValue()))));
        keyPad.getClear().setOnAction(event -> keyPad.setDisplayText(""));
        keyPad.getExit().setOnAction(event -> {
            mainScene.setRoot(appRoot);
            mode = Mode.PLATFORMGAME;
        });
        keyPad.getEnter().setOnAction(event -> {
            if (keyPad.getDisplayText().equals("1234")) {
                mainScene.setRoot(appRoot);
                keyPad.setCodeCorrect(true);
                mode = Mode.PLATFORMGAME;
            } else {
                keyPad.setDisplayText("WRONG CODE!");
            }
        });
    }
}
