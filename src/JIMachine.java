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

/**
 * A class of application to display and manipulate image files.
 */
public class JIMachine extends Application {
  /** The original proportion of display. */
  private final double originalproportion = 1.0;
  /** Increment of proportion of each steps. */
  private final double stepProportion = 0.25;
  /** Pixels between buttons. */
  private final int sapceBetweenButtons = 20;

  /** the display proportion of currently opened image. */
  private double displayProportion = originalproportion;
  /** the width of currently opened image. */
  private double imageWidth = 0.0;
  /** the height of currently opened image. */
  private double imageHeight = 0.0;

  /**
   * Getter function of image proportion setting.
   * @return the image display proportion
   */
  public double getProportion() {
    return displayProportion;
  }

  /**
   * Set the original display proportion of the image.
   */
  public void setOriginalProportion() {
    displayProportion = originalproportion;
  }

  /**
   * Increase the display proportion of the image.
   */
  public void increaseProportion() {
    displayProportion *= (1 + stepProportion);
  }

  /**
   * Decrease the display proportion of the image.
   */
  public void decreaseProportion() {
    displayProportion *= (1 - stepProportion);
  }

  /**
   * A borderPane to organize the basic elements of the JIMachine.
   * @return a set-up pane
   */
  protected BorderPane getPane() {
    HBox paneForButtons = new HBox(sapceBetweenButtons);
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
      increaseProportion();
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
      decreaseProportion();
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

  /**
   * An start function to start the application.
   * @param primaryStage the main stage of the application
   */
  @Override
  public void start(final Stage primaryStage) {
    Scene scene = new Scene(getPane());
    primaryStage.setTitle("JIMachine");
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();
  }
}
