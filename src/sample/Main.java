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

import static sample.Mode.GAMEOVER;

public class Main extends Application {
    //music
    private String menuSong = "src/sound/menuSong.mp3";
    private javafx.scene.media.Media menuMedia = new javafx.scene.media.Media(new File(menuSong).toURI().toString());
    private MediaPlayer menuMediaPlayer = new MediaPlayer(menuMedia);

    private String Level1Song = "src/sound/Level1.mp3";
    private javafx.scene.media.Media level1Media = new javafx.scene.media.Media(new File(Level1Song).toURI().toString());
    private MediaPlayer level1MediaPlayer = new MediaPlayer(level1Media);

    private String Level3Song = "src/sound/Level3.mp3";
    private javafx.scene.media.Media level3Media = new javafx.scene.media.Media(new File(Level3Song).toURI().toString());
    private MediaPlayer level3MediaPlayer = new MediaPlayer(level3Media);

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
    private AudioClip buttonClickSound = new AudioClip(Paths.get("src/sound/buttonpress.wav").toUri().toString());
    private AudioClip tunnelstepSound = new AudioClip(Paths.get("src/sound/tunnelstep.wav").toUri().toString());
    private AudioClip terminalopenSound = new AudioClip(Paths.get("src/sound/terminalopen.wav").toUri().toString());
    private AudioClip terminalcloseSound = new AudioClip(Paths.get("src/sound/terminalclose.wav").toUri().toString());
    private AudioClip wrongcodeSound = new AudioClip(Paths.get("src/sound/wrongcode.wav").toUri().toString());
    private AudioClip pickupSound = new AudioClip(Paths.get("src/sound/pickup.wav").toUri().toString());
    private AudioClip disappearSound = new AudioClip(Paths.get("src/sound/disappear.wav").toUri().toString());

    //global variables
    private int WIDTH, HEIGHT;
    private int level = 1;
    private Mode mode = Mode.STARTMENU;

    private Stage stage;
    private int timeCounter = 0;
    private TextArea popUp = new TextArea("");
    private InfoBar infobar;
    private Object speaker;
    private Object warning;
    private AnchorPane appRoot = new AnchorPane();
    private Map map;
    private Scene mainScene;
    private KeyPad keyPad;
    private GameOver gameOver;
    private GameMenu gameMenu;
    private BossHealthBar bossHealthBar;
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
        this.stage = primaryStage;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        initialiseFonts();
        //set Stage boundaries to visible bounds of the main screen, reinitialise everything
        this.WIDTH = (int) primaryScreenBounds.getWidth();
        this.HEIGHT = (int) primaryScreenBounds.getHeight();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        //PREPARE ROOT NODES FROM DIFFERENT CLASSES
        keyPad = new KeyPad(WIDTH, HEIGHT);
        gameOver = new GameOver(WIDTH, HEIGHT);
        gameMenu = new GameMenu(WIDTH, HEIGHT);
        instructionScreen = new InstructionScreen(WIDTH, HEIGHT);
        //SET THE SCENE
        mainScene = new Scene(gameMenu.returnRoot(), WIDTH, HEIGHT);
        mainScene.setFill(Color.LIGHTSTEELBLUE);
        mainScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        //RUN
        primaryStage.setScene(mainScene);
        primaryStage.show();
        //stage.setFullScreen (true);
        runGame(primaryStage);
        gameMenu.instructions().setOnAction(instructionButtonHandler);
        gameMenu.startGame().setOnAction(startButtonHandler);
        instructionScreen.returnButton().setOnAction(returnButtonHandler);
        //prepare game
        initContent(1);
        initialiseLabels();
        musicStart();
        appRoot.getChildren().addAll(map.mapRoot());
        for (Object object : map.level().bringtofront()) {
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
        if (map.levelHasBoss()) {
            bossHealthBar = new BossHealthBar(map.getKing().getLives());
            map.mapRoot().getChildren().add(bossHealthBar.group());
        }
    }

