package com.example.cat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Bundle;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;

import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import static java.net.HttpURLConnection.HTTP_OK;

public class MainActivity extends AppCompatActivity {

    CalendarView calMenu;

    TextView sel,tag,selname;
    Button testbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sel = findViewById(R.id.sel);
        tag = findViewById(R.id.tag);
        selname = findViewById(R.id.selname);
        ImageView itemMenu = findViewById(R.id.itemMenu);



        calMenu = (CalendarView)findViewById(R.id.calMenu);
        sel = (TextView)findViewById(R.id.sel);
        testbtn = findViewById(R.id.testBtn);

        calMenu.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                sel.setText(date); // 선택한 날짜로 설정

            }
        });

        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String location = "2920051500"; // 광주광역시 송정1동 값
                    String today = "2020070706"; // 오늘날짜 yyyymmddhh
                    StringBuffer buffer=new StringBuffer();

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

                    testbtn.setText(buffer.toString());

//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setConnectTimeout(15000);
//                    conn.setReadTimeout(10000);
//                    conn.setRequestMethod("POST");
//                    conn.setDoOutput(true); // 쓰기모드 지정
//                    conn.setDoInput(true); // 읽기모드 지정
//                    conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
//                    conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
//                    conn.setRequestProperty("Accept-Charset", "utf-8");
//                    conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//                    BufferedReader rd;
//                    if (conn.getResponseCode() == HTTP_OK) { // 원인
//                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                    } else {
//                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
//                    }
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = rd.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    rd.close();
//                    conn.disconnect();
//                    // System.out.println(sb.toString());
//                    testbtn.setText(sb.toString());
                } catch (IOException | XmlPullParserException | OutOfMemoryError e ) {
                    testbtn.setText(e.toString());
                }
            }
        });
    }
}
