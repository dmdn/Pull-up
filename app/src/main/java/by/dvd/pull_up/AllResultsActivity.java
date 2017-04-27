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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class AllResultsActivity extends Activity implements View.OnClickListener {

    private ShareActionProvider shareActionProvider;

    Button btnDel;
    EditText etId;

    public LinearLayout llMainResult;
    public String messageText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_results);

        etId = (EditText) findViewById(R.id.etId);

        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        showDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_results, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent(messageText);

        MenuItem action_graph = menu.findItem(R.id.action_graph);
        action_graph.setVisible(false);

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
            case R.id.action_delete_DB:
                SQLiteOpenHelper dbHelper = new DBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("RESULTS", null, null);
                db.close();
                llMainResult.removeAllViews();
                Toast.makeText(AllResultsActivity.this, getString(R.string.all_posts_deleted), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String db_id = etId.getText().toString();

        SQLiteOpenHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnDel:
                if (db_id.equalsIgnoreCase("")){
                    Toast.makeText(AllResultsActivity.this, getString(R.string.fill_row_to_delete), Toast.LENGTH_SHORT).show();
                    break;
                }
                db.delete("RESULTS", "_id" + "=" + db_id, null);
                db.close();
                llMainResult.removeAllViews();
                showDB();
                etId.setText("");
                Toast.makeText(AllResultsActivity.this, getString(R.string.string_n) + " " + db_id + " " + getString(R.string.removed_from_database), Toast.LENGTH_SHORT).show();
        }
    }

    public void showDB(){
        try {
            SQLiteOpenHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = "RESULTS";
            String[] columns = null;
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String orderBy = null;
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.moveToFirst()) {

                llMainResult = (LinearLayout) findViewById(R.id.llMainResult);
                ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                do {
                    int iD = cursor.getInt(0);

                    String idCursor = Integer.toString(iD);
                    String numberText = cursor.getString(1);
                    String exerciseText = cursor.getString(2);
                    String dateText = cursor.getString(3);

                    TextView tvNew = new TextView(this);
                    tvNew.setText(idCursor + ". " + dateText + " - " + exerciseText + ": " + numberText + getString(R.string.number_of_times_2));
                    tvNew.setTextSize(15);
                    llMainResult.addView(tvNew, lpView);

                    messageText += dateText + " - " + exerciseText + ": " + numberText + " " + getString(R.string.number_of_times_2);
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

}
