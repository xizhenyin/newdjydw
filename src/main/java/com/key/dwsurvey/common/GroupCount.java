package com.key.dwsurvey.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keyuan on 17/11/10.
 */
public class GroupCount {
    private Integer result;
    private String msg;
    private List<Object[]> answerCount = new ArrayList<Object[]>();
    private List<Object[]> surveyCount = new ArrayList<Object[]>();

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Object[]> getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(List<Object[]> answerCount) {
        this.answerCount = answerCount;
    }

    public List<Object[]> getSurveyCount() {
        return surveyCount;
    }

    public void setSurveyCount(List<Object[]> surveyCount) {
        this.surveyCount = surveyCount;
    }
}
