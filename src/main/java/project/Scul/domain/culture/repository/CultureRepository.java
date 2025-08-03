package project.Scul.domain.culture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Scul.domain.culture.domain.Culture;

import java.util.Optional;

@Repository
public interface CultureRepository extends JpaRepository<Culture,Long> {
    Optional<Culture> getCultureByCultureId(Long cultureId);
}
