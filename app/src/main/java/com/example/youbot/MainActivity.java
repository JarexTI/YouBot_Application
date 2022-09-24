package com.example.youbot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youbot.SSH.ClientSsh;
import com.example.youbot.TCP.TcpClient;
import com.jcraft.jsch.Session;

import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Session session = null;

    @SuppressLint("StaticFieldLeak")
    public static TextView resultTextSsh;
    EditText inputIpSsh, inputUsernameSsh, inputPasswordSsh;
    Button button_connect, button_disconnect, button_rosLaunch, button_closeRosLaunch;

    public static ArrayList<String> arrayListTerminal = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    public static ListView listViewTerminal;
    public static ArrayAdapter<String> adapterTerminal;

    @SuppressLint("StaticFieldLeak")
    public static TextView tcpResultText;
    EditText inputIpTCP, inputPortTCP;
    Button buttonConnectTCP, buttonDisconnectTCP;

    public static Socket clientSocket = null;

    boolean boolRosLaunch = true;
    public static boolean flagMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // кнопоки и поля для SSH
        resultTextSsh = findViewById(R.id.resultTextSsh);
        inputIpSsh = findViewById(R.id.inputIpSsh);
        inputUsernameSsh = findViewById(R.id.inputUsernameSsh);
        inputPasswordSsh = findViewById(R.id.inputPasswordSsh);
        button_connect = findViewById(R.id.button_connect);
        button_disconnect = findViewById(R.id.button_disconnect);

        // кнопоки rosLaunch
        button_rosLaunch = findViewById(R.id.button_rosLaunch);
        button_closeRosLaunch = findViewById(R.id.button_close_rosLaunch);

        // терминал
        listViewTerminal = (ListView) findViewById(R.id.list_of_text_terminal);

        // кнопоки и поля для TCP
        tcpResultText = findViewById(R.id.tcp_result_text);
        inputIpTCP = findViewById(R.id.inputTCPip);
        inputPortTCP = findViewById(R.id.inputPortTCP);
        buttonConnectTCP = findViewById(R.id.button_connect_TCP);
        buttonDisconnectTCP = findViewById(R.id.button_disconnect_TCP);

        adapterTerminal = new ArrayAdapter<>(this,
                android.R.layout.test_list_item,
                arrayListTerminal);

        // кнопка подключения ssh
        button_connect.setOnClickListener(v -> {
            if (session == null) {
                if (inputIpSsh.getText().toString().equals("")) {
                    String result = "ip поле пустое";
                    resultTextSsh.setText(result);
                } else if (inputUsernameSsh.getText().toString().equals("")) {
                    String result = "username поле пустое";
                    resultTextSsh.setText(result);
                } else if (inputPasswordSsh.getText().toString().equals("")) {
                    String result = "password поле пустое";
                    resultTextSsh.setText(result);
                } else {
                    String host = inputIpSsh.getText().toString();
                    String username = inputUsernameSsh.getText().toString();
                    String password = inputPasswordSsh.getText().toString();

                    ClientSsh clientSsh = new ClientSsh(username, host, password);
                    Thread thread1 = new Thread(clientSsh);
                    thread1.start();
                }
            } else {
                String result = "Подключение уже есть";
                resultTextSsh.setText(result);
            }
        });

        // кнопка отключение ssh
        button_disconnect.setOnClickListener(v -> {
            if (session != null && clientSocket == null) {
                ClientSsh.sshDisconnect();
                String result = "SSH соединение разорвано";
                resultTextSsh.setText(result);
            } else if (clientSocket != null && session != null) {
                String result = "Отключитесь по TCP";
                resultTextSsh.setText(result);
            } else if (boolRosLaunch) {
                String result = "Закройте rosLaunch";
                resultTextSsh.setText(result);
            } else {
                String result = "Сначала подключитесь по SSH";
                resultTextSsh.setText(result);
            }
        });

        // кнопка для отправки команды на Ubuntu "rosLaunch"
        button_rosLaunch.setOnClickListener(v -> {
            if (session != null && clientSocket == null && boolRosLaunch) {
                ClientSsh.rosLaunch();
                String result = "rosLaunch запущен";
                resultTextSsh.setText(result);
                boolRosLaunch = false;
            } else if (!boolRosLaunch) {
                String result = "rosLaunch уже запущен";
                resultTextSsh.setText(result);
            } else if (session == null) {
                String result = "Сначала подключитесь по SSH";
                resultTextSsh.setText(result);
            }
        });

        button_closeRosLaunch.setOnClickListener(v -> {
            if (session != null && clientSocket == null && !boolRosLaunch) {
                ClientSsh.closeRosLaunch();
                String result = "rosLaunch закрыт";
                resultTextSsh.setText(result);
                setBoolRosLaunch();
            } else if (session != null && clientSocket != null) {
                String result = "Отключитесь по TCP";
                resultTextSsh.setText(result);
            } else if (boolRosLaunch) {
                String result = "Запустите rosLaunch";
                resultTextSsh.setText(result);
            }
        });

        // кнопка для подключения по TCP
        buttonConnectTCP.setOnClickListener(v -> {
            if (session != null && clientSocket == null) {
                if (inputIpTCP.getText().toString().equals("")) {
                    String result = "ip поле пустое";
                    tcpResultText.setText(result);
                } else if (inputPortTCP.getText().toString().equals("")) {
                    String result = "port поле пустое";
                    tcpResultText.setText(result);
                } else {
                    String ipTcp = inputIpTCP.getText().toString();
                    int portTcp = Integer.parseInt(inputPortTCP.getText().toString());

                    TcpClient tcpClient = new TcpClient(ipTcp, portTcp);
                    Thread threadTcp = new Thread(tcpClient);
                    threadTcp.start();
                }
            } else {
                String result = "rosLaunch не запущен";
                tcpResultText.setText(result);
            }
        });

        // кнопка отключения по TCP
        buttonDisconnectTCP.setOnClickListener(v -> {
            if (session != null && clientSocket != null) {
                TcpClient.disconnectTcp();
                String result = "TCP соединение разорвано";
                tcpResultText.setText(result);
            } else {
                String result = "Сначала подключитесь по TCP";
                tcpResultText.setText(result);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        flagMainActivity = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        flagMainActivity = false;
    }

    // кнопка для перехода на SecondActivity
    public void startSecondActivity(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    private void setBoolRosLaunch() {
        boolRosLaunch = true;
    }

    public static void viewResult() {
        String result = "Данные не верны";
        resultTextSsh.setText(result);
    }
}

