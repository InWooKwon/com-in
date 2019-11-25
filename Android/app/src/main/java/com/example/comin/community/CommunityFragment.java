package com.example.comin.community;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommunityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Post> ReviewBoardList = new ArrayList<>();
    ArrayList<Post> QnABoardList = new ArrayList<>();
    ArrayList<Post> FreeBoardList = new ArrayList<>();

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
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
        View v = inflater.inflate(R.layout.fragment_community, container, false);


        getBoardList();
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

    public void getBoardList(){
        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "board",null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray boardsArray = jsonResponse.getJSONArray("board");
                    Log.d("test_length", Integer.toString(boardsArray.length()));
                    for (int i = 0; i<boardsArray.length(); i++)
                    {
                        JSONObject boardObject = boardsArray.getJSONObject(i);

                        Post post = new Post();

                        post.setIdx(boardObject.getInt("idx"));
                        post.setType(boardObject.getInt("type"));
                        post.setTitle(boardObject.getString("title"));
                        post.setScore(boardObject.getInt("score"));
                        post.setBody(boardObject.getString("body"));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date writeDate = sdf.parse(boardObject.getString("date"));  ////////
                        post.setDate(writeDate);
                        post.setAuthor(boardObject.getString("author"));
                        post.setUp(boardObject.getInt("up"));
                        post.setTag1(boardObject.getInt("tag1"));
                        post.setTag2(boardObject.getInt("tag2"));
                        post.setTag3(boardObject.getInt("tag3"));
                        post.setTag4(boardObject.getInt("tag4"));
                        post.setTag5(boardObject.getInt("tag5"));

                        if(post.getType() == 1) {
                            ReviewBoardList.add(post);
                        }
                        else if(post.getType() == 2) {
                            QnABoardList.add(post);
                        }
                        else if(post.getType() == 3) {
                            FreeBoardList.add(post);
                        }
                    }
                    for (int i=0;i<2;i++)
                    {
                        addBoardPreView(1, ReviewBoardList.get(i));
                        addBoardPreView(2, QnABoardList.get(i));
                        addBoardPreView(3, FreeBoardList.get(i));
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

    public void addBoardPreView(int type, Post board){
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.boardsummary, null);

        LinearLayout pre = null;
        if(type == 1) {
            pre = getView().findViewById(R.id.reviewlayer);
        }
        else if(type == 2) {
            pre = getView().findViewById(R.id.qnalayer);
        }
        else if(type == 3) {
            pre = getView().findViewById(R.id.freelayer);
        }

        TextView name = (TextView) rl.findViewById(R.id.nickName);
        name.setText(board.getAuthor());

        TextView time = (TextView) rl.findViewById(R.id.time);
        time.setText(formatTimeString(board.getDate()));

        TextView score = (TextView) rl.findViewById(R.id.rating);
        score.setText(Integer.toString(board.getScore()));

        TextView up = (TextView) rl.findViewById(R.id.ddabongcount);
        up.setText(Integer.toString(board.getUp()));
        Log.d("test_type1", board.getBody());

        TextView content = (TextView) rl.findViewById(R.id.content);
        content.setText(board.getBody());

        Button reviewbtn = (Button) getView().findViewById(R.id.reviewbtn);
        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReviewBoardActivity.class);
                startActivity(intent);
            }
        });
        Button qnabtn = (Button) getView().findViewById(R.id.qnabtn);
        qnabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QnABoardActivity.class);
                startActivity(intent);
            }
        });
        Button freebtn = (Button) getView().findViewById(R.id.freebtn);
        freebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FreeBoardActivity.class);
                startActivity(intent);
            }
        });
        pre.addView(rl);
        //reviewpreview.addView(rl);
        //linear.addView(reviewpreview);
    }
    public static String formatTimeString(Date tempDate) {
        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < 60) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= 60) < 60) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= 60) < 24) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= 24) < 30) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= 30) < 12) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }
}
