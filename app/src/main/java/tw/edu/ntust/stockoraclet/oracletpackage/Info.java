package tw.edu.ntust.stockoraclet.oracletpackage;

/**
 * Created by henrychong on 2016/5/24.
 */
public class Info {
    public String date;
    public double price;
    public int o_number;

    public Info() {
        super();
    }

    public  Info(String date, double price,int o_number){
        super();
        this.date = date;
        this.price = price;
        this.o_number = o_number;
    }

    public int getO_number() {
        return o_number;
    }

    public void setO_number(int o_number) {
        this.o_number = o_number;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}