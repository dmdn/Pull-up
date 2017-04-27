package by.dvd.pull_up;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashSet;
import java.util.Set;

public class DateListActivity extends Activity {

    Set<String> setDate;
    String[] arrDate = {};

    TextView countDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_list);

        setDate = new HashSet();
        ListView lvMain;
        countDate = (TextView) findViewById(R.id.countDate);

        try {
            SQLiteOpenHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = "RESULTS";
            String[] columns = new String[] {"DATE"};
            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String orderBy = null;
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.moveToFirst()) {
                do {
                    String dateText = cursor.getString(0);
                    setDate.add(dateText);

                } while (cursor.moveToNext());
                arrDate = setDate.toArray(new String[setDate.size()]);
                countDate.setText(getString(R.string.you_train) + " " + Integer.toString(arrDate.length) + " " + getString(R.string.days));

            } else {
                Toast toast = Toast.makeText(this, getString(R.string.no_saved_entries), Toast.LENGTH_SHORT);
                toast.show();
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, getString(R.string.database_unavailable), Toast.LENGTH_SHORT);
            toast.show();
        }

        lvMain = (ListView) findViewById(R.id.lvMain);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, arrDate);

        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selectedDate = arrDate[position];
                Intent intent = new Intent(DateListActivity.this, EditDateActivity.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
            }
        });
    }
}
