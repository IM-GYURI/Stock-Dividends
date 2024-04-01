package zerobase.stockdividends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.scraper.Scraper;
import zerobase.stockdividends.scraper.YahooFinanceScraper;

//@SpringBootApplication
public class StockDividendsApplication {
    public static void main(String[] args) {
//        SpringApplication.run(StockDividendsApplication.class, args);

        Scraper scraper = new YahooFinanceScraper();
//        var result = scraper.scrap(Company.builder().ticker("KO").build());
        var result = scraper.scrapCompanyByTicker("MMM");
        System.out.println(result);
    }
}