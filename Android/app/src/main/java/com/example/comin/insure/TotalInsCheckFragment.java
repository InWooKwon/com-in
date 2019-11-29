package com.example.comin.insure;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.comin.R;

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
    private SearchView sv;
    private Spinner cs;
    private Spinner ts;
    private Spinner ss;
    private SwipeRefreshLayout srl;

    Map<Integer, Insurance> insuranceMap = new LinkedHashMap<>();
    ArrayList<Insurance> insSearchList = new ArrayList<>();
    ArrayList<Insurance> insViewList = new ArrayList<>();
    static String selectCompany = "전체보기";
    static String selectType = "전체보기";
    static  String sortType = "정렬";
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

        sv = v.findViewById(R.id.searchView);
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
               // ArrayList<Insurance> tempList = new ArrayList<>();
                insSearchList.clear();
                insViewList.clear();
                for (Insurance ins : insuranceMap.values())
                {
                    if(ins.getCompany().contains(query)) {
                        insSearchList.add(ins);
                        continue;
                    }
                    if(ins.getProductName().contains(query)) {
                        insSearchList.add(ins);
                        continue;
                    }
                    if(ins.getProductType().contains(query)) {
                        insSearchList.add(ins);
                        continue;
                    }
                    if(ins.getProductName().contains(query)) {
                        insSearchList.add(ins);
                        continue;
                    }
                    for(Coverage cv : ins.getCoverageList())
                    {
                        if(cv.getType().contains(query))
                        {
                            insSearchList.add(ins);
                            break;
                        }
                        if(cv.getContent().contains(query))
                        {
                            insSearchList.add(ins);
                            break;
                        }
                    }
                }
                linear.removeAllViews();
                insViewList.addAll(insSearchList);
                for (Insurance ins : sortInsList(sortType, insSearchList))
                {
                    addInsuranceInfoView(ins);
                }
                //검색 시 spinner 초기화
                cs.setSelection(0);
                ts.setSelection(0);

                sv.clearFocus();
                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    linear.removeAllViews();
                    insSearchList.clear();
                    insSearchList.addAll(insuranceMap.values());
                    insViewList.clear();
                    insViewList.addAll(insSearchList);
                    for (Insurance ins : sortInsList(sortType, insSearchList))
                    {
                        addInsuranceInfoView(ins);
                    }
                    cs.setSelection(0);
                    ts.setSelection(0);
                }
                return true;
            }
        });

        getIsuraceList();


        srl = v.findViewById(R.id.swipeRefresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                linear.removeAllViews();
                getIsuraceList();
                srl.setRefreshing(false);
            }

        });

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
                    //초기화 진행
                    insuranceMap.clear();
                    insSearchList.clear();
                    insViewList.clear();
                    selectCompany = "전체보기";
                    selectType = "전체보기";
                    sortType = "정렬";

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
                            coverage.setAmount(coverageObject.getString("amount"));
                            coverage.setContent(coverageObject.getString("content"));
                            coverages.add(coverage);
                        }
                        insurance.setCoverageList(coverages);
                        insuranceMap.put(insurance.getIdx(), insurance);
                        insViewList.add(insurance);
                        insSearchList.add(insurance);
                    }

                    for (int i = 0; i<boardsArray.length(); i++)
                    {
                        JSONObject boardObject = boardsArray.getJSONObject(i);
                        Insurance ins = insuranceMap.get(boardObject.getInt("tag1"));
                        ins.setReviewCount(ins.getReviewCount() + 1);
                    }
                    Log.d("cover11", "1111");

                    cs = (Spinner)getView().findViewById(R.id.companSpinner);
                    ArrayList<String> companyList = new ArrayList<>(companySet);
                    for(String stt : companySet)
                    {
                        Log.d("cover123",stt);
                    }
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,companyList);
                    cs.setAdapter(companyAdapter);
                    cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            insViewList.clear();
                            selectCompany = (String)adapterView.getItemAtPosition(position);
                            for (Insurance ins : insSearchList)
                            {
                                if(!(ins.getCompany().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if(!(ins.getProductType().equals(selectType) || selectType.equals("전체보기")))
                                    continue;
                                insViewList.add(ins);
                            }
                            for (Insurance ins : sortInsList(sortType))
                            {
                                addInsuranceInfoView(ins);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                    ts = (Spinner)getView().findViewById(R.id.typeSpinner);
                    ArrayList<String> typeList = new ArrayList<>(typeSet);
                    ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,typeList);
                    ts.setAdapter(typeAdapter);
                    ts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            insViewList.clear();
                            selectType = (String)adapterView.getItemAtPosition(position);
                            for (Insurance ins : insSearchList)
                            {
                                if(!(ins.getProductType().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if(!(ins.getCompany().equals(selectCompany) || selectCompany.equals("전체보기")))
                                    continue;
                                insViewList.add(ins);
                            }
                            for (Insurance ins : sortInsList(sortType))
                            {
                                addInsuranceInfoView(ins);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    ss = (Spinner)getView().findViewById(R.id.sortSpinner);
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
                            sortType = (String)adapterView.getItemAtPosition(position);
                            for (Insurance ins : sortInsList(sortType))
                            {
                                addInsuranceInfoView(ins);
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

    public ArrayList<Insurance> sortInsList(String sortType) {
        ArrayList<Insurance> iv = new ArrayList<>();
        iv.addAll(insViewList);
       if (sortType.equals("평점순")) {
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
        }
        else if (sortType.equals("후기순")) {
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
        }
        return iv;
    }

    public ArrayList<Insurance> sortInsList(String sortType, ArrayList<Insurance> insList) {
        ArrayList<Insurance> iv = new ArrayList<>();
        iv.addAll(insList);
        if (sortType.equals("평점순")) {
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
        }
        else if (sortType.equals("후기순")) {
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
        }
        return iv;
    }



    public void addInsuranceInfoView(Insurance ins){

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.insurance, null);
        TextView type = (TextView) rl.findViewById(R.id.insType);
        type.setText(ins.getProductType());
        TextView name = (TextView) rl.findViewById(R.id.insName);
        name.setText(ins.getProductName());

        ImageView image = (ImageView)  rl.findViewById(R.id.companyImage);

        //TODO :  추후 추가 필요
        if(ins.getCompany().equals("메리츠"))
            image.setImageResource(R.drawable.meritz);
        else if(ins.getCompany().equals("삼성화재"))
            image.setImageResource(R.drawable.samsughj);
        else if(ins.getCompany().equals("동부화재"))
            image.setImageResource(R.drawable.dongbuhj);
        else if(ins.getCompany().equals("동부"))
            image.setImageResource(R.drawable.dongbu);

        TextView reviewCount = (TextView) rl.findViewById(R.id.reviewNum);
        reviewCount.setText("(" + Integer.toString(ins.getReviewCount()) + ")");


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
