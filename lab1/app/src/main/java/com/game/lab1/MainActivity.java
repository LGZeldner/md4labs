package com.game.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.game.lab1.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView yourPoints;     /* ваши очки */
    TextView opponentPoints; /* очки противника */
    TextView whoTurn;        /* чей сейчас ход */
    TextView cube1;          /* кость 1 */
    TextView cube2;          /* кость 2 */
    TextView btn;            /* кнопка броска */
    int number1;             /* результат 1-й кости */
    int number2;             /* результат 2-й кости */
    public Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yourPoints = findViewById(R.id.yourPoints);
        opponentPoints = findViewById(R.id.opponentPoints);
        whoTurn = findViewById(R.id.whoTurn);

        cube1 = findViewById(R.id.cube1);
        cube2 = findViewById(R.id.cube2);

        btn = findViewById(R.id.button);
    }

    public void onClick(View view) { /* по клику на кнопку броска */
        while (playerTurn()) { /* первый ход за игроком */
            return;
        }

        if ((Integer.parseInt(yourPoints.getText().toString()) > 99) && (Integer.parseInt(opponentPoints.getText().toString()) < Integer.parseInt(yourPoints.getText().toString()))) {
            goToYouWinActivity();
            clearAll();
            return;
        }

        findViewById(R.id.button).setEnabled(false);
        opponentTurn(); /* ход противника */
        findViewById(R.id.button).setEnabled(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if ((Integer.parseInt(opponentPoints.getText().toString()) > 99) && (Integer.parseInt(opponentPoints.getText().toString()) > Integer.parseInt(yourPoints.getText().toString()))) {
                    goToPlayerLoseActivity();
                    clearAll();
                    return;
                }
            }
        }, 2000);
    }

    public void goToPlayerLoseActivity() { /* переходим на проигрыш */
        Intent intent = new Intent(this, playerLoseActivity.class);
        startActivity(intent);
    }

    public void goToYouWinActivity() { /* переходим на выигрыш */
        Intent intent = new Intent(this, playerWinActivity.class);
        startActivity(intent);
    }

    public void clearAll() { /* сброс счета */
        whoTurn.setText("");
        opponentPoints.setText("0");
        yourPoints.setText("0");
        cube1.setText("0");
        cube2.setText("0");
    }

    public void opponentTurn() { /* ход противника */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                number1 = random.nextInt(5) + 1;
                number2 = random.nextInt(5) + 1;
                int p2 = Integer.parseInt(opponentPoints.getText().toString());

                cube1.setText(String.valueOf(number1));
                cube2.setText(String.valueOf(number2));

                whoTurn.setText("Ход противника:");
                p2 = p2 + number1 + number2;
                opponentPoints.setText(String.valueOf(p2));

                if (number1 == number2) {
                    opponentTurn();
                }
            }
        }, 1000);
    }

    public boolean playerTurn() { /* ход игрока */
        number1 = random.nextInt(5) + 1;
        number2 = random.nextInt(5) + 1;
        int p1 = Integer.parseInt(yourPoints.getText().toString());

        cube1.setText(String.valueOf(number1));
        cube2.setText(String.valueOf(number2));

        whoTurn.setText("Ваш ход:");
        p1 = p1 + number1 + number2;
        yourPoints.setText(String.valueOf(p1));

        return number1 == number2;

    }

}
