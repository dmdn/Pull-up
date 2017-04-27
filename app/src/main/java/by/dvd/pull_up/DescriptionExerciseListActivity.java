package by.dvd.pull_up;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DescriptionExerciseListActivity extends Activity {
    ListView lvMain;
    String[] p2_textArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_exercise_list);

        lvMain = (ListView) findViewById(R.id.lvMain);
        p2_textArr = getResources().getStringArray(R.array.p2_textArr);

        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        Intent intent = new Intent(DescriptionExerciseListActivity.this, DescriptionExerciseActivity.class);
                        intent.putExtra("position", Integer.toString(position));
                        startActivity(intent);
                    }
                };

        lvMain.setOnItemClickListener(itemClickListener);
    }

}
