package com.example.comin.community;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.R;
import com.example.comin.insure.Insurance;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class QnABoardWriteActivity extends AppCompatActivity {

    String TAG_IMAGE = "insuranceimage";
    String TAG_TEXT = "insurancetext";
    String TAG_IDX = "insuranceidx";
    ArrayList<Insurance> insuranceList = new ArrayList<>();
    List<Map<String,Object>> dialogList;
    List<Map<String,Object>> tempList;

    ArrayList<Map<String,Integer>> taglist = new ArrayList<>();
    int tagcount=0;

    ListViewAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_board_write);

        final EditText titletext = (EditText) findViewById(R.id.titletext);
        final EditText bodytext = (EditText) findViewById(R.id.bodytext);
        ImageButton postbtn = (ImageButton) findViewById(R.id.postbtn);

        Intent intent = getIntent();
        insuranceList = (ArrayList<Insurance>) intent.getSerializableExtra("insurancelist");

        Map<String,Object> insimage=new HashMap<>();
        insimage.put("메리츠",R.drawable.meritz);
        insimage.put("삼성화재",R.drawable.samsungfire);
        insimage.put("동부화재",R.drawable.dongbufire);

        dialogList = new ArrayList<>();
        tempList = new ArrayList<>();
        for(Insurance ins:insuranceList){

            Map<String,Object> item = new HashMap<>();
            item.put(TAG_TEXT,ins.getProductName());
            item.put(TAG_IMAGE,insimage.get(ins.getCompany()));
            item.put(TAG_IDX,ins.getIdx());

            dialogList.add(item);
        }
        tempList.addAll(dialogList);
        ImageButton addtagbtn = (ImageButton) findViewById(R.id.addtagbtn);
        addtagbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagDialog();
            }
        });

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String posttitle = titletext.getText().toString();
                String postbody = bodytext.getText().toString();
                postQnABoard(posttitle,postbody);
            }
        });

    }

    public void showTagDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.tagdialog,null);
        builder.setView(view);

        final EditText tagname = (EditText)view.findViewById(R.id.tagname);
        final ListView listview = (ListView)view.findViewById(R.id.taglistview);
        final AlertDialog dialog = builder.create();

        tagname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = tagname.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }
        });

        adapter = new ListViewAdapter(this,dialogList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(taglist.size() == 5){
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"더이상 추가할 수 없습니다.",Toast.LENGTH_SHORT);
                    return;
                }
                final Map<String,Integer> tag = new HashMap<>();
                String insname = dialogList.get(position).get(TAG_TEXT).toString();
                Integer insidx = (Integer)dialogList.get(position).get(TAG_IDX);
                tag.put(insname,insidx);

                taglist.add(tag);
                final RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.tagpannel, null);
                TextView tagname = (TextView)rl.findViewById(R.id.tagname);
                tagname.setText(insname);

                final LinearLayout taglayer = (LinearLayout)findViewById(R.id.taglayer);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taglist.remove(tag);
                        taglayer.removeView(rl);
                        filter("");
                    }
                });
                taglayer.addView(rl);
                filter("");
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public boolean selectedCheck(String name){
        boolean check=false;
        for(Map<String,Integer> t:taglist){
            Set set = t.keySet();
            Iterator it = set.iterator();
            String key = (String)it.next();
            if(key.compareTo(name) == 0){
                check=true;
                break;
            }
        }
        if(check){
            return true;
        }
        else{
            return false;
        }
    }

    public void filter(String text){
        dialogList.clear();
        for (Map<String,Object> tag:tempList) {
            String name = tag.get(TAG_TEXT).toString();
            if(selectedCheck(name)){ // 선택한거면 넘어감
                continue;
            }
            if(name.contains(text)) {
                dialogList.add(tag);
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void postQnABoard(String posttitle,String postbody){
        JSONObject postjson = new JSONObject();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            User user=new User();
            postjson.put("title", posttitle);
            postjson.put("body", postbody);
            postjson.put("type",2);
            postjson.put("date", sdf.format(date));
            postjson.put("author", user.getUserNick(QnABoardWriteActivity.this));
            int i=1;
            for(Map<String,Integer> tag:taglist){
                Set set = tag.keySet();
                Iterator it = set.iterator();
                String key = (String)it.next();
                postjson.put("tag"+i,tag.get(key));
                i+=1;
            }

            //전송
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "board", postjson, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //받은 json형식의 응답을 받아
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONObject result = jsonResponse.getJSONObject("success");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("test", "json object error");
        }
    }
}
