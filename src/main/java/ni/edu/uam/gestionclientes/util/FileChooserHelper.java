package ni.edu.uam.gestionclientes.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public class FileChooserHelper {
    public File selectImageFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Client Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        return fileChooser.showOpenDialog(window);
    }

    public File selectDirectory(Window window) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Export Directory");
        return directoryChooser.showDialog(window);
    }
}
