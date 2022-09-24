package com.example.youbot.TCP;

import com.example.youbot.MainActivity;
import com.example.youbot.SecondActivity;
import com.example.youbot.ThirdActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TcpClient extends MainActivity implements Runnable {

    String ipTcp;
    int portTcp;

    private static BufferedReader in;
    private static BufferedWriter out;
    private static boolean whileTcpBool;

    public TcpClient(String ipTcp, int portTcp) {
        this.ipTcp = ipTcp;
        this.portTcp = portTcp;
    }

    // TCP подключение
    @Override
    public void run() {
        try {
            clientSocket = new Socket(ipTcp, portTcp);
        } catch (IOException e) {
            runOnUiThread(() -> {
                String result = "Данные не верны";
                MainActivity.tcpResultText.setText(result);
            });
            MainActivity.clientSocket = null;
            return;
        }

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTrueWhileTcpBool();

        runOnUiThread(() -> {
            String result = "TCP соединение установлено";
            MainActivity.tcpResultText.setText(result);
        });

        while (whileTcpBool) {
            String textInput;
            try {
                textInput = in.readLine();
                System.out.println("TCP" + textInput);

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

            if (MainActivity.flagMainActivity) {
                // отображаем элементы в списке "listViewTerminal"
                runOnUiThread(() -> MainActivity.listViewTerminal.setAdapter(MainActivity.adapterTerminal));
            } else if (SecondActivity.flagSecondActivity) {
                // отображаем элементы в списке "listViewTerminal"
                runOnUiThread(() -> SecondActivity.listViewSecondTerminal.setAdapter(SecondActivity.adapterSecondTerminal));
            }
        }
    }

    // отключение по TCP
    public static void disconnectTcp() {
        setFalseWhileTcpBool();
        try {
            MainActivity.clientSocket.close();
            MainActivity.clientSocket = null;
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // манипулятор
    public static void sendManipulation(String manipulator) {
        try {
            out.write(manipulator);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // движение назад
    public static void goBack() {
        try {
            out.write("LUA_Base(-0.2,0,0)^^^\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // движение вперед
    public static void goLeft() {
        try {
            out.write("LUA_Base(0,0.2,0)^^^\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // движение вправо
    public static void goRight() {
        try {
            out.write("LUA_Base(0,-0.2,0)^^^\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // движение вперед
    public static void goForward() {
        try {
            out.write("LUA_Base(0.2,0,0)^^^\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // робот стоит на месте
    public static void stopYouBot() {
        try {
            out.write("LUA_Base(0,0,0)^^^\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // поворот платформы влево
    public static void turnLeft() {
        try {
            out.write("LUA_Base(0,0,0.4)^^^\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // поворот платформы вправо
    public static void turnRight() {
        try {
            out.write("LUA_Base(0,0,-0.4)^^^\r\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendCommandsManipulator(int position) {
        try {
            String up = ThirdActivity.valueAngleManipulator.get(position);
            out.write(up);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setTrueWhileTcpBool() {
        whileTcpBool = true;
    }

    private static void setFalseWhileTcpBool() {
        whileTcpBool = false;
    }
}
