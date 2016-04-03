package com.juggleclouds.dicegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivDice;
    TextView tvScore;
    TextView tvTurn;

    Button bRoll, bReset, bHold;

    int yScore = 0;
    int yTotalScore = 0;
    int cScore = 0;
    int cTotalScore = 0;

    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bRoll = (Button) findViewById(R.id.roll);
        bHold = (Button) findViewById(R.id.hold);
        bReset = (Button) findViewById(R.id.reset);
        bRoll.setOnClickListener(this);
        bHold.setOnClickListener(this);
        bReset.setOnClickListener(this);
        tvScore = (TextView) findViewById(R.id.score);
        tvTurn = (TextView) findViewById(R.id.turn);
        ivDice = (ImageView) findViewById(R.id.dice);
    }

    int getDiceFace(int i) {
        switch (i) {
            case 1:
                return R.drawable.dice1;
            case 2:
                return R.drawable.dice2;
            case 3:
                return R.drawable.dice3;
            case 4:
                return R.drawable.dice4;
            case 5:
                return R.drawable.dice5;
            case 6:
                return R.drawable.dice6;
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roll:
                int roll = random.nextInt(6) + 1;
                Log.i("rollp", roll + "");
                ivDice.setImageResource(getDiceFace(roll));
                if (roll == 1) {
                    yScore = 0;
                    updateScore();
                    computerTurn();
                } else {
                    yScore += roll;
                    updateScore();
                }
                break;
            case R.id.hold:
                yTotalScore += yScore;
                yScore = 0;
                updateScore();
                computerTurn();
                break;
            case R.id.reset:
                yScore = 0;
                yTotalScore = 0;
                cScore = 0;
                cTotalScore = 0;
                updateScore();
                bRoll.setEnabled(true);
                bHold.setEnabled(true);
                tvTurn.setText("Your Turn");
                break;
        }
    }

    void updateScore() {
        if (yTotalScore + yScore >= 100) {
            tvTurn.setText("You won!");
            bRoll.setEnabled(false);
            bHold.setEnabled(false);
        } else if (cTotalScore + cScore >= 100) {
            tvTurn.setText("Computer won");
            bRoll.setEnabled(false);
            bHold.setEnabled(false);
        }
        tvScore.setText("Your Score : " + (yScore + yTotalScore) + " Computer Score : " + (cScore + cTotalScore));
    }

    android.os.Handler handler = new android.os.Handler();

    void computerTurn() {
        bHold.setEnabled(false);
        bRoll.setEnabled(false);
        bReset.setEnabled(false);
        tvTurn.setText("Computer's Turn");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cTurn();
            }
        }, 1000);
    }

    boolean isGoodScore() {
        if (yTotalScore > cTotalScore) {
            if (yTotalScore - cTotalScore > 10)
                return cScore > 15;
            else
                return cScore > (yTotalScore - cTotalScore) / 2;
        } else
            return cScore > 10;
    }

    void cTurn() {
        int roll = random.nextInt(5) + 1;
        Log.i("rollc", roll + "");
        ivDice.setImageResource(getDiceFace(roll));
        if (roll == 1)
            cScore = 0;
        else
            cScore += roll;
        if (cScore != 0 && isGoodScore()) {
            cTotalScore += cScore;
            cScore = 0;
        }
        updateScore();
        if (cScore != 0)
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cTurn();
                }
            }, 1000);
        else {
            bHold.setEnabled(true);
            bRoll.setEnabled(true);
            bReset.setEnabled(true);
            tvTurn.setText("Your Turn");
        }
    }
}
