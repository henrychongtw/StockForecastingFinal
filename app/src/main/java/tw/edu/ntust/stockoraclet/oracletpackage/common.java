package tw.edu.ntust.stockoraclet.oracletpackage;

/**
 * Created by henrychong on 2016/5/24.
 */
public class common {
    // Android官方模擬器連結本機web server可以直接使用 http://10.0.2.2
    public static String URL = "http://140.118.7.47:8000/findOraclet/1/";
    public static String URL2 = "http://140.118.7.47:8000/validatedRecord?oracletNumber=";
    public static String URLL = "http://140.118.7.47:8000/hotOraclet?limit=10&year=0";
    public static String URLcode = "http://140.118.7.47:8000/findDateOrID?codeOrdate=";
    public static String URLoraclet = "http://140.118.7.47:8000/oraclet?oracletNumber=";
    public static String URLcomment = "http://140.118.7.47:8000/oracletComment?oracletNumber=";
    public static String URLCheckVerify = "http://140.118.7.44:8000/checkVerify/?o_number=";
    public static String URLAllOraclet = "http://140.118.7.44:8000/showsubscribedOraclet?u_email=";
    public static String URLAllPredictor = "http://140.118.7.47:8000/showAllPredictor?u_mail=";
    public static String URLPredictorOraclet = "http://140.118.7.47:8000/predictorAllOraclet?predictor=";
    public static String URLContradiction = "http://140.118.7.47:8000/findContradictions?oracletNumber=";
    public static String URLsendKey = "http://10.0.3.2:8080/StockServer/MainServlet?key=";
    //10.0.3.2:8080是連Genymotion的特殊URL
    public static String URLSubscription = "http://140.118.7.44:8000/subscribeOraclet?u_email=";
    //public static String URLSubscription = "http://10.0.3.2:8080/StockServer/SubServlet?u_email=";




//	public final static String URL = "http://10.0.2.2:8080/TextToJson_Web/SearchServlet";

}
