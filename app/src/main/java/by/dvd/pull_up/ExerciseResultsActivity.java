package by.dvd.pull_up;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ExerciseResultsActivity extends Activity implements View.OnClickListener {

    private ShareActionProvider shareActionProvider;
    public String messageText = "";

    LinearLayout llMainResult;
    TextView tvExercise;
    Button btnExercise, btnClear;

    String exerciseNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_results);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        llMainResult = (LinearLayout) findViewById(R.id.llMainResult);
        tvExercise = (TextView) findViewById(R.id.tvExercise);
        btnExercise = (Button) findViewById(R.id.btnExercise);
        btnExercise.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        final Spinner spinner = (Spinner)findViewById(R.id.spinExercise);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.p2_textArr, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.p2_textArr);
                exerciseNo = choose[selectedItemPosition];

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_results, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent(messageText);

        MenuItem action_delete_DB = menu.findItem(R.id.action_delete_DB);
        action_delete_DB.setVisible(false);

        return true;
    }

    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_graph:

                Intent intent = new Intent(this, GraphActivity.class);
                intent.putExtra("exerciseNo", exerciseNo);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnExercise:
                try {
                    int i = 0;
                    SQLiteOpenHelper dbHelper = new DBHelper(this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    String table = "RESULTS";
                    String[] columns = new String[] {"NUMBER", "DATE"};
                    String selection = "EXERCISE = ?";
                    String[] selectionArgs = new String[] {exerciseNo};
                    String groupBy = null;
                    String having = null;
                    String orderBy = "DATE ASC";
                    Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

                    if (cursor.moveToFirst()) {
                        do {
                            String numberText = cursor.getString(0);
                            String dateText = cursor.getString(1);

                            ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            TextView tvNew = new TextView(this);
                            tvNew.setText(++i + ". " + dateText + " - " + numberText + " " + getString(R.string.number_of_times_3));
                            tvNew.setTextSize(15);
                            llMainResult.addView(tvNew, lpView);

                            tvExercise.setText(getString(R.string.pulling) + " " + exerciseNo);

                            messageText += dateText + ": " + exerciseNo + " = " + numberText + " " + getString(R.string.number_of_times_2);
                        } while (cursor.moveToNext());

                    } else {
                        Toast.makeText(this, getString(R.string.no_saved_entries), Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    db.close();
                } catch(SQLiteException e) {
                    Toast toast = Toast.makeText(this, getString(R.string.database_unavailable), Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case R.id.btnClear:
                llMainResult.removeAllViews();
                tvExercise.setText("");
                Toast.makeText(ExerciseResultsActivity.this, getString(R.string.screen_cleared), Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
