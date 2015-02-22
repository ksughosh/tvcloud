package player;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MoviePlayer extends Application {

    // this is the difference between stage.getHeight/Width() and scene.getHeight/Width().
    // the reason is that sometimes only the stage's size is updated, while the scene's size is still the old value, so we can't use it.
    // thus we use stage.getWidth/Height() with these offsets... they might actually be different for different OSs etc..
    private static double fuckingHeightOffset = -38.0;
    private static double fuckingWidthOffset = -17.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Movie Player");
        Group root = new Group();
        final SendMovieInfo sendMovieInfo = new SendMovieInfo();
        String videoPath = getHostServices().getDocumentBase()+"video/ExampleTrailer.mp4";
        Media media = new Media(videoPath);
        final MediaPlayer player = new MediaPlayer(media);
        final MediaView view = new MediaView(player);
        final Timeline slideIn = new Timeline();
        final Timeline slideOut = new Timeline();

        root.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                slideOut.play();
            }
        });
        root.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                slideIn.play();
            }
        });
        final VBox vbox = new VBox();
        final Slider slider = new Slider();
        vbox.getChildren().add(slider);

        final HBox hbox = new HBox(2);
        final int bands = player.getAudioSpectrumNumBands();
        final Rectangle[] rects = new Rectangle[bands];
        final DoubleProperty width = view.fitWidthProperty();
        final DoubleProperty height = view.fitHeightProperty();

        for (int i=0; i<rects.length; i++) {
            rects[i] = new Rectangle();
            rects[i].setFill(Color.GREEN);
            hbox.getChildren().add(rects[i]);
        }
        //vbox.getChildren().add(hbox);

        root.getChildren().add(view);
        root.getChildren().add(vbox);

        final Scene scene = new Scene(root, 1280, 760, Color.BLACK);
        stage.setScene(scene);
        view.setFitHeight(width.doubleValue());
        view.setFitHeight(height.doubleValue());
        view.setPreserveRatio(false);
        width.bind(Bindings.selectDouble(view.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(view.sceneProperty(), "height"));

        stage.show();
        player.play();
        player.setOnReady(new Runnable() {

            public void run() {

                hbox.setMinWidth(width.get());
                int bandWidth = (int) (width.get()/rects.length);
                for (Rectangle r:rects) {
                    r.setWidth(bandWidth);
                    r.setHeight(2);
                }

                stage.setMinWidth(width.get());
                stage.setMinHeight(height.get());
                vbox.setMinSize(width.get(), 10);
                vbox.setMaxSize(width.get(), 10);
                vbox.relocate(0, height.get() - 20);
                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());

                slideOut.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vbox.opacityProperty(), 0.9)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vbox.opacityProperty(), 0.0)
                        )
                );
                slideIn.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vbox.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vbox.opacityProperty(), 0.9)
                        )
                );
            }
        });

        slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                player.seek(Duration.seconds(slider.getValue()));
            }
        });

        player.currentTimeProperty().addListener(new javafx.beans.value.ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration current) {
                slider.setValue(current.toSeconds());
                        try {
                            sendMovieInfo.doInBackground(1,(int) current.toSeconds());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
            }
        });

        player.setAudioSpectrumListener(new AudioSpectrumListener() {
            public void spectrumDataUpdate(double v, double v1, float[] mags, float[] floats1) {
                for (int i=0; i<rects.length; i++) {
                    double h = mags[i]+60;
                    if (h > 2) {
                        rects[i].setHeight(h);
                    }
                }
            }
        });

        stage.widthProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                vbox.setMinWidth(stage.getWidth() + fuckingWidthOffset);
                vbox.setMaxWidth(stage.getWidth() + fuckingWidthOffset);
            }
        });

        stage.heightProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                vbox.relocate(0.0, stage.getHeight() + fuckingHeightOffset - 20.0);
            }
        });
    }
}