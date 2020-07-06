package com.example.cat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageView itemMenu;
    CalendarView calMenu;
    TextView sel,tag,selname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sel = findViewById(R.id.sel);
        tag = findViewById(R.id.tag);
        selname = findViewById(R.id.selname);
        itemMenu = findViewById(R.id.itemMenu);

        itemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(
                        getApplicationContext(), // 현재 화면의 제어권자
                        v); // anchor : 팝업을 띄울 기준될 위젯
                getMenuInflater().inflate(R.menu.item, p.getMenu());
                // 이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getApplicationContext(),
                                "팝업메뉴 이벤트 처리 - "
                                        + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });


                p.show(); // 메뉴를 띄우기

            }
        });


        calMenu = findViewById(R.id.calMenu);

        calMenu.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                //클래스 생성
                CustomDialog customDialog = new CustomDialog(MainActivity.this);
                //다이얼로그 호출
                customDialog.callFunction(selname,sel,tag);
          }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.item, menu);

        return true;
    }

    public void dd(View v) {
        Toast.makeText(getApplicationContext(), "dd", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        Toast toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);

        switch(item.getItemId())
        {
            case R.id.menu1:
                toast.setText("Select Menu1");
                break;
            case R.id.menu2:
                toast.setText("Select Menu2");
                break;
        }

        toast.show();

        return super.onOptionsItemSelected(item);
    }
}