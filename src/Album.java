import java.io.Serializable;
import java.util.ArrayList;

class Album implements Serializable {
  public int imageIndex;
  public String directory;
  public boolean viewMode;
  public boolean captionChanged;
  public ArrayList<String> captions;

  // Default constructor
  public Album(int imageIndex, String directory, boolean viewMode, boolean captionChanged, ArrayList<String> captions) {
    this.imageIndex = imageIndex;
    this.directory = directory;
    this.viewMode = viewMode;
    this.captionChanged = captionChanged;
    this.captions = captions;
  }

}
