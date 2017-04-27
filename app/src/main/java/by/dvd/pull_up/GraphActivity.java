package by.dvd.pull_up;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;


public class GraphActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String exerciseNo = getIntent().getStringExtra("exerciseNo");
        int i = 0;

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        try {
            SQLiteOpenHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = "RESULTS";
            String[] columns = new String[] {"NUMBER", "DATE"};
            String selection = "EXERCISE = ?";
            String[] selectionArgs = new String[] {exerciseNo};
            String groupBy = null;
            String having = null;
            String orderBy = null;
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.moveToFirst()) {
                do {
                    String numberText = cursor.getString(0);
                    int numberNum = Integer.parseInt(numberText);
                    String dateText = cursor.getString(1);

                    entries.add(new BarEntry(numberNum, i++));
                    labels.add(dateText);

                } while (cursor.moveToNext());
                BarDataSet dataset = new BarDataSet(entries, getString(R.string.number_of_pulling_by_month));

                final BarChart chart = new BarChart(this);
                chart.setDescription(exerciseNo);
                setContentView(chart);

                dataset.setColors(ColorTemplate.PASTEL_COLORS);

                BarData data = new BarData(labels, dataset);
                chart.setData(data);

                chart.animateY(3000);//X axis animation

            } else {
                Toast toast = Toast.makeText(this, getString(R.string.no_saved_entries), Toast.LENGTH_LONG);
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
