package zerobase.stockdividends.scraper;

import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