    private void initContent(int level) {
        int PLAYER_START_LIVES = 4;
        player = new Player(WIDTH / 2, HEIGHT / 2, PLAYER_START_LIVES);
        appRoot.getChildren().clear();
        //initialise background
        Image back1 = new Image("/graphics/background1.png", true);
        BackgroundImage background = new BackgroundImage(back1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        appRoot.setBackground(new Background(background));
        map = new Map(level, WIDTH); //initialises level based on level number input
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
        musicStart();
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
        if (keyPad.isCodeCorrect()) {
            mainScene.setRoot(appRoot);
            mode = Mode.PLATFORMGAME;
            keyPad.setCodeCorrect(false);
            doorOpenSound.play();
            map.bigdoor().sprite.setanimation(true);

        }
        if (mode == GAMEOVER) {
            musicStop();
            gameOver();
        }
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
            if (isPressed(KeyCode.R)) {
                level++;
                changeLevel(level);
            }
            moveScreenY();
            positionLabels();
            ListenerEnemies();
            MovePlatforms();
            ListenerPlatforms();
            ListenerItemsEvent();
            ListenerPlayerLives();
            ListenerPlayerUseWeapon();
            ListenerHackingAttack();
            ListenerAttackBots();
            ListenerTower ();
            ListenerBullets();
            ListenerButtons();
            UpdateAnimatedObjects();
            ListenerTimeBeforeNewEnemyWave();
            ListenerDoodads();
            ListenerHelpPopUp();
        }
        if (mode == Mode.PLATFORMGAME || mode == GAMEOVER) {
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
        warning.setX(-map.mapRoot().getTranslateX() + 0.5 * WIDTH - 200);
        warning.setY(-map.mapRoot().getTranslateY() + HEIGHT - 100);
        warning.box.setVisible(false);
        warning.sprite.toFront();
        if (map.levelHasBoss()) {
            bossHealthBar.group().setTranslateX(-map.mapRoot().getTranslateX() + WIDTH - 155);
            bossHealthBar.group().setTranslateY(-map.mapRoot().getTranslateY() + 106);
            bossHealthBar.group().toFront();
            bossHealthBar.updatePosition(0, 0);
            bossHealthBar.updateAppearance(map.getKing().getLives());
            stage.setTitle(map.getKing().getLives() + "");
        }
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

    private void ListenerTower () {
        for (Tower tower : map.getTowers ()) {
            if (tower.isCanShoot ()) {
                shootOneBullet (tower, tower.facingRight);
            }
        }
    }

    private void ListenerAttackBots() {
        //for attackbots
        for (AttackBot attackBot : map.attackbots()) {
            if (attackBot.moveLaser(player.getX(), player.getY()) && player.isCanDie() && !player.getSpin()) {
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
            attackBot.laser().toFront();

        }


    }

    private void ListenerDoodads() {
        if (map.inRangeOfTerminal(player.getX(), player.getY()) && isPressed(KeyCode.E)) {
            terminalopenSound.play();
            openKeyPad();
        }
        if (map.inRangeOfBigDoor(player.getX(), player.getY()) && map.bigdoor().sprite().isAnimationActive()) {
            tunnelstepSound.play();
            level++;
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
        if (player.getLives() < 2) {
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
                if (item.isAlive() && item.getItemType() != Type.HEART) {
                    pickupSound.play();
                    player.getLuggage().take(item);
                    map.hideEntity(item);
                    infobar.updateWeapon(true);
                    if (item.getItemType() == Type.BLOCK) {
                        miniGameKey();
                    }
                } else if (item.isAlive() && item.getItemType() == Type.HEART) {
                    if (player.getLives() < 4) {
                        pickupSound.play();
                        map.hideEntity(item);
                        item.setAlive(false);
                        player.addLife();
                        infobar.updateHealthFill(player.getLives());
                    }
                }
            }
        }
        /* drop item  */
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
                    if (player.isCanDie() && !player.getSpin()) {
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
                player.getLuggage().getWeapon().doesntShoot();
            }
        }
    }

    private void waitsSomeoneHitBullet(Bullet bullet, Person person) {
        if (!bullet.isAlive()) return;
        if (person.isCanDie() && person.box.getBoundsInParent().intersects(bullet.box.getBoundsInParent()) && person.isAlive()) {
            map.removeBullet(bullet);
            person.looseOneLife();
            enemy1hurtSound.play();
            person.setCanDie(false);
            if (person.type == Type.PLAYER) infobar.updateHealthEmpty(player.getLives());
        } else {
            person.setCanDie(true);
        }
        if (person.getLives() == 0 && person.isAlive()) {
            map.removeBullet(bullet);
            person.setAlive(false);
            enemy1dieSound.play();
            person.died();
            if (person instanceof HacKing) {
                doorOpenSound.play();
                map.bigdoor().sprite.setanimation(true);
            }
        }
    }

    private void ListenerBullets() {
        // moves existing bullets
        for (Bullet bullet : bulletsFired) {
            bullet.move(map);
            // if Hacking shooting, his bullets won't hurt the enemies
            if (!bullet.isPlayerShooting()) {
                waitsSomeoneHitBullet(bullet, player);
            }
            // if player shooting, his bullets wont hurt himself
            else {
                for (Person enemy : map.getEnemies ()) {
                    waitsSomeoneHitBullet (bullet, enemy);
                }
            }
        }
        bulletsFired.removeIf(bullet -> !bullet.isAlive());
    }

    /* ----------- ENEMIES ------------ */
    private void ListenerHackingAttack() {
        if (!map.levelHasBoss()) return;
        HacKing king = map.getKing();
        if (king.sprite().dead()) map.hideEntity(king);
        if (king == null || !king.isAlive()) {
            return;
        }
        king.listenerDefense();
        king.move(map, player.getX(), player.getY());

        if (king.isCanAttack()) {
            // send new enemies
            if (king.getAttackMode() == 1 && map.getEnemies().size() < 5) {
                map.addRandomEnemy();
                map.addRandomEnemy();
            }
            // send missiles in both directions
            if (king.getAttackMode() > 1) {
                shootOneBullet(king, true);
                shootOneBullet(king, false);
            }
            king.nextAttackMode();
        }
    }

    private void shootOneBullet(Person shooter, boolean shootRight) {
        boolean isPlayerShooter = shooter instanceof Player;
        final int BULLET_WIDTH = 20;
        final int OFFSET = 15;
        double directionX = shooter.getX() - BULLET_WIDTH - OFFSET;
        if (shootRight) {
            directionX += shooter.width + BULLET_WIDTH + 2 * OFFSET;
        }
        Bullet bullet = new Bullet(directionX, shooter.getY() + shooter.height / 2, shootRight, isPlayerShooter);
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
                if (enemy instanceof Enemy && ((Enemy) enemy).getCanMove()) {
                    ((Enemy) enemy).giveMotion(map);
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
        if (map.getCurrentLevel() == 2) {
            timeCounter++;
            int WAVE_SIZE = 3;
            if (map.getEnemies().size() < 5) {
                // 20 seconds
                int TIME_LIMIT = 60 * 20;
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
                buttonClickSound.play();
            }
        }
    }

    /* ----------- PLATFORMS ------------ */
    private void ListenerPlatforms() {
        for (Platform platform : map.getLevel().platforms()) {
            if (platform.isTimed()) {
                if ((this.player.box.getBoundsInParent()).intersects(platform.box.getBoundsInParent()) && player.isLanded && !platform.disappearing()) {
                    platform.setDisappear(true);
                    platform.sprite.setImage(new Image("/graphics/disappear2.png"));
                    platform.calculateUpdate(updateCount);
                }

                if (platform.disappearing() && platform.matchUpdate(updateCount)) {
                    platform.setAlive(false);
                    platform.setDisappear(false);
                    disappearSound.play();
                    platform.setCollisionBox(0, 0, 0, 0, Color.RED);
                    platform.calculateUpdate(updateCount);
                }
                if (!platform.isAlive() && platform.matchUpdate(updateCount)) {
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
        return object.getY() > map.level().height()
                || object.getX() >= map.level().width() - map.level().getOBJ_WIDTH()
                || object.getX() < 0;
    }

    private void ListenerGameOver() {
        if (player.getLives() > 0 && !isObjectOutOfBounds(player)) {
            return;
        }
        if (mode != GAMEOVER) { //This get called when you're playing and then you DIE!
            musicStop();
            defeatSound.play();
            mode = GAMEOVER;
            player.setLives(4);
            changeLevel(level);
            mainScene.setRoot(gameOver.returnRoot());
        }
    }

    private void gameOver() {
        if (isPressed(KeyCode.SPACE)) {
            musicStart();
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


    private EventHandler<ActionEvent> startButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            mainScene.setFill(Color.BLACK);
            mode = Mode.PLATFORMGAME;
            mainScene.setRoot(appRoot);
            musicStart();
            timeCounter = 0;
        }
    };

    private EventHandler<ActionEvent> instructionButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            mainScene.setRoot(instructionScreen.returnRoot());
        }
    };

    private EventHandler<ActionEvent> returnButtonHandler = new EventHandler<ActionEvent>() {
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
            terminalcloseSound.play();
        });
        keyPad.getEnter().setOnAction(event -> {
            if (keyPad.getDisplayText().equals("secret")) {
                mainScene.setRoot(appRoot);
                keyPad.setCodeCorrect(true);
                mode = Mode.PLATFORMGAME;
                terminalcloseSound.play();
            } else {
                keyPad.setDisplayText("WRONG CODE!");
                wrongcodeSound.play();
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
     *                            Music
     * **********************************************************************/

    private void musicStop() {
        level1MediaPlayer.stop();
        level3MediaPlayer.stop();
        menuMediaPlayer.stop();
        bossMediaPlayer.stop();
    }

    private void musicStart() {
        musicStop();
        if (mode == Mode.STARTMENU) {
            menuMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            menuMediaPlayer.setVolume(1);
            menuMediaPlayer.play();
        } else if (level == 1 || level == 2) {
            level1MediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            level1MediaPlayer.setVolume(0.5);
            level1MediaPlayer.play();
        } else if (level == 3) {
            bossMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bossMediaPlayer.setVolume(0.7);
            bossMediaPlayer.play();
        } else if (level == 4) {
            level3MediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            level3MediaPlayer.setVolume(0.7);
            level3MediaPlayer.play();
        }
    }
}

