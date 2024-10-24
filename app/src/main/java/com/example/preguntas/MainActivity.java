package com.example.preguntas;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mBackButton;
    private TextView mQuestionTextView;
    private TextView mContador;
    private TextView tvTimer;
    private int cont=0;
    private int secondsElapsed = 0;
    private CountDownTimer countDownTimer;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true,false),
            new Question(R.string.question_oceans, true,false),
            new Question(R.string.question_mideast, false,false),
            new Question(R.string.question_africa, false,false),
            new Question(R.string.question_americas, true,false),
            new Question(R.string.question_asia, true,false),
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvTimer = findViewById(R.id.tvTimer);
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                secondsElapsed++;
                int hours = secondsElapsed / 3600;
                int minutes = (secondsElapsed % 3600) / 60;
                int seconds = secondsElapsed % 60;

                // Mostrar el tiempo transcurrido en formato hh:mm:ss
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                tvTimer.setText(time);
            }

            @Override
            public void onFinish() {
                // No se usará, ya que el contador seguirá funcionando indefinidamente
            }
        }.start();  // Iniciamos el contador
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        };
        mContador = (TextView) findViewById(R.id.contador);
        mContador.setText(String.valueOf(contarRespondidas(mQuestionBank))+"/"+mQuestionBank.length);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        updateQuestion();
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mBackButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
                updateQuestion();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }






    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (!(mQuestionBank[mCurrentIndex].isAnswered())){
           encederBotones();
        }

    }

    private int contarRespondidas(Question[] listado){
        int num=0;
        for (Question pregunta:listado) {
            if (pregunta.isAnswered()){
                num++;
            }

        }
        return num;
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        mQuestionBank[mCurrentIndex].setAnswered(true);
        mContador.setText(String.valueOf(contarRespondidas(mQuestionBank))+"/"+mQuestionBank.length);
        apagarBotones();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            cont++;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
        if(contarRespondidas(mQuestionBank)==mQuestionBank.length){
            if (countDownTimer != null) {
                countDownTimer.cancel();  // Detener el temporizador


            }
            mContador.setText("Has acertado "+cont+" preguntas");
        }
    }

    private void apagarBotones(){
        mFalseButton.setEnabled(false);
        mTrueButton.setEnabled(false);
    }
    private void encederBotones(){
        mFalseButton.setEnabled(true);
        mTrueButton.setEnabled(true);
    }




    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}