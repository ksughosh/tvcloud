package player;

import de.tvcrowd.lib.dto.TagStormDto;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;

public class MoviePlayer extends Application {

    // this is the difference between stage.getHeight/Width() and scene.getHeight/Width().
    // the reason is that sometimes only the stage's size is updated, while the scene's size is still the old value, so we can't use it.
    // thus we use stage.getWidth/Height() with these offsets... they might actually be different for different OSs etc..
    private static double fuckingHeightOffset = -38.0;
    private static double fuckingWidthOffset = -17.0;
    private static int movieId;
    private final List<Rectangle> rects = new ArrayList<Rectangle>();
    private final PeriodSpinner periodSpinner = new PeriodSpinner(5, 300, 10, 5);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Movie Player");
        Group root = new Group();
        String videoPath = getHostServices().getDocumentBase() + "video/ExampleTrailer.mp4";
        if (videoPath.contains("ExampleTrailer")) {
            movieId = 2;
        }
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
        vbox.getChildren().add(hbox);

        final DoubleProperty width = view.fitWidthProperty();
        final DoubleProperty height = view.fitHeightProperty();

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

                int maxValue = (int) (player.getTotalDuration().toSeconds() / 2);
                periodSpinner.setMaxValue(maxValue + 5 - maxValue % 5);

                showTagStorm((int) player.getTotalDuration().toSeconds(), width.get(), hbox, periodSpinner.getValue());

                stage.setMinWidth(width.get());
                stage.setMinHeight(height.get());
                vbox.setMinSize(width.get(), 10);
                vbox.setMaxSize(width.get(), 10);
                vbox.relocate(0, height.get() - 30);
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

            int lastSecond = -1;

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration current) {
                slider.setValue(current.toSeconds());
                if (lastSecond != (int) current.toSeconds()) {
                    SendMovieInfo sendMovieInfo = new SendMovieInfo(movieId, (int) current.toSeconds());
                    Thread thread = new Thread(sendMovieInfo);
                    thread.start();
                    lastSecond = (int) current.toSeconds();
                }
            }
        });

        stage.widthProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                vbox.setMinWidth(stage.getWidth() + fuckingWidthOffset);
                vbox.setMaxWidth(stage.getWidth() + fuckingWidthOffset);
                showTagStorm((int) player.getTotalDuration().toSeconds(), width.get(), hbox, periodSpinner.getValue());
            }
        });

        stage.heightProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                vbox.relocate(0.0, stage.getHeight() + fuckingHeightOffset - 30.0);
            }
        });
    }

    private void showTagStorm(final int seconds, final double width, HBox hbox, int period) {
        for (Rectangle r : rects) {
            hbox.getChildren().remove(r);
        }
        rects.clear();

        double correctWidth = width + fuckingWidthOffset;
        int count = (int) Math.ceil(seconds / (double) period);
        double bandWidth = (correctWidth - 2 * (count + 2)) / count;
        for (int i = 0; i < count; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(bandWidth);
            rectangle.setFill(Color.AZURE);
            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        periodSpinner.dec();
                    } else {
                        periodSpinner.inc();
                    }
                    showTagStorm(seconds, width, hbox, periodSpinner.getValue());
                }
            });
            hbox.getChildren().add(rectangle);
            rects.add(rectangle);
        }

        try {
            GetTagStorm getTagStorm = new GetTagStorm(movieId, period);
            List<TagStormDto> tagStorms = getTagStorm.doInBackground();
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (TagStormDto tagStorm : tagStorms) {
                if (tagStorm.getTagCount() > max) {
                    max = tagStorm.getTagCount();
                }
                if (tagStorm.getTagCount() < min) {
                    min = tagStorm.getTagCount();
                }
            }

            double stepHeight = 13.0 / (max + 1);
            if (max < min) {
                stepHeight = 1;
            }

            for (Rectangle r : rects) {
                r.setHeight(stepHeight);
            }

            for (TagStormDto tagStorm : tagStorms) {
                if (tagStorm.getTagCount() / (double) max >= 0.90) {
                    rects.get(tagStorm.getPeriod()).setFill(Color.INDIANRED);
                } else if (tagStorm.getTagCount() / (double) max >= 0.75) {
                    rects.get(tagStorm.getPeriod()).setFill(Color.YELLOWGREEN);
                } else if (tagStorm.getTagCount() / (double) max >= 0.60) {
                    rects.get(tagStorm.getPeriod()).setFill(Color.BLUEVIOLET);
                }
                rects.get(tagStorm.getPeriod()).setHeight(tagStorm.getTagCount() * stepHeight + stepHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}