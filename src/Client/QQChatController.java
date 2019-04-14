package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class QQChatController implements Initializable {

    private Socket client;
    private int id;
    private ObservableList<Integer> userList;

    @FXML
    private Text userID;

    @FXML
    private ListView<Integer> userListViem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userListViem = new ListView<Integer>();

    }

    public void transferSocketInfo(Socket socket){
        this.client = socket;
        try {
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            Message msg = (Message) ois.readObject();
            id = msg.getSender();
            userID.setText("qq id: "+ id);
            userList = FXCollections.observableArrayList(msg.getUserlist());
            System.out.println(userList);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateUserlist(){

    }





}
