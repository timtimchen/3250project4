import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

/**
 * A class of application to display and manipulate image files.
 */
public class JIMachine extends Application {
  /** The original proportion of display. */
  private static final double ORIGINAL_PROPORTION = 1.0;
  /** Increment of proportion of each steps. */
  private static final double STEP_PROPORTION = 0.25;
  /** Pixels between buttons. */
  private static final int SPACE_BETWEEN_BUTTONS = 20;
  /** Pixels between buttons. */
  private static final int SPACE_BETWEEN_THUMBNAIL = 200;
  /** The original width of scene in pixels. */
  private static final int ORIGINAL_WIDTH = 1000;
  /** The original height of scene in pixels.*/
  private static final int ORIGINAL_HEIGHT = 800;
  /** Total images in Thumbnail View. */
  private static final int THUMBNAIL_TOTAL = 4;
  /** The original width of thumbnail in pixels. */
  private static final int THUMBNAIL_WIDTH = 100;
  /** The original height of scene in pixels.*/
  private static final int THUMBNAIL_HEIGHT = 75;
  private static final String EXTENSIONS = ".jpg";
  static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
    @Override
    public boolean accept(final File dir, final String name) {
      if (name.endsWith(EXTENSIONS)) {
        return (true);
      } else {
        return (false);
      }
    }
  };

  /** the display proportion of currently opened image. */
  private double displayProportion = ORIGINAL_PROPORTION;
  /** the width of currently opened image. */
  private double imageWidth = 0.0;
  /** the height of currently opened image. */
  private double imageHeight = 0.0;
  ArrayList<Image> imageList = new ArrayList<>();
  ArrayList<String> imageCaptions = new ArrayList<>();
  private int currentImageIndex = 0;
  boolean isThumbnailView = false;
  boolean isCaptionChanged = false;
  String currentDirectory = null;
  String albumSavePath = null;

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
    displayProportion = ORIGINAL_PROPORTION;
  }

  /**
   * Increase the display proportion of the image.
   */
  public void increaseProportion() {
    displayProportion *= (1 + STEP_PROPORTION);
  }

  /**
   * Decrease the display proportion of the image.
   */
  public void decreaseProportion() {
    displayProportion *= (1 - STEP_PROPORTION);
  }

  /**
   * Returns all jpg images from a directory in an array.
   *
   * @param directory  the directory to start with
   * @param currentFile the current select file name
   */
  private void getAllImages(String directory, String currentFile) {
    try {
      imageList.clear();
      if (!isCaptionChanged) {
        imageCaptions.clear();
      }
      File dir = new File(directory);
      if (dir.isDirectory()) {
        for (File file : dir.listFiles(IMAGE_FILTER)) {
          if (file.toURI().getPath().equals(currentFile)) {
            currentImageIndex = imageList.size();
          }
          imageList.add(new Image(new FileInputStream(file)));
          if (!isCaptionChanged) {
            imageCaptions.add(file.getName());
          }
        }
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private ImageView createThumbnailView(final int imageNumber) {
    ImageView thumbnailView = new ImageView();
    thumbnailView.setFitHeight(THUMBNAIL_HEIGHT);
    thumbnailView.setFitWidth(THUMBNAIL_WIDTH);
    if (imageNumber <= imageList.size()) {
      thumbnailView.setImage(imageList.get((imageNumber + currentImageIndex - 1) % imageList.size()));
    }
    return thumbnailView;
  }

  private Label createThumbnailLabel(final int imageNumber) {
    Label thumbnailLabel = new Label();
    if (imageNumber <= imageList.size()) {
      thumbnailLabel = new Label((imageCaptions.get((imageNumber + currentImageIndex - 1) % imageList.size())));
    }
    return thumbnailLabel;
  }

  private TilePane thumbnailTilePane(BorderPane pane, ImageView imageView, VBox singlePictureRegion) {
    TilePane tilePane = new TilePane();
    Label thumbLabel1 = createThumbnailLabel(1);
    thumbLabel1.setMaxWidth(150);
    ImageView thumbView1 = createThumbnailView(1);
    if (imageList.size() >= 1) {
      thumbView1.setOnMouseClicked(mouseEvent -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          if (mouseEvent.getClickCount() == 2) {
            pane.getChildren().remove(tilePane);
            imageView.setImage(imageList.get(currentImageIndex));
            imageWidth = imageList.get(currentImageIndex).getWidth();
            imageHeight = imageList.get(currentImageIndex).getHeight();
            setOriginalProportion();
            imageView.setFitWidth(imageWidth * getProportion());
            imageView.setFitHeight(imageHeight * getProportion());
            imageView.setPreserveRatio(false);
            if (currentImageIndex < imageCaptions.size()) {
              Label newLabel = new Label(imageCaptions.get(currentImageIndex));
              singlePictureRegion.getChildren().remove(1);
              singlePictureRegion.getChildren().add(newLabel);
            }
            pane.setCenter(singlePictureRegion);
            isThumbnailView = false;
          }
        }
      });
    }
    VBox imageBox1 = new VBox();
    imageBox1.getChildren().addAll(thumbView1, thumbLabel1);
    imageBox1.setAlignment(Pos.CENTER);
    Label thumbLabel2 = createThumbnailLabel(2);
    thumbLabel2.setMaxWidth(150);
    ImageView thumbView2 = createThumbnailView(2);
    if (imageList.size() >= 2) {
      thumbView2.setOnMouseClicked(mouseEvent -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          if (mouseEvent.getClickCount() == 2) {
            pane.getChildren().remove(tilePane);
            currentImageIndex = (currentImageIndex + 1) % imageList.size();
            imageView.setImage(imageList.get(currentImageIndex));
            imageWidth = imageList.get(currentImageIndex).getWidth();
            imageHeight = imageList.get(currentImageIndex).getHeight();
            setOriginalProportion();
            imageView.setFitWidth(imageWidth * getProportion());
            imageView.setFitHeight(imageHeight * getProportion());
            imageView.setPreserveRatio(false);
            if (currentImageIndex < imageCaptions.size()) {
              Label newLabel = new Label(imageCaptions.get(currentImageIndex));
              singlePictureRegion.getChildren().remove(1);
              singlePictureRegion.getChildren().add(newLabel);
            }
            pane.setCenter(singlePictureRegion);
            isThumbnailView = false;
          }
        }
      });
    }
    VBox imageBox2 = new VBox();
    imageBox2.getChildren().addAll(thumbView2, thumbLabel2);
    imageBox2.setAlignment(Pos.CENTER);
    Label thumbLabel3 = createThumbnailLabel(3);
    ImageView thumbView3 = createThumbnailView(3);
    thumbLabel3.setMaxWidth(150);
    if (imageList.size() >= 3) {
      thumbView3.setOnMouseClicked(mouseEvent -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          if (mouseEvent.getClickCount() == 2) {
            pane.getChildren().remove(tilePane);
            currentImageIndex = (currentImageIndex + 2) % imageList.size();
            imageView.setImage(imageList.get(currentImageIndex));
            imageWidth = imageList.get(currentImageIndex).getWidth();
            imageHeight = imageList.get(currentImageIndex).getHeight();
            setOriginalProportion();
            imageView.setFitWidth(imageWidth * getProportion());
            imageView.setFitHeight(imageHeight * getProportion());
            imageView.setPreserveRatio(false);
            if (currentImageIndex < imageCaptions.size()) {
              Label newLabel = new Label(imageCaptions.get(currentImageIndex));
              singlePictureRegion.getChildren().remove(1);
              singlePictureRegion.getChildren().add(newLabel);
            }
            pane.setCenter(singlePictureRegion);
            isThumbnailView = false;
          }
        }
      });
    }
    VBox imageBox3 = new VBox();
    imageBox3.getChildren().addAll(thumbView3, thumbLabel3);
    imageBox3.setAlignment(Pos.CENTER);
    Label thumbLabel4 = createThumbnailLabel(4);
    thumbLabel4.setMaxWidth(150);
    ImageView thumbView4 = createThumbnailView(4);
    if (imageList.size() >= 4) {
      thumbView4.setOnMouseClicked(mouseEvent -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          if (mouseEvent.getClickCount() == 2) {
            pane.getChildren().remove(tilePane);
            currentImageIndex = (currentImageIndex + 3) % imageList.size();
            imageView.setImage(imageList.get(currentImageIndex));
            imageWidth = imageList.get(currentImageIndex).getWidth();
            imageHeight = imageList.get(currentImageIndex).getHeight();
            setOriginalProportion();
            imageView.setFitWidth(imageWidth * getProportion());
            imageView.setFitHeight(imageHeight * getProportion());
            imageView.setPreserveRatio(false);
            if (currentImageIndex < imageCaptions.size()) {
              Label newLabel = new Label(imageCaptions.get(currentImageIndex));
              singlePictureRegion.getChildren().remove(1);
              singlePictureRegion.getChildren().add(newLabel);
            }
            pane.setCenter(singlePictureRegion);
            isThumbnailView = false;
          }
        }
      });
    }
    VBox imageBox4 = new VBox();
    imageBox4.getChildren().addAll(thumbView4, thumbLabel4);
    imageBox4.setAlignment(Pos.CENTER);
    tilePane.getChildren().addAll(imageBox1, imageBox2, imageBox3, imageBox4);
    tilePane.setPrefColumns(2);
    tilePane.setMaxWidth(650);
    tilePane.setHgap(SPACE_BETWEEN_THUMBNAIL);
    tilePane.setVgap(SPACE_BETWEEN_THUMBNAIL);
    tilePane.setAlignment(Pos.CENTER);
    return tilePane;
  }

  /**
   * A borderPane to organize the basic elements of the JIMachine.
   * @return a set-up pane
   */
  protected BorderPane getPane() {
    HBox paneForButtons = new HBox(SPACE_BETWEEN_BUTTONS);
    Button openFileButton = new Button("Open File");
    Button thumbnailButton = new Button("Thumbnail");
    Button previousButton = new Button("Previous");
    Button nextButton = new Button("Next");
    Button zoomInButton = new Button("Zoom In");
    Button originalSizeButton = new Button("100%");
    Button zoomOutButton = new Button("Zoom Out");
    Button captionsButton = new Button("Captions");
    Button saveAlbumButton = new Button("Save Album");
    Button openAlbumButton = new Button("Open Album");
    Button quitButton = new Button("Quit");

    paneForButtons.getChildren().addAll(openFileButton,
      thumbnailButton,
      previousButton,
      nextButton,
      zoomInButton,
      originalSizeButton,
      zoomOutButton,
      captionsButton,
      saveAlbumButton,
      openAlbumButton,
      quitButton);
    paneForButtons.setAlignment(Pos.CENTER);

    BorderPane pane = new BorderPane();
    pane.setTop(paneForButtons);

    ImageView imageView = new ImageView();
    if (currentDirectory == null) {
      currentDirectory = Paths.get("").toUri().getPath();
    }
    getAllImages(currentDirectory, null);

    if (imageList != null && !imageList.isEmpty()) {
      imageView.setImage(imageList.get(currentImageIndex));
      imageWidth = imageList.get(currentImageIndex).getWidth();
      imageHeight = imageList.get(currentImageIndex).getHeight();
      setOriginalProportion();
      imageView.setFitWidth(imageWidth * getProportion());
      imageView.setFitHeight(imageHeight * getProportion());
      imageView.setPreserveRatio(false);
    }
    Label imageLabel = new Label();
    if (currentImageIndex < imageCaptions.size()) {
      imageLabel = new Label(imageCaptions.get(currentImageIndex));
    }
    VBox singlePictureRegion = new VBox();
    singlePictureRegion.getChildren().addAll(imageView, imageLabel);
    singlePictureRegion.setAlignment(Pos.CENTER);
    if (isThumbnailView) {
      pane.setCenter(thumbnailTilePane(pane, imageView, singlePictureRegion));
    } else {
      pane.setCenter(singlePictureRegion);
    }

    openFileButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Resource File");
      fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files",
          "*.jpg"));
      File selectedFile = fileChooser
        .showOpenDialog(openFileButton.getScene().getWindow());
      if (selectedFile != null) {
        try {
          String pathStr = selectedFile.toURI().getPath();
          currentDirectory = pathStr.substring(0, pathStr.lastIndexOf('/'));
          isCaptionChanged = false;
          getAllImages(currentDirectory, pathStr);
          Image image =
            new Image(selectedFile.toURI().toURL().toExternalForm());
          imageView.setImage(image);
          imageWidth = image.getWidth();
          imageHeight = image.getHeight();
          setOriginalProportion();
          imageView.setFitWidth(imageWidth * getProportion());
          imageView.setFitHeight(imageHeight * getProportion());
          if (currentImageIndex < imageCaptions.size()) {
            Label newLabel = new Label(imageCaptions.get(currentImageIndex));
            singlePictureRegion.getChildren().remove(1);
            singlePictureRegion.getChildren().add(newLabel);
          }
          if (isThumbnailView) {
            isThumbnailView = false;
            pane.getChildren().remove(1);
            pane.setCenter(singlePictureRegion);
          }
        } catch (MalformedURLException e1) {
          System.out.println(e1);
        }
      }
    });

    thumbnailButton.setOnAction(e -> {
      isThumbnailView = true;
      pane.getChildren().remove(singlePictureRegion);
      pane.setCenter(thumbnailTilePane(pane, imageView, singlePictureRegion));
    });

    previousButton.setOnAction(e -> {
      if (isThumbnailView) {
        if (imageList.size() > THUMBNAIL_TOTAL) {
          if (currentImageIndex < THUMBNAIL_TOTAL) {
            currentImageIndex = (imageList.size() + currentImageIndex - THUMBNAIL_TOTAL) % imageList.size();
          } else {
            currentImageIndex = (currentImageIndex - THUMBNAIL_TOTAL) % imageList.size();
          }
          pane.getChildren().remove(1);
          pane.setCenter(thumbnailTilePane(pane, imageView, singlePictureRegion));
        }
      } else {
        if (!imageList.isEmpty()) {
          currentImageIndex--;
          if (currentImageIndex < 0 || currentImageIndex >= imageList.size()) {
            currentImageIndex = imageList.size() - 1;
          }
          imageView.setImage(imageList.get(currentImageIndex));
          imageWidth = imageList.get(currentImageIndex).getWidth();
          imageHeight = imageList.get(currentImageIndex).getHeight();
          setOriginalProportion();
          imageView.setFitWidth(imageWidth * getProportion());
          imageView.setFitHeight(imageHeight * getProportion());
          imageView.setPreserveRatio(false);
          if (currentImageIndex < imageCaptions.size()) {
            Label newLabel = new Label(imageCaptions.get(currentImageIndex));
            singlePictureRegion.getChildren().remove(1);
            singlePictureRegion.getChildren().add(newLabel);
          }
        }
      }
    });

    nextButton.setOnAction(e -> {
      if (isThumbnailView) {
        if (imageList.size() > THUMBNAIL_TOTAL) {
          currentImageIndex = (currentImageIndex + THUMBNAIL_TOTAL) % imageList.size();
          pane.getChildren().remove(1);
          pane.setCenter(thumbnailTilePane(pane, imageView, singlePictureRegion));
        }
      } else {
        if (!imageList.isEmpty()) {
          currentImageIndex++;
          if (currentImageIndex >= imageList.size()) {
            currentImageIndex = 0;
          }
          imageView.setImage(imageList.get(currentImageIndex));
          imageWidth = imageList.get(currentImageIndex).getWidth();
          imageHeight = imageList.get(currentImageIndex).getHeight();
          setOriginalProportion();
          imageView.setFitWidth(imageWidth * getProportion());
          imageView.setFitHeight(imageHeight * getProportion());
          imageView.setPreserveRatio(false);
          if (currentImageIndex < imageCaptions.size()) {
            Label newLabel = new Label(imageCaptions.get(currentImageIndex));
            singlePictureRegion.getChildren().remove(1);
            singlePictureRegion.getChildren().add(newLabel);
          }
        }
      }
    });

    zoomInButton.setOnAction(e -> {
      increaseProportion();
      imageView.setFitWidth(imageWidth * getProportion());
      imageView.setFitHeight(imageHeight * getProportion());
    });

    originalSizeButton.setOnAction(e -> {
      setOriginalProportion();
      imageView.setFitWidth(imageWidth * getProportion());
      imageView.setFitHeight(imageHeight * getProportion());
    });

    zoomOutButton.setOnAction(e -> {
      decreaseProportion();
      imageView.setFitWidth(imageWidth * getProportion());
      imageView.setFitHeight(imageHeight * getProportion());
    });

    captionsButton.setOnAction(e -> {
      isCaptionChanged = true;
      if (currentImageIndex < imageCaptions.size()) {
        TextInputDialog dialog = new TextInputDialog(imageCaptions.get(currentImageIndex));

        dialog.setTitle("Captions Dialog");
        dialog.setHeaderText("Enter new Caption of your image:");
        dialog.setContentText("Caption:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
          imageCaptions.set(currentImageIndex, result.get());
          Label newLabel = new Label(imageCaptions.get(currentImageIndex));
          if (isThumbnailView) {
            pane.getChildren().remove(1);
            pane.setCenter(thumbnailTilePane(pane, imageView, singlePictureRegion));
          } else {
            singlePictureRegion.getChildren().remove(1);
            singlePictureRegion.getChildren().add(newLabel);
          }
        }
      }
    });

    saveAlbumButton.setOnAction(e -> {
      Album object = new Album(currentImageIndex, currentDirectory, isThumbnailView, isCaptionChanged, imageCaptions);
      if (albumSavePath == null) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Specify a path and a filename to save the Album");
        File selectedFile = fileChooser
          .showOpenDialog(saveAlbumButton.getScene().getWindow());
        if (selectedFile != null) {
          albumSavePath = selectedFile.toURI().getPath();
        }
      }

      // Serialization
      try {
        //Saving of object in a file
        FileOutputStream file = new FileOutputStream(albumSavePath);
        ObjectOutputStream out = new ObjectOutputStream(file);

        // Method for serialization of object
        out.writeObject(object);

        out.close();
        file.close();

      } catch(IOException ex) {
        System.out.println(ex);
      }
    });

    openAlbumButton.setOnAction(e -> {
      Album object = null;
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select a file to open an Album");
      File selectedFile = fileChooser
        .showOpenDialog(saveAlbumButton.getScene().getWindow());
      if (selectedFile != null) {
        // Deserialization
        try {
          // Reading the object from a file
          FileInputStream file = new FileInputStream(selectedFile.toURI().getPath());
          ObjectInputStream in = new ObjectInputStream(file);

          // Method for deserialization of object
          object = (Album) in.readObject();

          in.close();
          file.close();

          currentDirectory = object.directory;
          getAllImages(currentDirectory, null);
          currentImageIndex = object.imageIndex;
          isThumbnailView = object.viewMode;
          isCaptionChanged = object.captionChanged;
          imageCaptions = object.captions;
          if (isThumbnailView) {
            if (!imageList.isEmpty()) {
              pane.getChildren().remove(1);
              pane.setCenter(thumbnailTilePane(pane, imageView, singlePictureRegion));
            }
          } else {
            if (!imageList.isEmpty()) {
              imageView.setImage(imageList.get(currentImageIndex));
              imageWidth = imageList.get(currentImageIndex).getWidth();
              imageHeight = imageList.get(currentImageIndex).getHeight();
              setOriginalProportion();
              imageView.setFitWidth(imageWidth * getProportion());
              imageView.setFitHeight(imageHeight * getProportion());
              imageView.setPreserveRatio(false);
              if (currentImageIndex < imageCaptions.size()) {
                Label newLabel = new Label(imageCaptions.get(currentImageIndex));
                singlePictureRegion.getChildren().remove(1);
                singlePictureRegion.getChildren().add(newLabel);
              }
              pane.getChildren().remove(1);
              pane.setCenter(singlePictureRegion);
            }
          }
        } catch(IOException ex) {
          System.out.println("IOException is caught");
        } catch(ClassNotFoundException ex) {
          System.out.println("ClassNotFoundException is caught");
        }
      }
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
    Scene scene = new Scene(getPane(), ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
    scene.setOnKeyPressed(e ->{
      if (e.getCode() == KeyCode.LEFT) {
        if (isThumbnailView) {
          if (imageList.size() > THUMBNAIL_TOTAL) {
            if (currentImageIndex < THUMBNAIL_TOTAL) {
              currentImageIndex = (imageList.size() + currentImageIndex - THUMBNAIL_TOTAL) % imageList.size();
            } else {
              currentImageIndex = (currentImageIndex - THUMBNAIL_TOTAL) % imageList.size();
            }
          }
        } else {
          currentImageIndex--;
          if (currentImageIndex < 0 || currentImageIndex >= imageList.size()) {
            currentImageIndex = imageList.size() - 1;
          }
        }
        scene.setRoot(getPane());
      } else if (e.getCode() == KeyCode.RIGHT) {
        if (isThumbnailView) {
          if (imageList.size() > THUMBNAIL_TOTAL) {
            currentImageIndex = (currentImageIndex + THUMBNAIL_TOTAL) % imageList.size();
          }
        } else {
          currentImageIndex++;
          if (currentImageIndex >= imageList.size()) {
            currentImageIndex = 0;
          }
        }
        scene.setRoot(getPane());
      }
    });
    primaryStage.setTitle("JIMachine");
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
