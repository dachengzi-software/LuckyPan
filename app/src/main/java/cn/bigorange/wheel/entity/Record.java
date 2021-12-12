package cn.bigorange.wheel.entity;

import java.io.Serializable;
import java.util.List;

public class Record implements Serializable {

    private long id;
    private String question;
    private List<String> optionList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }
}
