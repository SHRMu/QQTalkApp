package Server;

import Client.Message;
import Client.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ServerController implements Initializable {

    private StringBuilder sb;
    private ServerSocket server;
    private ArrayList<ClientHandler> clients;
    private ArrayList<Integer> userList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startBtn.setDisable(false);
        stopBtn.setDisable(true);

        sb = new StringBuilder();
        clients = new ArrayList<>();
        userList = new ArrayList<>();

    }

    @FXML private Button startBtn;

    @FXML private Button stopBtn;

    @FXML private TextArea messageTextArea;

    @FXML private void startServer(ActionEvent actionEvent) {
        //set button status
        startBtn.setDisable(true);
        stopBtn.setDisable(false);

        sb = sb.append("server starting ...\n");
        messageTextArea.setText(sb.toString());

        new Thread(new ServerThread()).start();

    }

    //used for server listening
    private class ServerThread implements Runnable{

        @Override
        public void run() {
            try {
                server = new ServerSocket(ServerConfig.PORT);
                sb.append("server is waiting at port " + ServerConfig.PORT + "\n");
                messageTextArea.setText(sb.toString());

                while (true) {
                    //Todo: interrupt before server.close()
                    Socket client = server.accept();
                    sb.append("new connection from client : " + client.getInetAddress().getHostAddress()+ "\n");
                    messageTextArea.setText(sb.toString());
                    new Thread(new ClientHandler(client, clients)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     }

    public class ClientHandler implements Runnable{
        private int id;
        private Socket socket;
        private ArrayList<ClientHandler> clients;
        private boolean flag = true;

        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public int getId() {
            return id;
        }

        public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
            this.socket = socket;
            this.clients = clients;
            clients.add(this);
        }

        @Override
        public void run() {
            try {
                System.out.println("Client : " + socket.getInetAddress().getHostName());
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                //Todo; change flag when user exit
                while (flag){
                    Message msg = (Message) ois.readObject();
                    switch (msg.getMessageType()){
                        case MessageType.TYPE_LOGIN:
                            id = msg.getSender();
                            userList.add(id);
                            msg.setInfo("welcome");
                            msg.setUserlist(userList);
                            oos.writeObject(msg);
                            break;
                        case MessageType.TYPE_UPDATE_USERLIST:
                            oos.writeObject(clients);
                            break;
                        case MessageType.TYPE_CHAT:
                            int receiver = msg.getReceiver();
                            for (ClientHandler client:
                                 clients) {
                                if (receiver == client.id && receiver != this.id){
                                    client.oos.writeObject(msg);
                                    break;
                                }
                            }
                    }
                }
                ois.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    @FXML private void stopServer(ActionEvent actionEvent){
        startBtn.setDisable(false);
        stopBtn.setDisable(true);
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
