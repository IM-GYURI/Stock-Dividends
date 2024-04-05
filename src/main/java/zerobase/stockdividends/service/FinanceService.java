package zerobase.stockdividends.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zerobase.stockdividends.exception.impl.NoCompanyException;
import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.model.Dividend;
import zerobase.stockdividends.model.ScrapedResult;
import zerobase.stockdividends.model.constants.CacheKey;
import zerobase.stockdividends.persist.CompanyRepository;
import zerobase.stockdividends.persist.DividendRepository;
import zerobase.stockdividends.persist.entity.CompanyEntity;
import zerobase.stockdividends.persist.entity.DividendEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 요청이 자주 들어오는가? -> 특정 데이터에 대한 요청이 몰릴 것 (Google, Apple 등) -> 그런 회사들의 배당금 정보를 캐싱해놓으면 좋을 듯
    // 자주 변경되는 데이터인가? -> 변경이 잦지 않음 (연 1회, 분기당 1회, 월 1회...)
    // ->> 캐싱 대상 적합
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        // 회사명을 기준으로 회사 정보 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                                        .orElseThrow(() -> new NoCompanyException());

        // 조회된 회사 아이디로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName()), dividends);
    }
}
