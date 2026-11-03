package jammy.orderrenew.StockTest.product;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<StockEntity, String> {

    // SELECT ... FOR UPDATE : 트랜잭션이 끝날 때까지 해당 행을 다른 트랜잭션이 읽거나 수정하지 못하게 잠금
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM StockEntity s WHERE s.optionCode = :optionCode")
    Optional<StockEntity> findByOptionCodeForUpdate(@Param("optionCode") String optionCode);
}