package com.th3md.trivia.data;

import com.th3md.trivia.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void Finished(ArrayList<Question> questionArrayList);
}
