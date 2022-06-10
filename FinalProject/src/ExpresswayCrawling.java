// This class is for web-crawling from search.naver.com for get information about Real-time Highway Status
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpresswayCrawling { // This method returning the crwaled information by List<String>
    public static List<ExpressWay> getexpressList(String s) { // the argument determine what direction will be returning.
        try {
            Document doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=%EA%B3%A0%EC%86%8D%EB%8F%84%EB%A1%9C+%EC%83%81%ED%99%A9").get();
            Elements upspeed = doc.select("[data-way=up] li dl dd");
            Elements upname = doc.select("[data-way=up] li dl dt a");
            Elements dnspeed = doc.select("[data-way=dn] li dl dd");
            Elements dnname = doc.select("[data-way=dn] li dl dt a");
            List<String> upnamelist = upname.eachText(); // make list of name of northbound lane 
            List<String> upspeedlist = upspeed.eachText(); // make list of speed, distance, time, junction name of northbound lane
            List<String> dnnamelist = dnname.eachText(); // make list of name of southbound lane
            List<String> dnspeedlist = dnspeed.eachText(); // make list of speed, distance, time, junction name of southbound lane

            Map<String, String> upmap = makeMap(upnamelist, upspeedlist);
            Map<String, String> dnmap = makeMap(dnnamelist, dnspeedlist);

            // make ExpressWay list. ExpressWay is a class and its instance have information of name, speed, distance, time, and junction name
            List<ExpressWay> uplist = makeExpressWayList(upmap);
            List<ExpressWay> dnlist = makeExpressWayList(dnmap);

            // If the argument is "up", return northbound lane list. If the argument is "down", return southbound lane list.
            if(s.equals("up")) return uplist;
            else if(s.equals("dn")) return dnlist;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<ExpressWay> makeExpressWayList(Map<String, String> map) { // This method make ExpressWay List taking the crawled information
        List<ExpressWay> list = new ArrayList<>();
        for(String key : map.keySet()){
            String[] array = map.get(key).split(" "); // split the whole string by " "
            String subname = array[0]; // the variable for store junction name
            String distance = array[2].replace(',', '\0'); // the variable for store length of the highway
            String speed = array[4]; // the variable for store the real-time speed at the highway
            array[5] = array[5].replaceAll("시간", "hours ");
            array[5] = array[5].replaceAll("분", "minutes");
            String time = array[5]; // the variable for store the time for through the highway
            list.add(new ExpressWay(key,subname,distance,speed,time));
        }
        return list;
    }

    private static Map<String, String> makeMap(List<String> namelist, List<String> speedlist){ // This methoed is for matching the name of expressway and other informations by making map.
        Map<String, String> map = new HashMap<>();
        List<String> speedlist2 = new ArrayList<>();
        int speed2size = namelist.size();
        for(int i = 0; i< speed2size; i++){
            String a = speedlist.get(0) + " " +speedlist.get(1);
            speedlist.remove(0);
            speedlist.remove(0);
            speedlist2.add(a);
        }
        for(int i = 0;i<speed2size;i++){
            map.put(namelist.get(i), speedlist2.get(i));
        }
        return map;
    }
}