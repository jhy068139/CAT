package com.example.cat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cat.database.DatabaseHelper;
import com.example.cat.database.model.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    CalendarView calMenu;

    TextView sel,tag,selname;


    ImageView itemMenu;
    ListView listView;

    ArrayList<Note> notesList = new ArrayList<>();


    private DatabaseHelper db;


    ListAdapter listAdapter;

    private void createNote(String tag,String note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(tag, note);

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
                Toast.makeText(getApplication(), "ast",Toast.LENGTH_SHORT).show();

                return false;

            }
        });

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
                        if (item.getTitle().toString().equals("설정") ){
                            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                            startActivity(intent);

                        }
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


        calMenu = (CalendarView) findViewById(R.id.calMenu);

        sel= findViewById(R.id.sel);


        calMenu.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                sel.setText(date); // 선택한 날짜로 설정
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

                        createNote(selItem,value);
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

//
//        testbtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            try {
//                                StringBuffer buffer=new StringBuffer();
//
//                                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/LivingWthrIdxService/getUVIdx"); /*URL*/
//                                // urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=서비스키"); /*Service Key*/
//                                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode("0lN0C9DeARAXcU4qCfXFEPZoapkuQGQF957J6OF9lcfwk0MjstHyiMtbFSjWjUOrz8fIManY1NMA%2BbZWTA8PGQ%3D%3D", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
//                                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
//                                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
//                                urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)*/
//                                urlBuilder.append("&" + URLEncoder.encode("areaNo", "UTF-8") + "=" + URLEncoder.encode("2920051500", "UTF-8")); /*광주광역시 송정1동*/
//                                urlBuilder.append("&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode("2020070606", "UTF-8")); /*2020년 7월 6일 6시 발표*/
//
//                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                                StrictMode.setThreadPolicy(policy);
//
//                                URL url = new URL(urlBuilder.toString());
//                                URLConnection t_connection = url.openConnection();
//                                t_connection.setReadTimeout(8000);
//                                InputStream is= t_connection.getInputStream(); //url위치로 입력스트림 연결 // 여기부터 문제 (링크 오류 아님, 불러오는 값의 형태가 잘못됐을 가능성이 높음)
//
//                                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
//                                XmlPullParser xpp= factory.newPullParser();
//                                xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
//
//                                String tag;
//
//                                xpp.next();
//                                int eventType= xpp.getEventType();
//                                while( eventType != XmlPullParser.END_DOCUMENT ){
//                                    switch( eventType ){
//                                        case XmlPullParser.START_DOCUMENT:
//                                            break;
//
//                                        case XmlPullParser.START_TAG:
//                                            tag= xpp.getName();//태그 이름 얻어오기
//
//                                            if(tag.equals("item")) ;// 첫번째 검색결과
//                                            else if(tag.equals("today")){
//                                                buffer.append("오늘자외선 : ");
//                                                xpp.next();
//                                                buffer.append(xpp.getText());//today 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                                                buffer.append("\n"); //줄바꿈 문자 추가
//                                            }
//                                            else if(tag.equals("tomorrow")){
//                                                buffer.append("내일자외선 : ");
//                                                xpp.next();
//                                                buffer.append(xpp.getText());
//                                                buffer.append("\n");
//                                            }
//                                            else if(tag.equals("theDayAfterTomorrow")){
//                                                buffer.append("모레자외선 :");
//                                                xpp.next();
//                                                buffer.append(xpp.getText());
//                                                buffer.append("\n");
//                                            }
//                                            break;
//
//                                        case XmlPullParser.TEXT:
//                                            break;
//
//                                        case XmlPullParser.END_TAG:
//                                            tag= xpp.getName(); //태그 이름 얻어오기
//
//                                            if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
//
//                                            break;
//                                    }
//
//                                    eventType= xpp.next();
//                                }
//
//                                testbtn.setText(buffer.toString());
//
//
////                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                    conn.setConnectTimeout(15000);
////                    conn.setReadTimeout(10000);
////                    conn.setRequestMethod("POST");
////                    conn.setDoOutput(true); // 쓰기모드 지정
////                    conn.setDoInput(true); // 읽기모드 지정
////                    conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
////                    conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
////                    conn.setRequestProperty("Accept-Charset", "utf-8");
////                    conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
////                    BufferedReader rd;
////                    if (conn.getResponseCode() == HTTP_OK) { // 원인
////                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
////                    } else {
////                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
////                    }
////                    StringBuilder sb = new StringBuilder();
////                    String line;
////                    while ((line = rd.readLine()) != null) {
////                        sb.append(line);
////                    }
////                    rd.close();
////                    conn.disconnect();
////                    // System.out.println(sb.toString());
////                    testbtn.setText(sb.toString());
//                } catch (IOException | XmlPullParserException | OutOfMemoryError e ) {
//                    testbtn.setText(e.toString());
//                }
//            }
//        });
//    }
//
//
//
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater inflater = getMenuInflater();
//
//        inflater.inflate(R.menu.item, menu);
//
//        return true;
//    }
//
//    public void dd(View v) {
//        Toast.makeText(getApplicationContext(), "dd", Toast.LENGTH_SHORT).show();
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item)
//    {
//        Toast toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);
//
//        switch(item.getItemId())
//        {
//            case R.id.menu1:
//                toast.setText("Select Menu1");
//                break;
//            case R.id.menu2:
//                toast.setText("Select Menu2");
//                break;
//        }
//
//        toast.show();
//
//        return super.onOptionsItemSelected(item);
//    }
    }

}

