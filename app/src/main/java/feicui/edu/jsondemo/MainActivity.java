package feicui.edu.jsondemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int MESSAGE=1;
    private TextView name1;
    private TextView name2;
    private TextView age1;
    private TextView age2;

    private TextView className;
    private String mclassName;

    private static final String urlPath = "http://192.168.1.147:8080/index2.jsp";

    private List<Students> mList=null;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE:
                    name1.setText(mList.get(0).name);
                    age1.setText(String.valueOf(mList.get(0).age));
                    name2.setText(mList.get(1).name);
                    age2.setText(String.valueOf(mList.get(1).age));
                    className.setText(mclassName);
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name1= (TextView) findViewById(R.id.name1);
        name2= (TextView) findViewById(R.id.name2);
        age1= (TextView) findViewById(R.id.age1);
        age2= (TextView) findViewById(R.id.age2);
        className= (TextView) findViewById(R.id.tv_class);
        new Thread(){
            @Override
            public void run() {
                super.run();
                String json=getJsonData(urlPath);
                mList=parseJson(json);
                mHandler.sendEmptyMessage(MESSAGE);
            }
        }.start();
    }

    private List<Students> parseJson(String json) {
        List<Students> list=new ArrayList<>();

        try {
            JSONObject students=new JSONObject(json);
            mclassName= (String) students.get("classname");

            JSONArray studentArray= (JSONArray) students.get("students");
            for (int i=0;i<studentArray.length();i++){
                JSONObject jsonObject= (JSONObject) studentArray.get(i);

                Students s=new Students();
                s.name= (String) jsonObject.get("name");
                Log.d("name", s.name);

                s.age=jsonObject.getInt("age");
                Log.d("age",String.valueOf(s.age));
                list.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String getJsonData(String urlPath) {
        String jsonData=null;

        try {
            URL url=new URL(urlPath);

            HttpURLConnection connection= (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();



            ByteArrayOutputStream out=new ByteArrayOutputStream();

            byte[] b=new byte[1024];

            int leng=0;
            while ((leng=inputStream.read(b))!=-1){
                out.write(b,0,leng);
            }

            out.close();
            inputStream.close();

            jsonData = out.toString();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return jsonData;
    }
}
