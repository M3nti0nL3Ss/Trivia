package com.th3md.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.th3md.trivia.controller.AppController;
import com.th3md.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {


    ArrayList<Question> arrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++){
                            try{
                                    Question question = new Question();
                                    question.setAnswer(response.getJSONArray(i).get(0).toString());
                                    question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                    arrayList.add(question);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            //Log.d("Respones", "onResponse: "+response);
                        }

                        if(null != callBack) callBack.Finished(arrayList);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Respones", "onResponse: "+error.getMessage());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return arrayList;
    }
}
