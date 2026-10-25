package jammy.orderrenew.repository;

import jammy.orderrenew.model.entity.OrderEntity;
import jammy.orderrenew.model.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findAllByOrder(OrderEntity order);
}
