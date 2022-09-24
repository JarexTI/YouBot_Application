package com.example.youbot.SSH;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youbot.MainActivity;
import com.example.youbot.SecondActivity;
import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ClientSsh extends AppCompatActivity implements Runnable {

    public static ChannelShell channel;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static boolean boolWhileSSH = true;

    String username;
    String host;
    String password;

    public ClientSsh(String username, String host, String password) {
        this.username = username;
        this.host = host;
        this.password = password;
    }

    // SSH подключение
    @Override
    public void run() {
        JSch jsch = new JSch();
        try {
            // данные по порту не используем, т.к. он дефолтный - 22
            MainActivity.session = jsch.getSession(username, host);
            MainActivity.session.setPassword(password);
            MainActivity.session.setConfig("StrictHostKeyChecking", "no");
        } catch (JSchException e) {
            e.printStackTrace();
        }

        // открываем сессию SSH
        try {
            MainActivity.session.connect();
        } catch (JSchException e) {
            MainActivity.viewResult();

            MainActivity.session = null;
            return;
        }

        // запускаем канал shell и открываем потоки данных
        try {
            channel = (ChannelShell) MainActivity.session.openChannel("shell");
            in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(channel.getOutputStream()));
            channel.connect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }

        setTrueBoolWhileSSH();

        runOnUiThread(() -> {
            String result = "SSH соединение установлено";
            MainActivity.resultTextSsh.setText(result);
        });

        // крутим поток входящих данных
        while (boolWhileSSH) {
            String textInput;
            try {
                textInput = in.readLine();
                String finalTextInput = textInput;
                if (finalTextInput != null) {
                    if (MainActivity.flagMainActivity) {
                        // обновление adapterTerminal
                        runOnUiThread(() -> {
                            MainActivity.arrayListTerminal.add(finalTextInput);
                            MainActivity.adapterTerminal.notifyDataSetChanged();
                        });
                    } else if (SecondActivity.flagSecondActivity) {
                        // обновление adapterTerminal
                        runOnUiThread(() -> {
                            SecondActivity.arrayListSecondTerminal.add(finalTextInput);
                            SecondActivity.adapterSecondTerminal.notifyDataSetChanged();
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // отображаем элементы в списке "listViewTerminal"
            runOnUiThread(() -> MainActivity.listViewTerminal.setAdapter(MainActivity.adapterTerminal));
        }
    }

    // запускаем rosLaunch
    public static void rosLaunch() {
        try {
            out.write("roslaunch youbot_tactical_level ytl.launch\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // прекращение работы rosLaunch
    public static void closeRosLaunch() {
        try {
            out.write("Ctrl+^C\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // отключение SSH соединение
    public static void sshDisconnect() {
        setFalseBoolWhileSSH();
        if (channel != null) {
            channel.disconnect();
            channel = null;
        }
        if (MainActivity.session != null) {
            MainActivity.session.disconnect();
            MainActivity.session = null;
        }
    }

    private static void setTrueBoolWhileSSH() {
        boolWhileSSH = true;
    }

    private static void setFalseBoolWhileSSH() {
        boolWhileSSH = false;
    }
}
