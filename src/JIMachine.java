import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.MalformedURLException;

public class JIMachine extends Application {
  private final double ORIGINAL_PROPORTION = 1.0;
  private final double STEP_PROPORTION = 0.25;

  private double displayProportion = ORIGINAL_PROPORTION;
  private double imageWidth = 0.0;
  private double imageHeight = 0.0;

  public double getProportion() {
    return displayProportion;
  }

  public void setOriginalProportion() {
    displayProportion = ORIGINAL_PROPORTION;
  }

  public void increaseProportion() {
    displayProportion *= (1 + STEP_PROPORTION);
  }

  public void decreaseProportion() {
    displayProportion *= (1 - STEP_PROPORTION);
  }

  protected BorderPane getPane() {
    HBox paneForButtons = new HBox(20);
    Button openFileButton = new Button("Open File");
    Button zoomInButton = new Button("Zoom In");
    Button originalSizeButton = new Button("100% Size");
    Button zoomOutButton = new Button("Zoom Out");
    Button quitButton = new Button("Quit");

    paneForButtons.getChildren().
      addAll(openFileButton, zoomInButton, originalSizeButton,
        zoomOutButton, quitButton);
    paneForButtons.setAlignment(Pos.CENTER);

    BorderPane pane = new BorderPane();
    pane.setTop(paneForButtons);

    ImageView imageView = new ImageView();
    imageView.setPreserveRatio(false);
    HBox pictureRegion = new HBox();
    pictureRegion.getChildren().add(imageView);
    pictureRegion.setAlignment(Pos.CENTER);
    pane.setCenter(pictureRegion);

    openFileButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Resource File");
      fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files",
          "*.png", "*.jpg", "*.gif"),
        new FileChooser.ExtensionFilter("All Files",
          "*.*"));
      File selectedFile = fileChooser
        .showOpenDialog(openFileButton.getScene().getWindow());
      if (selectedFile != null) {
        try {
          Image image =
            new Image(selectedFile.toURI().toURL().toExternalForm());
          imageView.setImage(image);
          imageWidth = image.getWidth();
          imageHeight = image.getHeight();
          setOriginalProportion();
          imageView.setFitWidth(imageWidth * getProportion());
          imageView.setFitHeight(imageHeight * getProportion());
          Stage stage = (Stage) openFileButton.getScene().getWindow();
          stage.sizeToScene();
        } catch (MalformedURLException e1) {
          System.out.println(e1);
        }
      }
    });

    zoomInButton.setOnAction(e -> {
      decreaseProportion();
      imageView.setFitWidth(imageWidth * getProportion());
      imageView.setFitHeight(imageHeight * getProportion());
      Stage stage = (Stage) zoomInButton.getScene().getWindow();
      stage.sizeToScene();
    });

    originalSizeButton.setOnAction(e -> {
      setOriginalProportion();
      imageView.setFitWidth(imageWidth * getProportion());
      imageView.setFitHeight(imageHeight * getProportion());
      Stage stage = (Stage) originalSizeButton.getScene().getWindow();
      stage.sizeToScene();
    });

    zoomOutButton.setOnAction(e -> {
      increaseProportion();
      imageView.setFitWidth(imageWidth * getProportion());
      imageView.setFitHeight(imageHeight * getProportion());
      Stage stage = (Stage) zoomOutButton.getScene().getWindow();
      stage.sizeToScene();
    });

    quitButton.setOnAction(e -> {
      Stage stage = (Stage) quitButton.getScene().getWindow();
      stage.close();
    });

    return pane;
  }

  @Override
  public void start(Stage primaryStage) {
    Scene scene = new Scene(getPane());
    primaryStage.setTitle("JIMachine");
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();
  }
}
