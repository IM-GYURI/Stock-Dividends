package zerobase.stockdividends.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.stockdividends.persist.entity.CompanyEntity;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByTicker(String ticker);
    Optional<CompanyEntity> findByName(String name);
    Optional<CompanyEntity> findByTicker(String ticker);
    Page<CompanyEntity> findByNameStartingWithIgnoreCase(String s, Pageable pageable);
}
