package edu.cmu;


import com.csvreader.CsvReader;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StateServlet",urlPatterns = {"/submit"})
public class StateServlet extends HttpServlet {
    public static String nickname = "";
    public static String population = "";
    public static String flower = "";
    public static String capital = "";
    public static String song = "";
    public static String flag = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("State"));
        String state = req.getParameter("State");
        nickname = scrapeNickname(state);
        population = scrapePopulation(state);
        flower = scrapeFlower(state);
        capital = scrapeCapital(state);
        song = scrapeSong(state);
        flag = scrapeFlag(state);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("result.jsp");
        requestDispatcher.forward(req, resp);
    }
    private static String scrapeNickname(String state) throws IOException {
        String name = state;
        Document doc = Jsoup.connect("https://britannica.com/topic/List-of-nicknames-of-U-S-States-2130544").get();
        Elements newsHeadlines = doc.select("a[class=md-crosslink]");
        System.out.println(newsHeadlines.size());

        for (int i = 0; i < newsHeadlines.size(); i++) {
            if (newsHeadlines.get(i).html().equals(name)) {
                System.out.println(newsHeadlines.get(i).html());
                return String.valueOf(newsHeadlines.get(i).parent().nextElementSibling().html());
            }
        }
        return null;
    }
    private static String scrapePopulation(String state) throws IOException {
        List<String[]> content = new ArrayList<>();
        try {
            CsvReader csvReader = new CsvReader("fips.csv", ',', Charset.forName("GBK"));
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                String line = csvReader.getRawRecord();
                content.add(csvReader.getValues());
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code="";
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i)[0].equals(state)){
                code=content.get(i)[1];
            }
        }
        System.out.println(code);
        String url = "https://api.census.gov/data/2020/dec/pl?get=NAME,P1_001N&for=state:"+code+"&key=f2161d5f85166e4d5fa5ad2e6ea0d250d95bd845";
        String name=state;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        String USER_AGENT = "Mozilla/5.0";
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        String all=response.toString();
        String[] splitall=all.split("\",\"");
        System.out.println(splitall[3]);
        return splitall[3];
    }
    private static String scrapeFlower(String state) throws IOException {
        String name = state;
        Document doc = Jsoup.connect("https://statesymbolsusa.org/categories/flower").get();
        Elements newsHeadlines = doc.select("a[href^=http]");
        System.out.println(newsHeadlines.size());
        for (int i = 0; i < newsHeadlines.size(); i++) {
            if (newsHeadlines.get(i).html().equals(name)) {
                System.out.println(newsHeadlines.get(i).html());
                System.out.println(newsHeadlines.get(i).parent().parent().parent().child(0).child(0).child(0).child(0).attr("src"));
                return String.valueOf(newsHeadlines.get(i).parent().parent().parent().child(0).child(0).child(0).child(0).attr("src"));
            }
        }
        return null;
    }
    private static String scrapeCapital(String state) throws IOException {
        String name = state;
        Document doc = Jsoup.connect("https://gisgeography.com/united-states-map-with-capitals").get();
        Elements newsHeadlines1 = doc.select("div[class=wp-block-kadence-column inner-column-1 kadence-column_4a1fe7-ee]");
        System.out.println(newsHeadlines1.get(0).child(0).child(0));
        Elements newsHeadlines2 = doc.select("div[class=wp-block-kadence-column inner-column-2 kadence-column_c51600-7e]");
        System.out.println(newsHeadlines2.get(0).child(0).child(0));
        String all=String.valueOf(newsHeadlines1.get(0).child(0).child(0))+String.valueOf(newsHeadlines2.get(0).child(0).child(0));
        System.out.println(all);
        int ind=all.indexOf(name);
        int start=0;
        for (int i = 0; i < 50; i++) {
            if (all.charAt(ind + i) == '(') {
                start = ind + i;
            }
            if (all.charAt(ind + i) == ')') {
                return all.substring(start + 1, ind + i);
            }
        }
        return null;
    }
    private static String scrapeSong(String state) throws IOException {
        String name = state;
        Document doc = Jsoup.connect("https://50states.com/songs/").get();
        Elements newsHeadlines = doc.select("a[href^=http]");
        System.out.println(newsHeadlines.size());
        String result="";
        for (int i = 0; i < newsHeadlines.size(); i++) {
            if (newsHeadlines.get(i).parent().parent().child(0).html().equals(name)) {
                result=result+newsHeadlines.get(i).html()+"\n";
            }
        }
        return String.valueOf(result);
    }
    private static String scrapeFlag(String state) throws IOException {
        String name = state;
        Document doc = Jsoup.connect("https://states101.com/flags").get();
        Elements newsHeadlines = doc.select("img[src^=/img/flags/gif/]");
        System.out.println(newsHeadlines.size());
        for (int i = 0; i < newsHeadlines.size(); i++) {
            if (newsHeadlines.get(i).attr("alt").startsWith(name)) {
                return String.valueOf(newsHeadlines.get(i).attr("src"));
            }
        }
        return null;
    }
}

