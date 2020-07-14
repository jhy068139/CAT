package com.example.cat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cat.database.BroadcastD;
import com.example.cat.database.DatabaseHelper;
import com.example.cat.database.model.Note;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    CalendarView calMenu;
    ImageView itemMenu;
    ListView listView;

    ArrayList<Note> notesList = new ArrayList<>();

    StringBuffer buffer = new StringBuffer();

    public StringBuffer getBuffer() {
        return this.buffer;
    }


    private DatabaseHelper db;


    ListAdapter listAdapter;

    private void createNote(String tag, String note, String SettingTime) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(tag, note, SettingTime);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position

            notesList.add(0, n);


            // refreshing the list
            listAdapter.notifyDataSetChanged();


        }
    }

    /**
     * Toggling list and empty notes view
     */


    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        listAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        itemMenu = findViewById(R.id.itemMenu);

        listAdapter = new ListAdapter(notesList);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes());

        /*        createNote("hi","bye");*/

        listView.setAdapter(listAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteNote(position);
                Toast.makeText(getApplication(), "ast", Toast.LENGTH_SHORT).show();

                return false;

            }
        });

        itemMenu.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // 이벤트 처리

                                            AlertDialog.Builder aBuilder = new AlertDialog.Builder(MainActivity.this);
                                            View mView = getLayoutInflater().inflate(R.layout.setting_dialog, null);
                                            // 스피너 설정
                                            final Switch selSw = mView.findViewById(R.id.selSw);

                                            // editText 설정

                                            aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Text 값 받아서 로그 남기기


                                                    //닫기
                                                    dialog.dismiss();
                                                }
                                            });


// 취소 버튼 설정
                                            aBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();     //닫기
                                                }
                                            });
                                            //다이얼로그 호출
                                            aBuilder.setView(mView);
                                            AlertDialog dialog = aBuilder.create();
                                            dialog.show();
                                        }
                                    });



    calMenu =(CalendarView)findViewById(R.id.calMenu);




        calMenu.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트

    {
        @Override
        public void onSelectedDayChange (@NonNull CalendarView view,int year, int month,
        int dayOfMonth){
        final String date = year + "/" + (month + 1) + "/" + dayOfMonth;

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);


        // 스피너 설정
        final Spinner sp = (Spinner) mView.findViewById(R.id.sp);
        final TimePicker tp = mView.findViewById(R.id.tp);
        // 스피너 어댑터 설정
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.location, android.R.layout.simple_spinner_item);
        sp.setAdapter(yearAdapter);

        // editText 설정
        final EditText mesgase = mView.findViewById(R.id.mesgase);
        // 확인 버튼 설정
        aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Text 값 받아서 로그 남기기
                String selItem = (String) sp.getSelectedItem();
                String value = mesgase.getText().toString();

                createNote(selItem, value, date);
                dialog.dismiss();

                //닫기
            }
        });


// 취소 버튼 설정
        aBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        //다이얼로그 호출
        aBuilder.setView(mView);
        AlertDialog dialog = aBuilder.create();
        dialog.show();
    }
    });

        String location = "2920051500"; // 광주광역시 송정1동 값

        SimpleDateFormat day = new SimpleDateFormat( "yyyyMMdd"); // 오늘날짜 불러오기
        Date now = new Date();

        String today = day.format(now) + "06"; // 오늘날짜 yyyyMMdd + HH(06)

        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/LivingWthrIdxService/getUVIdx"); /*URL*/
            // urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=서비스키"); /*Service Key*/
            urlBuilder.append("?" + "serviceKey" + "=" + "0lN0C9DeARAXcU4qCfXFEPZoapkuQGQF957J6OF9lcfwk0MjstHyiMtbFSjWjUOrz8fIManY1NMA%2BbZWTA8PGQ%3D%3D"); /*공공데이터포털에서 받은 인증키*/
            urlBuilder.append("&" + "pageNo" + "=" + "1"); /*페이지번호*/
            urlBuilder.append("&" + "numOfRows" + "=" + "1"); /*한 페이지 결과 수*/
            urlBuilder.append("&" + "dataType" + "=" + "XML"); /*요청자료형식(XML/JSON)*/
            urlBuilder.append("&" + "areaNo" + "=" + location);
            urlBuilder.append("&" + "time" + "=" + today);
            urlBuilder.append("&");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(urlBuilder.toString());
            URLConnection t_connection = url.openConnection();
            t_connection.setReadTimeout(8000);
            InputStream is= t_connection.getInputStream(); //url위치로 입력스트림 연결 // 여기부터 문제 (링크 오류 아님, 불러오는 값의 형태가 잘못됐을 가능성이 높음)

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            StringBuffer buffer = new StringBuffer();

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ; // 첫번째 검색결과
                        else if(tag.equals("today")){
                            buffer.append("오늘자외선 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//today 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("tomorrow")){
                            buffer.append("내일자외선 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("theDayAfterTomorrow")){
                            buffer.append("모레자외선 :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= xpp.next();
            }



        } catch (IOException | XmlPullParserException | OutOfMemoryError e ) {

        }

        new AlarmHATT(getApplicationContext()).Alarm();

    }

    public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BroadcastD.class);

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE) + 1, calendar.get(Calendar.SECOND));

            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }
}

