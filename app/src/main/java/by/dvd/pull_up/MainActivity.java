package by.dvd.pull_up;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    String[] p2_textArr;

    Button btnAdd, btnDel, btnDelAll;

    LinearLayout llMain;
    TextView tvDate, tv1;

    List<EditText> listNumber;
    List<String> listExercise;

    int iNum = 1;
    String exerciseNo;
    int selectExercise_i = 0;

    int nullText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p2_textArr = getResources().getStringArray(R.array.p2_textArr);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);
        btnDelAll = (Button) findViewById(R.id.btnDelAll);
        btnDelAll.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tvDate);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd");
        tvDate.setText(dateFormat.format(date));

        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText(getString(R.string.choose_exercises));

        llMain = (LinearLayout) findViewById(R.id.llMain);

        listNumber = new ArrayList();
        listExercise = new ArrayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case  R.id.action_db_add:

                for (EditText n : listNumber) {
                    if (n.getText().toString().equals("")){
                        nullText++;
                    }
                }
                if (nullText == 0) {
                    SQLiteOpenHelper dbHelper = new DBHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues cValues = new ContentValues();

                    for (int i=0; i<listNumber.size(); i++) {

                        EditText etNumber = listNumber.get(i);
                        String etNumberText = etNumber.getText().toString();
                        cValues.put("NUMBER", etNumberText);

                        String exerciseNoText = listExercise.get(i);
                        cValues.put("EXERCISE", exerciseNoText);

                        String tvDateText = (String) tvDate.getText();
                        cValues.put("DATE", tvDateText);

                        db.insert("RESULTS", null, cValues);
                    }
                    llMain.removeAllViews();
                    Toast.makeText(MainActivity.this, getString(R.string.result_saved), Toast.LENGTH_LONG).show();

                    db.close();
                    dbHelper.close();

                    nullText = 0;
                    iNum = 1;

                } else {
                    nullText = 0;
                    Toast.makeText(MainActivity.this, getString(R.string.fill_line), Toast.LENGTH_LONG).show();
                }
                break;

            case  R.id.action_oll_results:
                Intent intent1 = new Intent(this, AllResultsActivity.class);
                startActivity(intent1);
                break;
            case  R.id.action_date_results:
                Intent intent2 = new Intent(this, DateListActivity.class);
                startActivity(intent2);
                break;
            case  R.id.action_exercise_results:
                Intent intent3 = new Intent(this, ExerciseResultsActivity.class);
                startActivity(intent3);
                break;
            case  R.id.action_description_exercise:
                Intent intent4 = new Intent(this, DescriptionExerciseListActivity.class);
                startActivity(intent4);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:

                LinearLayout linearLayoutNew = new LinearLayout(this);
                linearLayoutNew.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams linLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llMain.addView(linearLayoutNew, linLayoutParams);
                ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView tvNewINum = new TextView(this);
                tvNewINum.setText(Integer.toString(iNum++) + ". ");
                tvNewINum.setTextSize(16);
                linearLayoutNew.addView(tvNewINum, lpView);

                EditText etNew = new EditText(this);
                etNew.setTextSize(16);
                etNew.setEms(3);
                etNew.requestFocus();
                etNew.setInputType(InputType.TYPE_CLASS_NUMBER);
                linearLayoutNew.addView(etNew, lpView);
                listNumber.add(etNew);

                TextView tvNew1 = new TextView(this);
                tvNew1.setText(getString(R.string.number_of_times));
                tvNew1.setTextSize(16);
                linearLayoutNew.addView(tvNew1, lpView);

                final Spinner spinnerNew = new Spinner(this);
                ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.p2_textArr, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNew.setAdapter(adapter);
                spinnerNew.setSelection(selectExercise_i);

                spinnerNew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                        exerciseNo = spinnerNew.getSelectedItem().toString();
                        selectExercise_i = spinnerNew.getSelectedItemPosition();
                        listExercise.add(exerciseNo);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                linearLayoutNew.addView(spinnerNew, lpView);

                break;

            case R.id.btnDel:
                int numChildLiMain = llMain.getChildCount();
                llMain.removeView(llMain.getChildAt(numChildLiMain - 1));

                if (iNum >= 1) { iNum--; } else { iNum = 1; }
                break;

            case R.id.btnDelAll:
                llMain.removeAllViews();
                iNum = 1;
                break;
        }

    }
}
