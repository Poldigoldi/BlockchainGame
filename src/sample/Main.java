package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    //music
    private String menuSong = "src/sound/menuSong.mp3";
    private javafx.scene.media.Media menuMedia = new javafx.scene.media.Media(new File(menuSong).toURI().toString());
    private MediaPlayer menuMediaPlayer = new MediaPlayer(menuMedia);

    private String gameSong = "src/sound/Ninja-Game-Intro.mp3";
    private javafx.scene.media.Media gameMedia = new javafx.scene.media.Media(new File(gameSong).toURI().toString());
    private MediaPlayer gameMediaPlayer = new MediaPlayer(gameMedia);

    private String bossSong = "src/sound/bossSong.mp3";
    private javafx.scene.media.Media bossMedia = new javafx.scene.media.Media(new File(bossSong).toURI().toString());
    private MediaPlayer bossMediaPlayer = new MediaPlayer(bossMedia);

    //sounds
    private AudioClip defeatSound = new AudioClip(Paths.get("src/sound/defeat.wav").toUri().toString());
    private AudioClip shootSound = new AudioClip(Paths.get("src/sound/shoot.wav").toUri().toString());
    private AudioClip doorOpenSound = new AudioClip(Paths.get("src/sound/dooropen.wav").toUri().toString());
    private AudioClip playerhurtSound = new AudioClip(Paths.get("src/sound/playerhurt.wav").toUri().toString());
    private AudioClip enemy1hurtSound = new AudioClip(Paths.get("src/sound/enemy1hurt.wav").toUri().toString());
    private AudioClip enemy1dieSound = new AudioClip(Paths.get("src/sound/enemy1die.wav").toUri().toString());

    //global variables
    private int WIDTH, HEIGHT;
    private int level = 1;
    private final int PLAYERSTARTX = 450, PLAYERSTARTY = 300, PLAYER_START_LIVES = 4;
    private final int TIME_LIMIT = 60 * 20 * 1; // 20 seconds
    private Mode mode = Mode.STARTMENU;

    private int timeCounter = 0;
    private TextArea popUp = new TextArea("");
    private boolean doorunlocked = false;
    private InfoBar infobar;
    private Object speaker;
    private Object warning;
    private Stage stage;
    private AnchorPane appRoot = new AnchorPane();
    private Map map;
    private Scene mainScene;
    private KeyPad keyPad;
    private GameOver gameOver;
    private GameMenu gameMenu;
    private InstructionScreen instructionScreen;
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private Player player;
    private ArrayList<Bullet> bulletsFired = new ArrayList<>();
    private Font font;
    private int updateCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage = primaryStage;
        initialiseFonts();
        //set Stage boundaries to visible bounds of the main screen, reinitialise everything
        this.WIDTH = (int) primaryScreenBounds.getWidth();
        this.HEIGHT = (int) primaryScreenBounds.getHeight();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        //PREPARE ROOT NODES FROM DIFFERENT CLASSES
        keyPad = new KeyPad(WIDTH, HEIGHT);
        gameOver = new GameOver(WIDTH, HEIGHT);
        gameMenu = new GameMenu(WIDTH, HEIGHT);
        instructionScreen = new InstructionScreen(WIDTH, HEIGHT);
        //SET THE SCENE
        musicStart(MUSIC.MENU);
        mainScene = new Scene(gameMenu.returnRoot(), WIDTH, HEIGHT);
        mainScene.setFill(Color.BLACK);
        mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        //RUN
        primaryStage.setScene(mainScene);
        primaryStage.show();
        runGame(primaryStage);
        gameMenu.instructions().setOnAction(instructionButtonHandler);
        gameMenu.startGame().setOnAction(startButtonHandler);
        instructionScreen.returnButton().setOnAction(returnButtonHandler);
        //prepare game
        initContent(1);
        initialiseLabels();
        appRoot.getChildren().addAll(map.mapRoot());
        for(Object object: map.level().bringtofront()){
            object.sprite.toFront();
        }

    }

    private void initialiseLabels() {
        infobar = new InfoBar();
        warning = new Object(Type.ABSTRACT, new Frame("/graphics/warn1.png"));
        warning.sprite.loadDefaultImages(new Frame("/graphics/warn1.png", 8),
                new Frame("/graphics/warn2.png", 8),
                new Frame("/graphics/warn3.png", 8),
                new Frame("/graphics/warn4.png", 8),
                new Frame("/graphics/warn5.png", 8),
                new Frame("/graphics/warn6.png", 15),
                new Frame("/graphics/warn7.png", 30),
                new Frame("/graphics/warn6.png", 15),
                new Frame("/graphics/warn5.png", 8),
                new Frame("/graphics/warn4.png", 8),
                new Frame("/graphics/warn3.png", 8));
        speaker = new Object(Type.ABSTRACT, new Frame("/graphics/helpspeaker1.png"));
        speaker.sprite.loadDefaultImages(new Frame("/graphics/helpspeaker1.png", 30),
                new Frame("/graphics/helpspeaker2.png", 8),
                new Frame("/graphics/helpspeaker3.png", 8),
                new Frame("/graphics/helpspeaker4.png", 8),
                new Frame("/graphics/helpspeaker5.png", 8));
        popUp.setWrapText(true);
        popUp.setDisable(true);
        popUp.setStyle("-fx-background-color: black");
        popUp.setFont(font);
        popUp.setMinSize(WIDTH - 490, 80);
        popUp.setMaxSize(WIDTH - 490, 80);
        popUp.setFont(font);
        addLabels();
    }

    private void addLabels() {
        for (Object barcontents : infobar.infoBarList()) {
            map.addAnimatedObjects(barcontents);
        }
        speaker.sprite.setOpacity(0);
        map.addAnimatedObjects(speaker, warning);
        map.mapRoot().getChildren().addAll(popUp, infobar.infoBarGroup());
    }

    private void initContent(int level) {
        player = new Player("Hero", WIDTH / 2, HEIGHT / 2, PLAYER_START_LIVES);
        appRoot.getChildren().clear();
        //initialise background
        Image back1 = new Image("/graphics/background1.png", true);
        BackgroundImage background = new BackgroundImage(back1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        appRoot.setBackground(new Background(background));
        map = new Map(level, WIDTH, HEIGHT); //initialises level based on level number input
        map.addPlayer(player, 70, map.level().height() - 135);
        map.mapRoot().setTranslateX(0);
        map.mapRoot().setTranslateY(-map.level().height() + HEIGHT);

    }


    /*Changes the level based on level number. New levels can be created in Grid class*/
    private void changeLevel(int level) {
        map.removePlayer(player);
        for (Object barcontents : infobar.infoBarList()) {
            map.mapRoot().getChildren().remove(barcontents.box);
            map.mapRoot().getChildren().remove(barcontents.sprite);
        }
        initContent(level);
        addLabels();
        infobar.updateHealthFill(4);
        infobar.updateWeapon(player.hasWeapon());
        appRoot.getChildren().addAll(map.mapRoot());
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
        updateCount++;
        stage.setTitle(player.getLives() + "");

        if (keyPad.isCodeCorrect()) {
            map.bigdoor().sprite.setanimation(true);
            mainScene.setRoot(appRoot);
            mode = Mode.PLATFORMGAME;
            doorOpenSound.play();
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
            if (isPressed(KeyCode.LEFT) || (isPressed(KeyCode.A))) {
                player.setFacingRight(false);
                if (player.move_X(-5, map)) map.moveScreenX(-5, player);
            } else if (isPressed(KeyCode.RIGHT) || (isPressed(KeyCode.D))) {
                player.setFacingRight(true);
                if (player.move_X(5, map)) map.moveScreenX(5, player);
            }
            if (isPressed(KeyCode.W) || (isPressed(KeyCode.UP))) {
                player.jump();
            }

            moveScreenY();
            positionLabels();
            ListenerEnemies();
            MovePlatforms();
            ListenerPlatforms();
            ListenerItemsEvent();
            ListenerPlayerLives();
            ListenerPlayerUseWeapon();
            ListenerHackingAttack ();
            ListenerAttackBots();
            ListenerBullets ();
            ListenerButtons();
            UpdateAnimatedObjects();
            ListenerTimeBeforeNewEnemyWave();
            ListenerDoodads();
            ListenerHelpPopUp();
        }
        if (mode == Mode.PLATFORMGAME || mode == Mode.GAMEOVER) {
            ListenerGameOver();
        }
    }

    private void positionLabels() {
        infobar.positionBar(-map.mapRoot().getTranslateX() + 25, -map.mapRoot().getTranslateY() + 25);
        popUp.setTranslateX(-map.mapRoot().getTranslateX() + 435);
        popUp.setTranslateY(-map.mapRoot().getTranslateY() + 25);
        popUp.toFront();
        speaker.setX(-map.mapRoot().getTranslateX() + 330);
        speaker.setY(-map.mapRoot().getTranslateY() + 25);
        speaker.box.setVisible(false);
        speaker.sprite.toFront();
        warning.setX(-map.mapRoot().getTranslateX() + 0.5*WIDTH-200);
        warning.setY(-map.mapRoot().getTranslateY() + HEIGHT-100);
        warning.box.setVisible(false);
        warning.sprite.toFront();
    }

    private void MovePlatforms() {

    }

    //updates the screen Y based on player position
    private void moveScreenY() {
        if (player.getY() > HEIGHT / 2 + 40 && player.getY() < map.level().height() - HEIGHT / 2 + 12) {
            map.mapRoot().setTranslateY(-player.getY() + HEIGHT / 2);
        }
        /*
        if (player.getY() > HEIGHT / 2 + 40 && player.getY() < map.level().height() - HEIGHT / 2 - 64) {
            map.mapRoot().setTranslateY(map.level().height() - player.getY() - HEIGHT);
        }*/
    }

    private void UpdateAnimatedObjects() {
        for (Object object : map.animatedObjects()) {
            object.update(map);
        }
    }

    /***************************************************************************
     *                              LISTENERS FOR EVENTS
     * **********************************************************************/

    private void ListenerAttackBots(){
        //for attackbots
            for(AttackBot attackBot: map.attackbots()) {
                    if(attackBot.moveLaser(player.getX(), player.getY())==true && player.isCanDie()) {
                        player.looseOneLife();
                        player.setCanDie(false);
                        playerhurtSound.play();
                        infobar.updateHealthEmpty(player.getLives());
                    }
                if (attackBot.counter() < 300) {
                    if (attackBot.getX() < player.getX()) {
                        attackBot.sprite.offset(15, 0);
                        attackBot.sprite.setdefaultanimationchoice(2);
                        attackBot.setFacingRight(true);
                    } else {
                        attackBot.sprite.offset(0, 0);
                        attackBot.sprite.setdefaultanimationchoice(1);
                        attackBot.setFacingRight(false);
                    }
                }

            }


    }

    private void ListenerDoodads() {
        if (map.getCurrentLevel() == 1 && map.inRangeOfTerminal(player.getX(), player.getY()) && isPressed(KeyCode.E)) {
            openKeyPad();
        }
        if (doorunlocked && map.inRangeOfBigDoor(player.getX(), player.getY())) {
            level = 2;
            musicStop();
            musicStart(MUSIC.BOSS);
            changeLevel(level);
        }
    }

    private void ListenerHelpPopUp() {
        boolean visible = false;
        for (HelpPopUp helper : map.helpers()) {
            if (helper.inRange(player.getX(), player.getY())) {
                popUp.setText(helper.string());
                visible = true;
            }
        }
        if (visible) {
            if (popUp.getOpacity() < 1) popUp.setOpacity(popUp.getOpacity() + 0.05);
            if (speaker.sprite.getOpacity() < 1) speaker.sprite.setOpacity(popUp.getOpacity());
        } else {
            if (popUp.getOpacity() > 0) popUp.setOpacity(popUp.getOpacity() - 0.05);
            if (speaker.sprite.getOpacity() > 0) speaker.sprite.setOpacity(popUp.getOpacity());
        }
        if (player.getLives()<2) {
            if (warning.sprite.getOpacity() < 1) warning.sprite.setOpacity(warning.sprite.getOpacity() + 0.05);
        } else {
            if (warning.sprite.getOpacity() > 0) warning.sprite.setOpacity(warning.sprite.getOpacity() - 0.05);
        }
    }


    /* ---------- PLAYER ----------- */

    private void ListenerItemsEvent() {
        for (Collectable item : map.getItems()) {
            if ((this.player.box.getBoundsInParent()).intersects(item.box.getBoundsInParent())) {
                /* pickup item */
                if (item.isAlive() && item.getItemType() != Type.HEART ) {
                    player.getLuggage().take(item);
                    map.hideEntity(item);
                    infobar.updateWeapon(true);
                    if (item.getItemType() == Type.BLOCK) {
                        miniGameKey();
                    }
                }
               else if (item.isAlive() && item.getItemType() == Type.HEART ) {
                   if(player.getLives() < 4) {
                       map.hideEntity(item);
                       item.setAlive(false);
                       player.addLife();
                       infobar.updateHealthFill(player.getLives());
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
        for (Person enemy : map.getEnemies()) {
            if (enemy.isAlive()) {
                if (player.box.getBoundsInParent().intersects(enemy.box.getBoundsInParent())) {
                    collision++;
                    // Waits that player moves out from the enemy to loose another life
                    if (player.isCanDie()) {
                        player.looseOneLife();
                        player.setCanDie(false);
                        playerhurtSound.play();
                        infobar.updateHealthEmpty(player.getLives());
                    }
                }
            }
        }
        // if player not on any enemy
        if (collision == 0) {
            player.setCanDie(true);
        }
    }

    private void ListenerGainLife() {

    }

    private void ListenerPlayerUseWeapon() {
        if (player.hasWeapon()) {
            // If player allowed to use weapon and has bullets left
            if (isPressed(KeyCode.SPACE) && player.canUseWeapon()) {
                shootOneBullet(player, player.isFacingRight());
                shootSound.play();
                player.getLuggage().getWeapon().looseOneBullet();
                player.getLuggage().getWeapon().setCanShoot(false);
            }
            // User can only shoot 1 bullet when press key (un-press & press again to shoot)
            if (!isPressed(KeyCode.SPACE)) {
                player.getLuggage().getWeapon().setCanShoot(true);
            }
        }
    }

    private void waitsSomeoneHitBullet(Bullet bullet, Person person) {

        if (person.isCanDie() && person.box.getBoundsInParent().intersects(bullet.box.getBoundsInParent()) && person.isAlive()) {
            map.removeBullet(bullet);
            person.looseOneLife();
            enemy1hurtSound.play();
            person.setCanDie(false);
        } else {
            person.setCanDie(true);
        }
        if (person.getLives() == 0 && person.isAlive()) {
            person.setAlive(false);
            enemy1dieSound.play();
            person.died();
        }
    }

    private void ListenerBullets() {
        // moves existing bullets
        for (Bullet bullet : bulletsFired) {
            bullet.move(map);
            for (Person enemy : map.getEnemies ()) {
                // if Hacking shooting, his bullets won't hurt the enemies
                if (!bullet.isPlayerShooting()) {
                    waitsSomeoneHitBullet(bullet, player);
                }
                // if player shooting, his bullets wont hurt himself
                else {
                    waitsSomeoneHitBullet(bullet, enemy);
                 //   waitsSomeoneHitBullet (bullet, map.getKing ());
                }
            }

        }
        bulletsFired.removeIf(bullet -> !bullet.isAlive());
    }

    /* ----------- ENEMIES ------------ */
    private void ListenerHackingAttack() {
        HacKing king = map.getKing();
        if (king == null) {
            return;
        }
        System.out.println ("king lives " + king.getLives ());
        king.listenerRegenerate ();
        king.move (map, player.getX (), player.getY ());

        if (king.isCanAttack ()) {
            // send new enemies
            if (king.getAttackMode () == 1 && map.getEnemies ().size () < 10) {
                map.addRandomEnemy ();
                map.addRandomEnemy ();
            }
            // send missiles in both directions
            if (king.getAttackMode () > 1) {
                shootOneBullet (king, true);
                shootOneBullet (king, false);
            }
            king.nextAttackMode ();
        }
    }

    private void shootOneBullet (Person  shooter, boolean shootRight) {
        boolean isPlayerShooter = shooter instanceof Player;
        final int BULLET_WIDTH = 20;
        final int OFFSET = 5;
        double directionX = shooter.getX() - BULLET_WIDTH - OFFSET;
        if (shootRight) {
            directionX += shooter.width + BULLET_WIDTH + 2 * OFFSET;
        }
        Bullet bullet = new Bullet(directionX, shooter.getY() + shooter.height / 4, shootRight, isPlayerShooter);
        map.mapRoot().getChildren().addAll(bullet.label(), bullet.box, bullet.sprite);
        bulletsFired.add(bullet);
    }

    private void ListenerEnemies() {
        for (Person enemy : map.getEnemies()) {
            // check if enemy died
            if (enemy.isAlive()) {
                if (isObjectOutOfBounds(enemy)) {
                    enemy.setAlive(false);
                }
                if (enemy instanceof Enemy && ((Enemy)enemy).getCanMove()) {
                    ((Enemy)enemy).giveMotion(map);
                }
            }
            if (enemy.sprite.dead()) {
                map.hideEntity(enemy);
            }
        }
        map.getEnemies().removeIf(enemy -> enemy.sprite.dead());
    }

    // Sends a new wave of enemy to the game every so often
    // New enemies are sent 1 by 1 (every second)
    // will keep going until there is more 10 enemies on the map
    private void ListenerTimeBeforeNewEnemyWave() {
        if (map.getCurrentLevel() == 1) {
            timeCounter++;
            int WAVE_SIZE = 3;
            if (map.getEnemies().size() < 10) {
                if (timeCounter >= TIME_LIMIT && timeCounter % 60 == 0) map.addRandomEnemy();
                if (timeCounter > TIME_LIMIT + WAVE_SIZE * 60) timeCounter = 0;
            }
        }
    }

    /* ----------- BUTTONS ------------ */
    private void ListenerButtons() {
        for (PlatformButton button : map.buttons()) {
            if (!(this.player.box.getBoundsInParent()).intersects(button.box.getBoundsInParent())) {
                button.setPressed(false);
            }
            if (!button.isPressed() && (this.player.box.getBoundsInParent()).intersects(button.box.getBoundsInParent()) && player.movingDown && !player.isLanded) {
                for (Platform platform : map.getLevel().platforms()) {
                    if (platform.canDisappear() && platform.isAlive() && !platform.getColour().equals(button.getColour())) {
                        platform.setAlive(false);
                        platform.setCollisionBox(0, 0, 0, 0, Color.DARKORANGE);
                        map.setButton(button.getColour());
                    } else if (platform.canDisappear() && !platform.isAlive() && platform.getColour().equals(button.getColour())) {
                        platform.restoreCollisionBox();
                        platform.setAlive(true);
                        map.setButton(button.getColour());
                    }
                }
                button.setPressed(true);
            }
        }
    }

    /* ----------- PLATFORMS ------------ */
    private void ListenerPlatforms() {
        for (Platform platform : map.getLevel().platforms()) {
            if (platform.isTimed() == true) {
                if ((this.player.box.getBoundsInParent()).intersects(platform.box.getBoundsInParent()) && player.isLanded && !platform.disappearing()) {
                    platform.setDisappear(true);
                    platform.sprite.setImage(new Image("/graphics/disappear2.png"));
                    platform.calculateUpdate(updateCount);
                }

                if (platform.disappearing() && platform.matchUpdate(updateCount)) {
                    platform.setAlive(false);
                    platform.setDisappear(false);
                    platform.setCollisionBox(0, 0, 0, 0, Color.RED);
                    platform.calculateUpdate(updateCount);
                }
                if (platform.isAlive() == false && platform.matchUpdate(updateCount)) {
                    platform.setAlive(true);
                    platform.sprite.setImage(new Image("/graphics/disappear1.png"));
                    platform.restoreCollisionBox();
                }
            }
        }
    }


    /***************************************************************************
     *                              GAME OVER
     * **********************************************************************/

    private boolean isObjectOutOfBounds(Object object) {
        if (object.getY() > map.level().height()
                || object.getX() >= map.level().width() - map.level().getOBJ_WIDTH()
                || object.getX() < 0) {
            return true;
        }
        return false;
    }

    private void ListenerGameOver() {
        if (player.getLives() > 0 && !isObjectOutOfBounds(player)) {
            return;
        }
        if (mode != mode.GAMEOVER) { //This get called when you're playing and then you DIE!
            musicStop();
            defeatSound.play();
            mode = Mode.GAMEOVER;
            player.setLives(4);
            changeLevel(level);
            mainScene.setRoot(gameOver.returnRoot());
        }
    }

  private void gameOver() {
    if (isPressed(KeyCode.SPACE)) {
        if (level == 2) { //Play song for boss level!
            musicStart(MUSIC.BOSS);
        } else {
            musicStart(MUSIC.LEVEL1);
        }
      player.getLuggage().removeWeapon();
      infobar.updateWeapon(false);
      mainScene.setRoot(appRoot);
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
            musicStop();
            musicStart(MUSIC.LEVEL1);
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
        keyPad.getQ().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getQ().getValue())));
        keyPad.getW().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getW().getValue())));
        keyPad.getE().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getE().getValue())));
        keyPad.getR().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getR().getValue())));
        keyPad.getT().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getT().getValue())));
        keyPad.getY().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getY().getValue())));
        keyPad.getU().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getU().getValue())));
        keyPad.getI().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getI().getValue())));
        keyPad.getO().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getO().getValue())));
        keyPad.getP().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getP().getValue())));
        keyPad.getA().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getA().getValue())));
        keyPad.getS().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getS().getValue())));
        keyPad.getD().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getD().getValue())));
        keyPad.getF().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getF().getValue())));
        keyPad.getG().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getG().getValue())));
        keyPad.getH().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getH().getValue())));
        keyPad.getJ().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getJ().getValue())));
        keyPad.getK().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getK().getValue())));
        keyPad.getL().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getL().getValue())));
        keyPad.getY().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getY().getValue())));
        keyPad.getX().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getX().getValue())));
        keyPad.getC().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getC().getValue())));
        keyPad.getV().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getV().getValue())));
        keyPad.getB().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getB().getValue())));
        keyPad.getN().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getN().getValue())));
        keyPad.getM().getButton().setOnAction(event -> keyPad.setDisplayText(keyPad.getDisplayText().concat(keyPad.getM().getValue())));

        keyPad.getClear().setOnAction(event -> keyPad.setDisplayText(""));
        keyPad.getExit().setOnAction(event -> {
            mainScene.setRoot(appRoot);
            mode = Mode.PLATFORMGAME;
        });
        keyPad.getEnter().setOnAction(event -> {
            if (keyPad.getDisplayText().equals("secret")) {
                mainScene.setRoot(appRoot);
                keyPad.setCodeCorrect(true);
                doorunlocked = true;
                mode = Mode.PLATFORMGAME;
            } else {
                keyPad.setDisplayText("WRONG CODE!");
            }
        });
    }

    private void initialiseFonts() {
        try {
            font = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 16);
        } catch (FileNotFoundException e) {
            font = Font.font("Verdana", 16);
        }
    }


    /***************************************************************************
     *                            MUSIC
     * **********************************************************************/

    private void musicStop() {
        gameMediaPlayer.stop();
        menuMediaPlayer.stop();
        bossMediaPlayer.stop();
    }

    private void musicStart(MUSIC music) {
        if (music == MUSIC.MENU) {
            menuMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            menuMediaPlayer.play();
        }
        else if (music == MUSIC.LEVEL1) {
            gameMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            gameMediaPlayer.setVolume(0.5);
            gameMediaPlayer.play();
        }
        else if (music == MUSIC.BOSS) {
            bossMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bossMediaPlayer.setVolume(0.5);
            bossMediaPlayer.play();
        }
    }
}

