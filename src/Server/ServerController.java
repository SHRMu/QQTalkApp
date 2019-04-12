package Server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    private StringBuilder sb;
    private ServerSocket server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStartBtn(true);
        setStopBtn(false);
    }

    @FXML private Button startBtn;

    @FXML private Button stopBtn;

    @FXML private TextArea messageTextArea;

    private void setStartBtn(boolean b){
        startBtn.setDisable(!b);
    }

    private void setStopBtn(boolean b){
        stopBtn.setDisable(!b);
    }

    @FXML private void startServer(ActionEvent actionEvent) {
        setStartBtn(false);
        setStopBtn(true);

        sb = new StringBuilder("server starting ...\n");

        messageTextArea.setText(sb.toString());

        new Thread(new Runnable() {
            DataInputStream dis = null;
            DataOutputStream dos = null;

            @Override
            public void run() {
                try {
                    server = new ServerSocket(10000);
                    while (true) {
                        Socket client = server.accept();
                        new HandlerThread(client);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        dis.close();
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ).start();
    }

    private class HandlerThread implements Runnable{
        private Socket socket;
        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        @Override
        public void run() {
            DataInputStream dis = null;
            DataOutputStream dos = null;
            try{
                dis = new DataInputStream(socket.getInputStream());
                byte[] bytes = new byte[1024];
                dis.read(bytes,0, bytes.length);
                sb.append("client : " + new String(bytes) + " login in !\n");
                messageTextArea.setText(sb.toString());
                dos = new DataOutputStream(socket.getOutputStream());
                dos.write("ack from your server".getBytes());

            }catch (IOException e){
                e.printStackTrace();
            } finally {
                try {
                    dis.close();
                    dos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML private void stopServer(ActionEvent actionEvent){
        setStartBtn(true);
        setStopBtn(false);
    }
}
