package com.th3md.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.th3md.trivia.data.AnswerListAsyncResponse;
import com.th3md.trivia.data.QuestionBank;
import com.th3md.trivia.model.Question;
import com.th3md.trivia.utils.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView counter;

    private int currentQues=0;

    private TextView score_;
    private Prefs prefs;
    private int score;


    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = new Prefs(MainActivity.this);
        question = findViewById(R.id.question);
        counter = findViewById(R.id.counter);

        Button trueButton = findViewById(R.id.true_button);
        Button falseButton = findViewById(R.id.false_button);

        ImageButton next = findViewById(R.id.next_button);
        ImageButton prev = findViewById(R.id.prev_button);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);

        score_ = findViewById(R.id.score);
        TextView highestScore = findViewById(R.id.highest_score);

        currentQues = prefs.getState();

        String smsg = MessageFormat.format("Highest Score: {0}", prefs.getHighScore());
        highestScore.setText(smsg);
        score_.setText(MessageFormat.format("Score: {0}", score));

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void Finished(ArrayList<Question> questionArrayList) {
                //Log.d("Response", "Finished: "+ questionArrayList);
                questionList=questionArrayList;
                question.setText(questionArrayList.get(currentQues).getAnswer());
                String msg=(currentQues+1)+"/"+questionList.size();
                counter.setText(msg);
            }
        });

    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score);
        prefs.setState(currentQues);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.prev_button:
                updateIndex(-1);
                break;
            case R.id.next_button:
                updateIndex(1);
                break;
            case R.id.true_button:
                checkAnswer(true);
                break;
            case R.id.false_button:
                checkAnswer(false);
                break;
        }
    }

    private void checkAnswer(boolean b) {
        Toast.makeText(MainActivity.this,
                (b == questionList.get(currentQues).isAnswerTrue()) ? "Correct !" : "Wrong !",
                Toast.LENGTH_SHORT)
                .show();
        if (b != questionList.get(currentQues).isAnswerTrue()){
            shakeAnimation();
            updateScore(-1);
        }else {
            fadeView();
            updateScore(1);
        }
        updateIndex(0);
    }

    private void updateScore(int i){
        score+=i;
        score=(score<0)?0:score;
        String smsg="Score: "+score;
        score_.setText(smsg);
    }

    private void updateIndex(int i){
        currentQues =((currentQues + i) % questionList.size())+(
                (currentQues-1<0 && i<0)? questionList.size(): 0);

        question.setText(questionList.get(currentQues).getAnswer());
        String messgae = (currentQues+1)+"/"+questionList.size();
        counter.setText(messgae);
    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cardView2);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(350);//ms
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                updateIndex(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation(){
        final CardView cardView = findViewById(R.id.cardView2);
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);

        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                updateIndex(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
