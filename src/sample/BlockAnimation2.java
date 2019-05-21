package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class BlockAnimation2 {
    public void start(Group bridge) {
        final ImageView bridge1 = new ImageView("graphics/minigameimages/animation/message1.png");
        bridge1.setFitWidth(400);
        bridge1.setPreserveRatio(true);
        bridge1.setSmooth(true);
        final ImageView bridge2 = new ImageView("graphics/minigameimages/animation/message2.png");
        bridge2.setFitWidth(400);
        bridge2.setPreserveRatio(true);
        bridge2.setSmooth(true);
        final ImageView bridge3 = new ImageView("graphics/minigameimages/animation/message3.png");
        bridge3.setFitWidth(400);
        bridge3.setPreserveRatio(true);
        bridge3.setSmooth(true);
        final ImageView bridge4 = new ImageView("graphics/minigameimages/animation/message4.png");
        bridge4.setFitWidth(400);
        bridge4.setPreserveRatio(true);
        bridge4.setSmooth(true);
        final ImageView bridge5 = new ImageView("graphics/minigameimages/animation/message5.png");
        bridge5.setFitWidth(400);
        bridge5.setPreserveRatio(true);
        bridge5.setSmooth(true);

        Timeline tl = new Timeline();
        tl.setCycleCount(1);

        tl.getKeyFrames().add(new KeyFrame(Duration.millis(100), e -> {
            bridge.getChildren().setAll(bridge1);
        }
        ));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(900), e -> {
            bridge.getChildren().setAll(bridge2);
        }
        ));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(1100), (e) -> {
            bridge.getChildren().setAll(bridge3);
        }
        ));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(1300), (e) -> {
            bridge.getChildren().setAll(bridge4);
        }
        ));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(1500), (e) -> {
            bridge.getChildren().setAll(bridge5);
        }
        ));
        tl.play();
    }
}
