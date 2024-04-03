package zerobase.stockdividends.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.model.Dividend;
import zerobase.stockdividends.model.ScrapedResult;
import zerobase.stockdividends.persist.CompanyRepository;
import zerobase.stockdividends.persist.DividendRepository;
import zerobase.stockdividends.persist.entity.CompanyEntity;
import zerobase.stockdividends.persist.entity.DividendEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {
        // 회사명을 기준으로 회사 정보 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                                        .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다."));

        // 조회된 회사 아이디로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 결과 조합 후 반환
        List<Dividend> dividends = new ArrayList<>();
        for (var entity : dividendEntities) {
            dividends.add(Dividend.builder()
                            .date(entity.getDate())
                            .dividend(entity.getDividend())
                            .build()
            );
        }

        return new ScrapedResult(Company.builder()
                                    .ticker(company.getTicker())
                                    .name(company.getName())
                                    .build(),
                                dividends
        );
    }
}
