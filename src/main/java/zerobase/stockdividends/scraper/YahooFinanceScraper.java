package zerobase.stockdividends.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.model.Dividend;
import zerobase.stockdividends.model.ScrapedResult;
import zerobase.stockdividends.model.constants.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceScraper {
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final long START_TIME = 86400;   // 60 * 60 * 24

    public ScrapedResult scrap(Company company) {
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {
            long end = System.currentTimeMillis() / 1000;   // 초단위로 바꾸기 위해 1000으로 나눔

            String url = String.format(STATISTICS_URL, company.getTicker(),START_TIME, end);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0);  // table 전체

            Element tbody = tableEle.children().get(1);  // thead = 0, tbody = 1, tfoot = 2

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String txt = e.text();

                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(Dividend.builder()
                                    .date(LocalDateTime.of(year, month, day, 0, 0))
                                    .dividend(dividend)
                                    .build()
                );
            }
            scrapResult.setDividendEntities(dividends);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scrapResult;
    }

    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }
}
