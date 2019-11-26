package com.example.comin.insure;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import android.view.ViewGroup;

import com.example.comin.R;
import com.example.comin.community.Post;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TotalInsCheckFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TotalInsCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TotalInsCheckFragment extends Fragment {

    private LinearLayout linear;
    Map<Integer, Insurance> insuranceMap = new LinkedHashMap<>();
    ArrayList<Insurance> insViewList = new ArrayList<>();
    static String selectCompany = "전체보기";
    static String selectType = "전체보기";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TotalInsCheckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TotalInsCheckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TotalInsCheckFragment newInstance(String param1, String param2) {
        TotalInsCheckFragment fragment = new TotalInsCheckFragment();
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
        View v = inflater.inflate(R.layout.fragment_total_ins_check, container, false);

        linear = v.findViewById(R.id.linearLayout);
        getIsuraceList();
        return v;
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

    public void getIsuraceList(){
        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances",null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray insurancesArray = jsonResponse.getJSONArray("insurances");
                    JSONArray coverageArray = jsonResponse.getJSONArray("coverages");
                    JSONArray boardsArray = jsonResponse.getJSONArray("reviewBoard");

                    Set<String> companySet = new LinkedHashSet<>();
                    companySet.add("전체보기");
                    Set<String> typeSet = new LinkedHashSet<>();
                    typeSet.add("전체보기");

                    Log.d("cover22", "22222");

                    for (int i = 0; i<insurancesArray.length(); i++)
                    {
                        JSONObject insuranceObject = insurancesArray.getJSONObject(i);

                        Insurance insurance = new Insurance();

                        insurance.setIdx(insuranceObject.getInt("idx"));
                        insurance.setProductName((insuranceObject.getString("productName")));
                        insurance.setCompany(insuranceObject.getString("company"));
                        insurance.setProductType(insuranceObject.getString("productType"));
                        insurance.setMinAge(insuranceObject.getInt("minAge"));
                        insurance.setMaxAge(insuranceObject.getInt("maxAge"));
                        insurance.setPrice(insuranceObject.getInt("price"));
                        insurance.setScore(insuranceObject.getDouble("score"));
                        companySet.add(insurance.getCompany());
                        typeSet.add(insurance.getProductType());

                        ArrayList<Coverage> coverages = new ArrayList<>();
                        for (int j = 0; j<coverageArray.length(); j++)
                        {
                            JSONObject coverageObject = coverageArray.getJSONObject(j);
                            if (coverageObject.getInt("insuranceIdx") != insurance.getIdx())
                                continue;
                            Coverage coverage = new Coverage();
                            coverage.setType(coverageObject.getString("coverageType"));
                            coverage.setAmount(coverageObject.getInt("amount"));
                            coverage.setContent(coverageObject.getString("content"));
                            coverages.add(coverage);
                        }
                        insurance.setCoverageList(coverages);
                        insuranceMap.put(insurance.getIdx(), insurance);
                    }

                    for (int i = 0; i<boardsArray.length(); i++)
                    {
                        JSONObject boardObject = boardsArray.getJSONObject(i);
                        Log.d("insins13",Integer.toString(boardObject.getInt("tag1")));
                        Log.d("insins11",Integer.toString(insuranceMap.get(boardObject.getInt("tag1")).getReviewCount()));
                        Insurance ins = insuranceMap.get(boardObject.getInt("tag1"));
                        ins.setReviewCount(ins.getReviewCount() + 1);
                        Log.d("insins12",Integer.toString(insuranceMap.get(boardObject.getInt("tag1")).getReviewCount()));
                    }

                    Spinner cs = (Spinner)getView().findViewById(R.id.companSpinner);
                    ArrayList<String> companyList = new ArrayList<>(companySet);
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,companyList);
                    cs.setAdapter(companyAdapter);
                    cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            insViewList.clear();
                            selectCompany = (String)adapterView.getItemAtPosition(position);
                            for (Insurance ins : insuranceMap.values())
                            {
                                if(!(ins.getCompany().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if(!(ins.getProductType().equals(selectType) || selectType.equals("전체보기")))
                                    continue;
                                insViewList.add(ins);
                                addInsuranceInfoView(ins);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                    Spinner ts = (Spinner)getView().findViewById(R.id.typeSpinner);
                    ArrayList<String> typeList = new ArrayList<>(typeSet);
                    ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,typeList);
                    ts.setAdapter(typeAdapter);
                    ts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            insViewList.clear();
                            selectType = (String)adapterView.getItemAtPosition(position);
                            for (Insurance ins : insuranceMap.values())
                            {
                                if(!(ins.getProductType().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if(!(ins.getCompany().equals(selectCompany) || selectCompany.equals("전체보기")))
                                    continue;
                                insViewList.add(ins);
                                addInsuranceInfoView(ins);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    Spinner ss = (Spinner)getView().findViewById(R.id.sortSpinner);
                    ArrayList<String> sortList = new ArrayList<>();
                    sortList.add("정렬");
                    sortList.add("평점순");
                    sortList.add("후기순");
                    ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,sortList);
                    ss.setAdapter(sortAdapter);
                    ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            ArrayList<Insurance> iv = new ArrayList<>();
                            iv.addAll(insViewList);
                            if(adapterView.getItemAtPosition(position).equals("평점순")) {
                                Collections.sort(iv, new Comparator<Insurance>() {
                                    @Override
                                    public int compare(Insurance o1, Insurance o2) {
                                        if (o1.getScore() < o2.getScore())
                                            return 1;
                                        else if (o1.getScore() > o2.getScore())
                                            return -1;
                                        else
                                            return 0;
                                    }
                                });
                                for (Insurance ins : iv) {
                                    addInsuranceInfoView(ins);
                                }
                            }
                            else if(adapterView.getItemAtPosition(position).equals("정렬"))
                            {
                                for (Insurance ins : insViewList) {
                                    addInsuranceInfoView(ins);
                                }
                            }
                            else if(adapterView.getItemAtPosition(position).equals("후기순")){
                                Collections.sort(iv, new Comparator<Insurance>() {
                                    @Override
                                    public int compare(Insurance o1, Insurance o2) {
                                        if (o1.getReviewCount() < o2.getReviewCount())
                                            return 1;
                                        else if (o1.getReviewCount() > o2.getReviewCount())
                                            return -1;
                                        else
                                            return 0;
                                    }
                                });
                                for (Insurance ins : iv) {
                                    addInsuranceInfoView(ins);
                                }
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test",error.toString());
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void addInsuranceInfoView(Insurance ins){

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.insurance, null);
        TextView type = (TextView) rl.findViewById(R.id.insType);

        type.setText(ins.getProductType());
        TextView name = (TextView) rl.findViewById(R.id.insName);
        name.setText(ins.getProductName());

        //후기수 필요

        TextView reviewCount = (TextView) rl.findViewById(R.id.reviewNum);
        reviewCount.setText("후기 " + Integer.toString(ins.getReviewCount())+" 개");


        TextView score = (TextView) rl.findViewById(R.id.score);
        score.setText(Double.toString(ins.getScore()));

        TextView price = (TextView) rl.findViewById(R.id.price);
        price.setText("보험료 : 월 " + Integer.toString(ins.getPrice()) + "원");

        StringBuilder sb = new StringBuilder();
        for (Coverage cover : ins.getCoverageList())
        {
            sb.append(cover.getType() + " : " + cover.getAmount() + "원\n");
        }
        TextView coverage = (TextView) rl.findViewById(R.id.coverage);
        coverage.setText(sb);


        rl.setTag((Insurance)ins);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoDetailActivity.class);
                intent.putExtra("insurance", (Insurance)v.getTag());
                startActivity(intent);
            }
        });


        linear.addView(rl);
    }
}
