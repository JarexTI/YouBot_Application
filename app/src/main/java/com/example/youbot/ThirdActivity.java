package com.example.youbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.youbot.TCP.TcpClient;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {

    // массивы для названия и углов команд манипулятора
    ArrayList<String> nameCommandManipulator = new ArrayList<>();
    public static ArrayList<String> valueAngleManipulator = new ArrayList<>();

    ArrayAdapter<String> adapterSpinner;
    Spinner spinner;
    TextView resultCommand;
    Button btnAddCommand;
    EditText nameCommand, inputQ1, inputQ2, inputQ3, inputQ4, inputQ5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        resultCommand = findViewById(R.id.result_command);

        addStartingNameAndCommand();

        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.test_list_item, nameCommandManipulator);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapterSpinner);
        btnAddCommand = findViewById(R.id.button_add_command);

        nameCommand = findViewById(R.id.input_name_command);
        inputQ1 = findViewById(R.id.input_q1);
        inputQ2 = findViewById(R.id.input_q2);
        inputQ3 = findViewById(R.id.input_q3);
        inputQ4 = findViewById(R.id.input_q4);
        inputQ5 = findViewById(R.id.input_q5);

        spinner.setPrompt("Команды манипулятора");

        spinner.setSelection(0);

        // выпадающий список
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.clientSocket != null) {
                    TcpClient.sendCommandsManipulator(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // кнопка для добалвения команды манипулятора
        btnAddCommand.setOnClickListener(v -> {
                if (inputQ1.getText().toString().equals("")) {
                    String result = "q1 поле пустое";
                    resultCommand.setText(result);
                } else if (inputQ2.getText().toString().equals("")) {
                    String result = "q2 поле пустое";
                    resultCommand.setText(result);
                } else if (inputQ3.getText().toString().equals("")) {
                    String result = "q3 поле пустое";
                    resultCommand.setText(result);
                } else if (inputQ4.getText().toString().equals("")) {
                    String result = "q4 поле пустое";
                    resultCommand.setText(result);
                } else if (inputQ5.getText().toString().equals("")) {
                    String result = "q5 поле пустое";
                    resultCommand.setText(result);
                } else {
                    String q1 = inputQ1.getText().toString();
                    String q2 = inputQ2.getText().toString();
                    String q3 = inputQ3.getText().toString();
                    String q4 = inputQ4.getText().toString();
                    String q5 = inputQ5.getText().toString();
                    if (Integer.parseInt(q1) < -170 || Integer.parseInt(q1) > 170) {
                        String result = "Ограничения q1: +/- 170 градусов";
                        resultCommand.setText(result);
                    } else if (Integer.parseInt(q2) < -150 || Integer.parseInt(q2) > 150) {
                        String result = "Ограничения q2: -65/+90 градусов";
                        resultCommand.setText(result);
                    } else if (Integer.parseInt(q3) < -139 || Integer.parseInt(q3) > 139) {
                        String result = "Ограничения q3: -151/+146 градусов";
                        resultCommand.setText(result);
                    } else if (Integer.parseInt(q4) < -196 || Integer.parseInt(q4) > 196) {
                        String result = "Ограничения q4: +/- 102 градуса";
                        resultCommand.setText(result);
                    } else if (Integer.parseInt(q5) < -169 || Integer.parseInt(q5) > 169) {
                        String result = "Ограничения q5: +/- 169 градусов";
                        resultCommand.setText(result);
                    } else {
                        String angles = "LUA_ManipDeg(0,"
                                + q1 + ","
                                + q2 + ","
                                + q3 + ","
                                + q4 + ","
                                + q5 + ")^^^\r\n";
                        String name = nameCommand.getText().toString();

                        nameCommandManipulator.add(name);
                        adapterSpinner.notifyDataSetChanged();
                        valueAngleManipulator.add(angles);
                    }
                }
        });
    }

    private void addStartingNameAndCommand() {
        nameCommandManipulator.add("Выберите команду");
        nameCommandManipulator.add("UP");
        nameCommandManipulator.add("Forward");
        nameCommandManipulator.add("Start take");
        nameCommandManipulator.add("take");
        nameCommandManipulator.add("look_near");

        // index 0: plug; 1: up; 2: forward; 3: Start take; 4: take;
        valueAngleManipulator.add("null");
        valueAngleManipulator.add("LUA_ManipDeg(0,160,61,-139,97,159)^^^\r\n");
        valueAngleManipulator.add("LUA_ManipDeg(0,168,27,-42,110,169)^^^\r\n");
        valueAngleManipulator.add("LUA_ManipDeg(0,170,145,-139,167,169)^^^\r\n");
        valueAngleManipulator.add("LUA_ManipDeg(0,168,131,-79,130,169)^^^\r\n");
        valueAngleManipulator.add("LUA_ManipDeg(0,170,46,-81,177,169)^^^\r\n");
    }

    public void goSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}