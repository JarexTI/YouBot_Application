package com.example.youbot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youbot.TCP.TcpClient;
import com.longdo.mjpegviewer.MjpegView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    TextView resultManipulationPlatform;

    // иницилизация для манипулятора
    EditText inputManipulationId, inputManipulationQ1, inputManipulationQ2,
            inputManipulationQ3, inputManipulationQ4, inputManipulationQ5;
    Button btnSendManipulator;
    String manipulatorConfiguration;

    // инициализация кнопок для управления платформы
    ImageButton btnMoveBack, btnMoveForward, btnMoveLeft,
            btnMoveRight, btnMoveTurnLeft, btnMoveTurnRight;

    // терминал
    public static ArrayList<String> arrayListSecondTerminal = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static ListView listViewSecondTerminal;
    public static ArrayAdapter<String> adapterSecondTerminal;

    private static final String URL_VIDEO = "http://192.168.88.22:8080/stream?topic=/camera/rgb/image_raw&width=640&height=300&quality=50";

    Button btn_video_on, btn_video_off;

    public static boolean flagSecondActivity;

    MjpegView viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // присваиваем переменным id полей манипулятора
        inputManipulationId = findViewById(R.id.inputManipulation_id);
        inputManipulationQ1 = findViewById(R.id.inputManipulation_q1);
        inputManipulationQ2 = findViewById(R.id.inputManipulation_q2);
        inputManipulationQ3 = findViewById(R.id.inputManipulation_q3);
        inputManipulationQ4 = findViewById(R.id.inputManipulation_q4);
        inputManipulationQ5 = findViewById(R.id.inputManipulation_q5);
        btnSendManipulator = findViewById(R.id.btn_send_Manipulator);

        resultManipulationPlatform = findViewById(R.id.text_result_manipulation_platform);

        // присваиваем переменным id кнопок платформы
        btnMoveBack = findViewById(R.id.i_btn_goBack);
        btnMoveForward = findViewById(R.id.i_btn_goForward);
        btnMoveLeft = findViewById(R.id.i_btn_goLeft);
        btnMoveRight = findViewById(R.id.i_btn_goRight);
        btnMoveTurnLeft = findViewById(R.id.i_btn_goTurnLeft);
        btnMoveTurnRight = findViewById(R.id.i_btn_goTurnRight);

        // присваиваем переменным id для видеосвязи
        //image_video = findViewById(R.id.image_video);
        btn_video_on = findViewById(R.id.btn_video_on);
        btn_video_off = findViewById(R.id.btn_video_off);

        listViewSecondTerminal = findViewById(R.id.list_of_text_terminal_secondActivity);

        adapterSecondTerminal = new ArrayAdapter<>(this,
                android.R.layout.test_list_item,
                arrayListSecondTerminal);

        viewer = findViewById(R.id.mjpegview);

        // видеосвязь
        btn_video_on.setOnClickListener(v -> {
            viewer.setMode(MjpegView.MODE_FIT_WIDTH);
            viewer.setAdjustHeight(true);
            viewer.setSupportPinchZoomAndPan(true);
            viewer.setUrl(URL_VIDEO);
            viewer.startStream();
        });

        // отключение видеосязи
        btn_video_off.setOnClickListener(v -> viewer.stopStream());

        // отправка данных манипулятору
        btnSendManipulator.setOnClickListener(v -> {
            if (MainActivity.clientSocket != null) {
                if (inputManipulationId.getText().toString().equals("")) {
                    String result = "id поле пустое";
                    resultManipulationPlatform.setText(result);
                } else if (inputManipulationQ1.getText().toString().equals("")) {
                    String result = "q1 поле пустое";
                    resultManipulationPlatform.setText(result);
                } else if (inputManipulationQ2.getText().toString().equals("")) {
                    String result = "q2 поле пустое";
                    resultManipulationPlatform.setText(result);
                } else if (inputManipulationQ3.getText().toString().equals("")) {
                    String result = "q3 поле пустое";
                    resultManipulationPlatform.setText(result);
                } else if (inputManipulationQ4.getText().toString().equals("")) {
                    String result = "q4 поле пустое";
                    resultManipulationPlatform.setText(result);
                } else if (inputManipulationQ5.getText().toString().equals("")) {
                    String result = "q5 поле пустое";
                    resultManipulationPlatform.setText(result);
                } else {
                    String id = inputManipulationId.getText().toString();
                    String q1 = inputManipulationQ1.getText().toString();
                    String q2 = inputManipulationQ2.getText().toString();
                    String q3 = inputManipulationQ3.getText().toString();
                    String q4 = inputManipulationQ4.getText().toString();
                    String q5 = inputManipulationQ5.getText().toString();
                    if (Integer.parseInt(id) != 0 && Integer.parseInt(id) !=1) {
                        String result = "Такого id не существует";
                        resultManipulationPlatform.setText(result);
                    } else if (Integer.parseInt(q1) < -170 || Integer.parseInt(q1) > 170) {
                        String result = "Ограничения q1: +/- 170 градусов";
                        resultManipulationPlatform.setText(result);
                    } else if (Integer.parseInt(q2) < -150 || Integer.parseInt(q2) > 150) {
                        String result = "Ограничения q2: -65/+90 градусов";
                        resultManipulationPlatform.setText(result);
                    } else if (Integer.parseInt(q3) < -139 || Integer.parseInt(q3) > 139) {
                        String result = "Ограничения q3: -151/+146 градусов";
                        resultManipulationPlatform.setText(result);
                    } else if (Integer.parseInt(q4) < -196 || Integer.parseInt(q4) > 196) {
                        String result = "Ограничения q4: +/- 102 градуса";
                        resultManipulationPlatform.setText(result);
                    } else if (Integer.parseInt(q5) < -169 || Integer.parseInt(q5) > 169) {
                        String result = "Ограничения q5: +/- 169 градусов";
                        resultManipulationPlatform.setText(result);
                    } else {
                        manipulatorConfiguration = "LUA_ManipDeg(" + id + ","
                                + q1 + ","
                                + q2 + ","
                                + q3 + ","
                                + q4 + ","
                                + q5 + ")^^^\r\n";
                        TcpClient.sendManipulation(manipulatorConfiguration);
                    }
                }
            }
        });

        // движение платформы вперед
        btnMoveForward.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TcpClient.goForward();
                        break;
                    case MotionEvent.ACTION_UP:
                        TcpClient.stopYouBot();
                        break;
                }
            return false;
        });

        // движение платформы назад
        btnMoveBack.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TcpClient.goBack();
                        break;
                    case MotionEvent.ACTION_UP:
                        TcpClient.stopYouBot();
                        break;
                }
            return false;
        });

        // движение платформы влево
        btnMoveLeft.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TcpClient.goLeft();
                        break;
                    case MotionEvent.ACTION_UP:
                        TcpClient.stopYouBot();
                        break;
                }
            return false;
        });

        // движение платформы вправо
        btnMoveRight.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TcpClient.goRight();
                        break;
                    case MotionEvent.ACTION_UP:
                        TcpClient.stopYouBot();
                        break;
                }
            return false;
        });

        // поворот платформы влево
        btnMoveTurnLeft.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TcpClient.turnLeft();
                        break;
                    case MotionEvent.ACTION_UP:
                        TcpClient.stopYouBot();
                        break;
                }
            return false;
        });

        // поворот платформы вправо
        btnMoveTurnRight.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TcpClient.turnRight();
                        break;
                    case MotionEvent.ACTION_UP:
                        TcpClient.stopYouBot();
                        break;
                }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        flagSecondActivity = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        flagSecondActivity = false;
    }

    public void goHomeActivity(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goThirdActivity(View v) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
}
