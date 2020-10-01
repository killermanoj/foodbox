
package sample.foody;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {
    
    @FXML
     public void loginScreen(ActionEvent event) throws Exception  {
		Stage primaryStage =new Stage();
                primaryStage.initStyle(StageStyle.UNDECORATED);
		Parent root =FXMLLoader.load(getClass().getResource("../view/OTPlogin.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
                
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	public void loginOTPScreen(ActionEvent event) throws Exception  {
		Stage primaryStage =new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		Parent root =FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Hide this current window (if this is what you want)
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

     @FXML
      public void signupScreen(ActionEvent event) throws Exception  {
		Stage primaryStage =new Stage();
                primaryStage.initStyle(StageStyle.UNDECORATED);
		Parent root =FXMLLoader.load(getClass().getResource("../view/Signup.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
                
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
	}
      
      
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
