package tw.edu.ntust.stockoraclet.oracletpackage;

/**
 * Created by henrychong on 2016/5/25.
 */
public class comment {
    private String status;
    private String name;
    private String content;
    private String time;
    private int o_number;

    public comment(String content, String name, int o_number, String status, String time) {
        this.content = content;
        this.name = name;
        this.o_number = o_number;
        this.status = status;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getO_number() {
        return o_number;
    }

    public void setO_number(int o_number) {
        this.o_number = o_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
