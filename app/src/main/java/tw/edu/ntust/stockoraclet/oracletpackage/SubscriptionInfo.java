package tw.edu.ntust.stockoraclet.oracletpackage;

/**
 * Created by henrychong on 2016/5/27.
 */
public class SubscriptionInfo {
    //private int notification;
    private String predict_people;
    private String u_email;
    private int o_number;
    private int result_status;
    private String p_name;
    private String t_name;
    //private String accuracy;

    public SubscriptionInfo() {
        super();
    }

    public SubscriptionInfo(String u_mail, int o_number, int result_status, String p_name, String t_name) {
        this.u_email = u_mail;
        this.o_number = o_number;
        this.result_status = result_status;
        this.p_name = p_name;
        this.t_name = t_name;
    }


    public String getU_mail() {
        return u_email;
    }

    public void setU_mail(String u_mail) {
        this.u_email = u_mail;
    }

    public int getO_number(){
        return o_number;
    }

    public void setO_number(int o_number) {
        this.o_number = o_number;
    }

    public int getResult_status() {
        return result_status;
    }

    public void setResult_status(int result_status) {
        this.result_status = result_status;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
}
