package com.example.comin.myinsure;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.R;
import com.example.comin.login.User;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyInsureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyInsureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyInsureFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout linear;

    ArrayList <String> insureName = new ArrayList<String>();
    ArrayList <String> insureType = new ArrayList<String>();
    ArrayList <String> insurePrice = new ArrayList<String>();
    ArrayList <String> insureDescript = new ArrayList<String>();

    TextView holdText;
    TextView totalText;

    User user = new User();

    int total_price=0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyInsureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyInsureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyInsureFragment newInstance(String param1, String param2) {
        MyInsureFragment fragment = new MyInsureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_insure, container, false);

        holdText = (TextView)v.findViewById(R.id.HoldText);
        totalText = (TextView)v.findViewById(R.id.TotalText);

        Log.d("test","myinsureFragment");
        linear = v.findViewById(R.id.linearLayout);

       getmyinsure();
       return v;
    }

    public boolean getmyinsure(){

        //int idx = user.getUserIdx(MyInsureFragment.this);

            //JSONObject testjson = new JSONObject();
           // JSONObject input_object = new JSONObject();
            //testjson.put("idx", "");
           // String jsonString = testjson.toString(); //완성된 json 포맷
           // Log.d("test", jsonString);


        String idx = Integer.toString(user.getUserIdx(getActivity().getApplicationContext()));


            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL)+"myinsure/"+idx, null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //받은 json형식의 응답을 받아
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        Log.d("test","1"+response.toString());
                        JSONObject outputJson = jsonResponse.getJSONObject("Output");
                        Log.d("test","2"+outputJson.toString());
                        JSONObject resultjson = outputJson.getJSONObject("Result");
                        Log.d("test","3"+resultjson.toString());


                        JSONArray insuranceArray = resultjson.getJSONArray("보험계약내용조회");
                        Log.d("test","5"+Integer.toString(insuranceArray.length()));
                        Log.d("test",insuranceArray.getJSONObject( 0).getString("상품명"));



                        for (int i = 0; i<insuranceArray.length(); i++) {
                            Log.d("test","for");
                            JSONObject insuranceObject = insuranceArray.getJSONObject(i);
                            insureName.add(insuranceObject.getString("상품명"));
                            Log.d("test","1");
                            insurePrice.add(insuranceObject.getString("보험료"));
                            Log.d("test","3");
                            insureDescript.add(insuranceObject.getString("만기일자"));
                            Log.d("test","4");

                            Log.d("test","check array add"+insureName.get(i));
                            Log.d("test","check array add"+insurePrice.get(i));
                            Log.d("test","check array add"+insureDescript.get(i));


                            Log.d("test",insuranceObject.getString("상품명"));
                            Log.d("test",insuranceObject.getString("보험료"));
                            Log.d("test",insuranceObject.getString("만기일자"));

                        }

                        Log.d("test", "check array list" + Integer.toString(insureName.size()));
                        for (int i = 0; i < insureName.size(); i++) {
                            addInsuranceInfoView(insureName.get(i), "보험Type", insurePrice.get(i), insureDescript.get(i));

                            total_price = total_price + Integer.parseInt(insurePrice.get(i));
                        }

                        holdText.setText("보유 계약\n" + Integer.toString(insureName.size()) + "건");
                        totalText.setText("월 보험료 \n 합계 \n" + Integer.toString(total_price) + "원");



                        //key값에 따라 value값을 쪼개 받아옵니다.
                        Log.d("test","test");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("test", error.toString());
                    error.printStackTrace();
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);

        return true;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void addInsuranceInfoView(String insName, String insType, String insPrice, String insDescrypt){

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.my_insurance, null);

        ImageView companyImage = rl.findViewById(R.id.mycompany);
        //TODO : 이미지추가 후 맞게 수정
        companyImage.setImageResource(R.drawable.meritz);

        TextView type = (TextView) rl.findViewById(R.id.myinsType);

        type.setText(insType);
        TextView name = (TextView) rl.findViewById(R.id.myinsName);
        name.setText(insName);


        TextView price = (TextView) rl.findViewById(R.id.myprice);
        price.setText("월 보험료 : ₩"+ insPrice + "원");


        TextView description = (TextView) rl.findViewById(R.id.mydescript);
        description.setText("만기일자 : "+insDescrypt);

        Log.d("test","test create view");


        linear.addView(rl);
    }
}
