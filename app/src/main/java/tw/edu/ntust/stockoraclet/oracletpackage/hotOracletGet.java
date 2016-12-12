package tw.edu.ntust.stockoraclet.oracletpackage;

/**
 * Created by henrychong on 2016/5/24.
 */
public class hotOracletGet {
    private int popularity;
    private String occur_time;
    private int result_status;
    private String predict_time;
    private String event_content;
    private int results;
    private int number;
    private int predict_targetcode;
    private double now_price;
    private int hasContradiction;
    private String predict_people;
    private int type;
    private String predict_targetname;
    private String accuracy;

    public hotOracletGet() {
        super();
    }
    public hotOracletGet(String event_content, int hasContradiction, double now_price,
                         int number, String occur_time, int popularity, String predict_people,
                         int predict_targetcode, String predict_targetname, String predict_time,
                         int result_status, int results, int type, String accuracy) {
        super();
        this.event_content = event_content;
        this.hasContradiction = hasContradiction;
        this.now_price = now_price;
        this.number = number;
        this.occur_time = occur_time;
        this.popularity = popularity;
        this.predict_people = predict_people;
        this.predict_targetcode = predict_targetcode;
        this.predict_targetname = predict_targetname;
        this.predict_time = predict_time;
        this.result_status = result_status;
        this.results = results;
        this.type = type;
        this.accuracy = accuracy;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public int getNameLength() {
        return predict_people.length();
    }

    public String getEvent_content() {
        return event_content;
    }

    public void setEvent_content(String event_content) {
        this.event_content = event_content;
    }

    public int getHasContradiction() {
        return hasContradiction;
    }

    public void setHasContradiction(int hasContradiction) {
        this.hasContradiction = hasContradiction;
    }

    public double getNow_price() {
        return now_price;
    }

    public void setNow_price(double now_price) {
        this.now_price = now_price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOccur_time() {
        return occur_time;
    }

    public void setOccur_time(String occur_time) {
        this.occur_time = occur_time;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getPredict_people() {
        return predict_people;
    }

    public void setPredict_people(String predict_people) {
        this.predict_people = predict_people;
    }

    public int getPredict_targetcode() {
        return predict_targetcode;
    }

    public void setPredict_targetcode(int predict_targetcode) {
        this.predict_targetcode = predict_targetcode;
    }

    public String getPredict_targetname() {
        return predict_targetname;
    }

    public void setPredict_targetname(String predict_targetname) {
        this.predict_targetname = predict_targetname;
    }

    public String getPredict_time() {
        return predict_time;
    }

    public void setPredict_time(String predict_time) {
        this.predict_time = predict_time;
    }

    public int getResult_status() {
        return result_status;
    }

    public void setResult_status(int result_status) {
        this.result_status = result_status;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
