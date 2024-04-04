package zerobase.stockdividends.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.model.ScrapedResult;
import zerobase.stockdividends.persist.CompanyRepository;
import zerobase.stockdividends.persist.DividendRepository;
import zerobase.stockdividends.persist.entity.CompanyEntity;
import zerobase.stockdividends.persist.entity.DividendEntity;
import zerobase.stockdividends.scraper.Scraper;
import zerobase.stockdividends.model.constants.CacheKey;

import java.util.List;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {
    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)   // finance의 모든 데이터를 지움
    @Scheduled(cron = "${scheduler.scrap.yahoo}")    // 매일 정각에 실행
    public void yahooFinanceScehduling() {
        log.info("scraping scheduler is started");
        // 저장된 회사 목록 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(new Company(company.getTicker(), company.getName()));

            // 스크래핑한 배당금 정보 중 DB에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    // 디비든 모델 -> 디비든 엔티티
                    .map(e -> new DividendEntity(company.getId(), e))
                    // 엘리먼트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists) {
                            this.dividendRepository.save(e);
                        }
                    });

            // 부하 방지
            try {
                Thread.sleep(3000); // 3초
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
