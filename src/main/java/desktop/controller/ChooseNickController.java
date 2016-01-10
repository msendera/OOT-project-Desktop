package desktop.controller;

import desktop.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import desktop.model.AppConfig;
import desktop.model.IServerConnector;
import desktop.model.ServerConnectorFactory;

public class ChooseNickController {

    @FXML
    public TextField nickField;
    @FXML
    public Button MainButton;

    private IServerConnector serverConnector = ServerConnectorFactory.getServerConnector();

    public void handleMainButtonPressed(ActionEvent actionEvent) {
        AppConfig.setUserNick(nickField.getText());
        serverConnector.registerClient(nickField.getText());
        Main.createMainWindow();
        Main.closeShowNickWindow();
    }


    public void handleNickChanged(Event event) {
        if (nickFieldEmpty()) {
            setButtonDisabled(true);
        } else {
            setButtonDisabled(false);
        }
    }

    private boolean nickFieldEmpty() {
        return nickField.getText().isEmpty();
    }

    private void setButtonDisabled(boolean val) {
        MainButton.disableProperty().setValue(val);
    }
}
