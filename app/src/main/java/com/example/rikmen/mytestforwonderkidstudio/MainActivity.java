package com.example.rikmen.mytestforwonderkidstudio;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.SQLException;

public class MainActivity extends ActionBarActivity {

    ListView mList;
    TextView header;
    DatabaseHelper sqlHelper;
    Cursor userCursor;
    Button addBtn;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = (TextView)findViewById(R.id.header);
        mList = (ListView)findViewById(R.id.list);
        addBtn = (Button) findViewById(R.id.addBt);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        sqlHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        try {
            sqlHelper.open();
            userCursor = sqlHelper.database.rawQuery("select * from " + DatabaseHelper.TABLE, null);
            String[] headers = new String[]{DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_YEAR};
            userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                    userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
            mList.setAdapter(userAdapter);
        }
        catch (SQLException ex){}
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        sqlHelper.database.close();
        userCursor.close();
    }

}
