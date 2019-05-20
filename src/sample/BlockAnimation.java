package sample;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.ArrayList;

public class BlockAnimation  {
    public void start(Group bridge, ArrayList<String> stillFrames, int gap, int start) {
        Timeline tl = new Timeline();
        tl.setCycleCount(1);



        for (String s : stillFrames) {
            final ImageView bridge1 = new ImageView(s);
            bridge1.setFitWidth(500);
            bridge1.setPreserveRatio(true);
            bridge1.setSmooth(true);
            tl.getKeyFrames().add(new KeyFrame(Duration.millis(start),  e -> {
                bridge.getChildren().setAll(bridge1);
            }
            ));
            start+=gap;
        }
        tl.play();
    }
}
