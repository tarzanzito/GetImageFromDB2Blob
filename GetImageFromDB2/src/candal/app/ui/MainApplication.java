
package candal.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Tarzanzito
 */
public class MainApplication extends Application {

    /**
     * @param args the command line arguments
     * @throws Exception 
     */
    public static void main(String[] args) {

    	launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
    	//fxml root element with-> attribute "fx:controller="candal.app.ui.MainWindowController"
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

        stage.setResizable(false);
        stage.setTitle("Get Image From DB2 - Version: " + "1.1.2");
        stage.centerOnScreen();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("application-8.png")));

    	Scene scene = new Scene(root);

        //start stage with a scene
        stage.setScene(scene);
        stage.show(); //or stage.showAndWait();
    }

}

