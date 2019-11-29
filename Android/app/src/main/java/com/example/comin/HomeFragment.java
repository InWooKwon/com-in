package com.example.comin;

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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.community.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout linear;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        linear = v.findViewById(R.id.homelinearLayout);
        getHotInsuranceList();

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


    public void getHotInsuranceList(){
        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances/hot",null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray hotinsArray = jsonResponse.getJSONArray("hotInsurances");
                    for (int i = 0; i<hotinsArray.length(); i++) {
                        LinearLayout hotLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.hotinsure, null);
                        JSONObject hotinsObject = hotinsArray.getJSONObject(i);

                        ImageView hotImage = hotLayout.findViewById(R.id.hot);
                        if(i==0)
                            hotImage.setImageResource(R.drawable.hot1);
                        else if(i==1)
                            hotImage.setImageResource(R.drawable.hot2);
                        else if (i==2)
                            hotImage.setImageResource(R.drawable.hot3);
                        else if (i==3)
                            hotImage.setImageResource(R.drawable.hot4);
                        else if (i==4)
                            hotImage.setImageResource(R.drawable.hot5);

                        ImageView imageView = hotLayout.findViewById(R.id.hotCompany);
                        imageView.setImageResource(getCompanyImageId(hotinsObject.getString("company")));
                        TextView textView = hotLayout.findViewById(R.id.hotName);
                        textView.setText(hotinsObject.getString("productName"));

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 0,0, 25);
                        hotLayout.setLayoutParams(layoutParams);

                        linear.addView(hotLayout);
                    }
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

    public int getCompanyImageId(String company)
    {
        if (company.equals("교보라이프플래닛생명"))
        {
            return R.drawable.kyobolife;
        }
        else if (company.equals("하나생명"))
        {
            return R.drawable.hanalife;

        }
        else if (company.equals("신한생명"))
        {
            return R.drawable.sinhanlife;
        }
        else if (company.equals("흥국생명"))
        {
            return R.drawable.heungkuk;
        }
        else if (company.equals("KDB생명"))
        {
            return R.drawable.kdblife;
        }
        else if (company.equals("라이나생명"))
        {
            return R.drawable.laina;
        }
        else if (company.equals("에이스손해보험"))
        {
            return R.drawable.ace;
        }
        else if (company.equals("DB손해보험"))
        {
            return R.drawable.dbsonhae;
        }
        else if (company.equals("동양생명"))
        {
            return R.drawable.dongyang;
        }
        else if (company.equals("삼성화재"))
        {
            return R.drawable.samsungfire;
        }
        else if (company.equals("MG손해보험"))
        {
            return R.drawable.mgsonhae;
        }
        else if (company.equals("KB손해보험"))
        {
            return R.drawable.kbsonhae;
        }
        else if (company.equals("한화생명"))
        {
            return R.drawable.hanalife;
        }
        else if (company.equals("미래에셋생명"))
        {
            return R.drawable.miraeasset;
        }
        else if (company.equals("한화손해보험"))
        {
            return R.drawable.hanhwasonhae;
        }
        else if (company.equals("NH농협손해보험"))
        {
            return R.drawable.nhsonhae;
        }
        else if (company.equals("삼성생명"))
        {
            return R.drawable.samsunglife;
        }
        else if (company.equals("AIG"))
        {
            return R.drawable.aig;
        }
        else if (company.equals("롯데손해보험"))
        {
            return R.drawable.lottesonhae;
        }
        else if (company.equals("현대해상"))
        {
            return R.drawable.hyundae;
        }
        else if (company.equals("메리츠화재"))
        {
            return R.drawable.meritz;
        }
        else
            return -1;
    }
}
