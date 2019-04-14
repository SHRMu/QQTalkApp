package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


public class QQLoginController {

    @FXML private Button loginBtn;

    @FXML private TextField userId;

    @FXML private PasswordField userPwd;

    @FXML private Button registerBtn;

    @FXML private void login(ActionEvent actionEvent){

        loginBtn.setDisable(true);

        Socket client = null;

        String userName = userId.getText();
        char[] passwordChar = userPwd.getText().toCharArray();
        String passwordString = String.copyValueOf(passwordChar);

        if (checkNull(userId.getText()) || checkNull(passwordString)) {
            JOptionPane.showMessageDialog(null, "pleas give right login information");
            return;
        }

        try {
            client = new Socket(ClientConfig.IP_ADDR, ClientConfig.PORT);
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(new Message(Integer.parseInt(userName),0,MessageType.TYPE_LOGIN,passwordString));
            oos.flush();
//            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
//            Message msg = (Message) ois.readObject();
//            JOptionPane.showMessageDialog(null, msg.getInfo());

            changeToChatScene(actionEvent, client);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeToChatScene(ActionEvent event, Socket socket) throws IOException{
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("QQ_Chat.fxml"));
        Parent chatRoot = chatLoader.load();

        QQChatController qqChatController = chatLoader.getController();
        qqChatController.transferSocketInfo(socket);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(chatRoot));
        window.show();
    }

    private boolean checkNull(String toCheck) {
        if (toCheck.equals("") || toCheck == null)
            return true;
        return false;
    }

    @FXML private void register(ActionEvent event) throws IOException{
        ;
    }

}
