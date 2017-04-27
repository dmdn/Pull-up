package by.dvd.pull_up;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class EditDateActivity extends Activity {
    private ShareActionProvider shareActionProvider;
    public String messageText = "";

    LinearLayout llMainResult;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_date);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv2 = (TextView) findViewById(R.id.tv2);

        String dataNo = getIntent().getStringExtra("selectedDate");

        try {
            int i = 0;
            SQLiteOpenHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = "RESULTS";
            String[] columns = new String[] {"EXERCISE", "NUMBER"};
            String selection = "DATE = ?";
            String[] selectionArgs = new String[] {dataNo};
            String groupBy = null;
            String having = null;
            String orderBy = "DATE ASC";
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.moveToFirst()) {
                do {
                    String exerciseText = cursor.getString(0);
                    String numberText = cursor.getString(1);

                    llMainResult = (LinearLayout) findViewById(R.id.llMainResult);
                    ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView tvNew = new TextView(this);
                    tvNew.setText(++i + ". " + exerciseText + " - " + numberText + " " + getString(R.string.number_of_times_2));
                    tvNew.setTextSize(15);
                    llMainResult.addView(tvNew, lpView);

                    tv2.setText(dataNo);

                    messageText += dataNo + ": " + exerciseText + " = " + numberText + " " + getString(R.string.number_of_times_2);
                } while (cursor.moveToNext());

            } else {
                Toast toast = Toast.makeText(this, getString(R.string.no_saved_entries), Toast.LENGTH_SHORT);
                toast.show();
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, getString(R.string.database_unavailable), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_results, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent(messageText);

        MenuItem action_graph = menu.findItem(R.id.action_graph);
        action_graph.setVisible(false);
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

}
