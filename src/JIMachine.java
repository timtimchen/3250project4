import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class JIMachine extends Application {
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

    ImageView imageView = new ImageView(new Image("car.jpg"));
    Pane paneForImage = new Pane();
    paneForImage.getChildren().add(imageView);
    pane.setCenter(paneForImage);

    quitButton.setOnAction(e -> {
      Stage stage = (Stage) quitButton.getScene().getWindow();
      stage.close();
    });

    return pane;
  }

  @Override
  public void start(Stage primaryStage) {
    Scene scene = new Scene(getPane(), 800, 600);
    primaryStage.setTitle("JIMachine");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
