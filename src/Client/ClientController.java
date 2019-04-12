package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


public class ClientController {

    @FXML private Button loginBtn;

    @FXML private TextField userId;

    @FXML private PasswordField userPwd;

    @FXML private void login(ActionEvent actionEvent){
        Socket client = null;
        OutputStream os = null;
        InputStream is = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            String userName = userId.getText();
            char[] passwordChar = userPwd.getText().toCharArray();
            String passwordString = String.copyValueOf(passwordChar);

            //检查是否为空
            if (checkNull(userName) || checkNull(passwordString)) {
                JOptionPane.showMessageDialog(null, "pleas give right login information");
                return;
            }

            client = new Socket(ClientConfig.IP_ADDR, ClientConfig.PORT);
            os = client.getOutputStream();
            dos = new DataOutputStream(os);
            dos.write(userId.getText().getBytes());
            is = client.getInputStream();
            dis = new DataInputStream(is);
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len=dis.read(bytes))!= -1){
                String str = new String(bytes);
                JOptionPane.showMessageDialog(null,str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                    try {
                        is.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
            }
            if (os != null){
                try {
                    os.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (dis != null){
                try {
                    dis.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (dos != null){
                try {
                    dos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkNull(String toCheck) {
        if (toCheck.equals("") || toCheck == null)
            return true;
        return false;
    }

}
