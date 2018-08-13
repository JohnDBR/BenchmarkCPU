package com.john.benchmarkcpu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompareResults extends AppCompatActivity {

    @BindView(R.id.compare_button)Button compareButton;
    @BindView(R.id.score_a_text_input_edit_text) TextInputEditText tScoreA;
    @BindView(R.id.score_b_text_input_edit_text) TextInputEditText tScoreB;
    @BindView(R.id.score_a_text_input_layout) TextInputLayout lScoreA;
    @BindView(R.id.score_b_text_input_layout) TextInputLayout lScoreB;
    @BindView(R.id.secondary_results_layout) LinearLayoutCompat lResults;
    @BindView(R.id.result_text_view) TextView firstResultTextView;
    @BindView(R.id.result2_text_view) TextView secondResultTextView;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_results);
        ButterKnife.bind(this);
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tScoreA.setText(settings.getString("cpu_bench_score", "Puntaje: 0").split("\\:")[1]);
    }

    public void compareScores(View view) {
        String a = tScoreA.getText().toString().trim();
        String b = tScoreB.getText().toString().trim();
        if (a != null && b != null && !a.isEmpty() && !b.isEmpty()) {
            int intA = Integer.valueOf(a);
            int intB = Integer.valueOf(b);
            if (intA != 0 && intB != 0) {
                int abPercentage = getPercentage(intA, intB);
                int baPercentage = getPercentage(intB, intA);
                lResults.setVisibility(view.VISIBLE);
//                if (abPercentage < 0) {
//                    firstResultTextView.setText("El Score A es " + (abPercentage * -1) + "% inferior" +
//                            " que el score B con respecto al rendimiento del CPU.");
//                } else  {
//                    firstResultTextView.setText("El Score A es " + abPercentage + "% superior que el" +
//                            " score B con respecto al rendimiento del CPU.");
//                }
                showResult(abPercentage, firstResultTextView, "A", "B");
                showResult(baPercentage, secondResultTextView, "B", "A");

            }
        }
    }

    public int getPercentage(float a, float b) {
        float result = (a/b) * 100;
        Log.i("PERCENTAGE!!!", result + "");
        return (int) result;
    }

    public void showResult(int percentage, TextView result, String up, String down){
        if (percentage < 100) {
            result.setText("El Score " + up + " es " + percentage + "% equivalente" +
                    " al Score " + down ); //+ "con respecto al rendimiento del CPU."
        } else  {
            result.setText("El Score " + up + " es " + (percentage - 100) + "% superior que el" +
                    " score " + down ); //+ " con respecto al rendimiento del CPU."
        }
    }
}
