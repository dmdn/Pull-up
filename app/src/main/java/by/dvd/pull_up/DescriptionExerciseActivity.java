package by.dvd.pull_up;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.ImageView;
import android.widget.TextView;

public class DescriptionExerciseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_exercise);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String srt_iEx = intent.getStringExtra("position");

        Integer iEx = new Integer(srt_iEx);

        ImageView imgView = (ImageView) findViewById(R.id.imgView);

        switch (iEx) {
            case 0:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_1, null));
                break;
            case 1:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_2, null));
                break;
            case 2:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_3, null));
                break;
            case 3:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_4, null));
                break;
            case 4:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_5, null));;
                break;
            case 5:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_6, null));
                break;
            case 6:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_7, null));
                break;
            case 7:
                imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pic_ex_8, null));
                break;
        }

        String[] p2_textArr = getResources().getStringArray(R.array.p2_textArr);
        String p2_textArrI = p2_textArr[iEx];
        TextView p2_text = (TextView) findViewById(R.id.p2_text);
        p2_text.setText(p2_textArrI);
        p2_text.setTextColor(Color.RED);

        String[] p3_textArr = getResources().getStringArray(R.array.p3_textArr);
        String p3_textArrI = p3_textArr[iEx];
        TextView p3_text = (TextView) findViewById(R.id.p3_text);
        p3_text.setText(p3_textArrI);

        String[] p4_textArr = getResources().getStringArray(R.array.p4_textArr);
        String p4_textArrI = p4_textArr[iEx];
        TextView p4_text = (TextView) findViewById(R.id.p4_text);
        p4_text.setText(p4_textArrI);
    }

 }
