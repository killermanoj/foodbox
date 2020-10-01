
package sample.foody;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmedController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    public void exitScreen(ActionEvent event){
        System.exit(0);
    }
    
}
