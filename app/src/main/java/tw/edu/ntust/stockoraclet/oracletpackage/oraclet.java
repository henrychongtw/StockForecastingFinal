package tw.edu.ntust.stockoraclet.oracletpackage;

import android.util.Log;

/**
 * Created by henrychong on 2016/5/24.
 */
public class oraclet {
    private String results;
    private String occur_time;
    private int result_status;
    private String predict_time;
    private String event_content;
    private int number;
    private int predict_targetcode;
    private double now_price;
    private int hasContradiction;
    private String predict_people;
    private int type;
    private String predict_targetname;
    private String accuracy;

    public oraclet() {
        super();
    }

    public oraclet(String occur_time, int result_status , String predict_time , String event_content ,
                   int number , int predict_targetcode , double now_price , int hasContradiction ,
                   String predict_people , int type, String predict_targetname , String results
                    ,String accuracy) {
        super();
        Log.i("1111", occur_time);
        Log.i("1111",number+"");
        this.occur_time = occur_time;
        this.result_status = result_status;
        this.predict_time = predict_time;
        this.event_content = event_content;
        this.number = number;
        this.predict_targetcode = predict_targetcode;
        this.now_price = now_price;
        this.hasContradiction = hasContradiction;
        this.predict_people = predict_people;
        this.type = type;
        this.accuracy = accuracy;
        this.results = results;
        this.predict_targetname = predict_targetname;

    }
    public int eventInt(){
        int i = 0;
        if(event_content == "買進"){
             i = 1;
        }else if(event_content == "賣出"){
            i = 0;
        }
        return i;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getResult() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public int getNameLength() {
        return predict_people.length();
    }

    public double getnow_price() {
        return now_price;
    }

    public int getType() {
        return type;
    }

    public void setnow_price(double now_price) {
        this.now_price = now_price;
    }

    public String getoccur_time() {
        return occur_time;
    }

    public String getevent_content() {
        return event_content;
    }

    public int isHasContradiction() {
        return hasContradiction;
    }

    public int getNumber() {
        return number;
    }

    public String getpredict_people() {
        return predict_people;
    }

    public int getpredict_targetcode() {
        return predict_targetcode;
    }

    public String getpredict_targetname() {
        return predict_targetname;
    }

    public String getpredict_time() {
        return predict_time;
    }

    public int getresult_status() {
        return result_status;
    }

    public void setoccur_time(String occur_time) {
        this.occur_time = occur_time;
    }

    public void setevent_content(String event_content) {
        this.event_content = event_content;
    }

    public void setHasContradiction(int hasContradiction) {
        this.hasContradiction = hasContradiction;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setpredict_people(String predict_people) {
        this.predict_people = predict_people;
    }

    public void setpredict_targetcode(int predict_targetcode) {
        this.predict_targetcode = predict_targetcode;
    }

    public void setpredict_targetname(String predict_targetname) {
        this.predict_targetname = predict_targetname;
    }

    public void setpredict_time(String predict_time) {
        this.predict_time = predict_time;
    }

    public void setresult_status(int result_status) {
        this.result_status = result_status;
    }

    public void setType(int type) {
        this.type = type;
    }


}

