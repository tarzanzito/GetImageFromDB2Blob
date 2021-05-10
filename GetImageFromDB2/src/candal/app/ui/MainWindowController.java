
package candal.app.ui;

import candal.app.business.ConfigFile;
import candal.app.business.DB2wrapper;
import candal.app.business.ImagesFullFileName;

import java.net.URL;
//import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.control.Label;

/**
 *
 * @author Tarzanzito
 */
public class MainWindowController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField textFieldUrl;
    @FXML
    private TextField textFieldSchema;
    @FXML
    private TextField textFieldUserId;
    @FXML
    private TextField textFieldUserPwd;
    @FXML
    private TextField textFieldRegID;
    @FXML
    private Button buttonGet;
    @FXML
    private Button buttonImage1;
    @FXML
    private Button buttonImage2;
    @FXML
    private Button buttonImage3;
    @FXML
    private Button buttonFolder;
    @FXML
    private Button buttonClose;
    @FXML
    private Label labelMessage;
    @FXML
    private ImageView imageViewArrived;
    
    //others
    private DB2wrapper _DB2wrapper = null;
    private int _currentImage = -1;
    private ImagesFullFileName _imagesFullName = null; 
    private String _url;
    private String _schema;
    private String _userId;
    private String _userPwd;
    private String _defaultFolder;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    	imageViewArrived.setPreserveRatio(false);
    	
    	readConfigFile();
    	
    	lockFields(true);

	   	textFieldUrl.setText(_url);
   	   	textFieldSchema.setText(_schema);
   	   	textFieldUserId.setText(_userId);
   	   	textFieldUserPwd.setText(_userPwd);

   	   	_defaultFolder = System.getProperty("user.dir");
   		//_defaultFolder = new java.io.File( "." ).getCanonicalPath();
    }    

    @FXML
    private void handleButtonGetAction(ActionEvent event) {

    	clearFields(false);
    	
      	try {
           	if (_DB2wrapper == null)
           		CreateDB2Wrapper();
      		
      		_imagesFullName = _DB2wrapper.getAllImages(textFieldRegID.getText().trim());
      		
      		mountImage(0);
      		
        } catch (Exception e) {
        	_DB2wrapper = null;
        	labelMessage.setText(e.getMessage());
        }
      	finally {
	    	lockFields(false);
      	}
    }
    
    @FXML
    private void handleButtonImage1Action(ActionEvent event) {

    	mountImage(0);
    }

    @FXML
    private void handleButtonImage2Action(ActionEvent event) {
    	
    	mountImage(1);
    }
    
    @FXML
    private void handleButtonImage3Action(ActionEvent event) {
    	
    	mountImage(2);
    }
    
    @FXML
    private void handleButtonCloseAction(ActionEvent event) {
    	
    	Platform.exit();
    }
    
    @FXML
    private void handleButtonFolderAction(ActionEvent event) {
    	        
      	try {
      		Runtime.getRuntime().exec("explorer.exe /select, \"" + _defaultFolder + "\"");
		} catch (Exception e) {
        	labelMessage.setText(e.getMessage());
		}
    }
    
    private void CreateDB2Wrapper() {
    	
    	try {
        	_DB2wrapper = new DB2wrapper(_url, _userId, _userPwd, _schema, _defaultFolder);
		} catch (Exception e) {
        	labelMessage.setText(e.getMessage());
		}
    }
    
    private void mountImage(int imageNumber) {

    	if (_currentImage == imageNumber)
    		return;

    	imageViewArrived.setImage(null);
    	
    	String fullFileName = _imagesFullName.getImageFullFileName(imageNumber);
    	
    	try {
    		File file = new File(fullFileName);
    		Image image = new Image(file.toURI().toString());
    		imageViewArrived.setImage(image);
    		imageViewArrived.setPreserveRatio(false); 
    	} catch (Exception e) {
        	labelMessage.setText(e.getMessage());
    	}
    	
    	_currentImage = imageNumber;
    	lockButtonsImage();
    }
    
    private void lockButtonsImage() {

    	switch (_currentImage) {
    		default:
	        	buttonFolder.setDisable(true);
	        	buttonImage3.setDisable(true);
	        	buttonImage2.setDisable(true);
	    		buttonImage1.setDisable(true);
	        	break;
    		case 0:
	        	buttonFolder.setDisable(false);
	        	buttonImage3.setDisable(false);
	        	buttonImage2.setDisable(false);
	    		buttonImage1.setDisable(true);
	        	break;
    		case 1:
	        	buttonFolder.setDisable(false);
	        	buttonImage3.setDisable(false);
	        	buttonImage2.setDisable(true);
	    		buttonImage1.setDisable(false);
	        	break;    			
    		case 2:
	        	buttonFolder.setDisable(false);
	        	buttonImage3.setDisable(true);
	        	buttonImage2.setDisable(false);
	    		buttonImage1.setDisable(false);
	        	break;    			        	
    	}
    }
  
    private void clearFields(boolean all) {

    	if (all)
    		textFieldRegID.setText("");
    	
    	labelMessage.setText("");
    	_currentImage = -1;
    	//AA_imagesFullName[0] = "";
    	//AA_imagesFullName[1] = "";
    	//AA_imagesFullName[2] = "";
    	imageViewArrived.setImage(null);
    }
    
    private void lockFields(boolean lockIt) {

    	labelMessage.setText("");
    	
    	textFieldRegID.setDisable(false);
    	buttonGet.setDisable(false);
    	buttonClose.setDisable(false);
    	
		buttonGet.setDefaultButton(true);

    	lockButtonsImage();

  		textFieldRegID.requestFocus();
    }
    
   	private void readConfigFile() {
   		
   		try {
   			
   			ConfigFile conf = new ConfigFile();
   			conf.read();
   			
   			_url = conf.getUrl().trim();
   			_schema = conf.getSchema().trim();
   			_userId = conf.getUserId().trim();
   			_userPwd = conf.getUserPwd().trim();
	  
   		} catch (FileNotFoundException e) {
   			labelMessage.setText(e.getMessage());
   		} catch (IOException e) {
   			labelMessage.setText(e.getMessage());
   		} catch (Exception e) {
   			labelMessage.setText(e.getMessage());
   			e.printStackTrace();
		}
   	}    
    
}    
