package zerobase.stockdividends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockDividendsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockDividendsApplication.class, args);

//        try {
//            Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/KO/history?period1=-252374400&period2=1711929600&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
//            Document document = connection.get();
//
//            Elements eles = document.getElementsByAttributeValue("data-test", "historical-prices");
//            Element ele = eles.get(0);  // table 전체
//
//            Element tbody = ele.children().get(1);  // thead = 0, tbody = 1, tfoot = 2
//            for (Element e : tbody.children()) {
//                String txt = e.text();
//
//                if (!txt.endsWith("Dividend")) {
//                    continue;
//                }
//
//                String[] splits = txt.split(" ");
//                String month = splits[0];
//                int day = Integer.valueOf(splits[1].replace(",", ""));
//                int year = Integer.valueOf(splits[2]);
//                String dividend = splits[3];
//
//                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}