package com.dzaitsev.marshmallows.dao.repository.custom.impl;

import com.dzaitsev.marshmallows.dao.entity.OrderEntity;
import com.dzaitsev.marshmallows.dao.entity.OrderLineEntity;
import com.dzaitsev.marshmallows.dao.repository.custom.OrderRepositoryCustom;
import com.dzaitsev.marshmallows.dto.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<OrderEntity> findByCriteria(LocalDate start, LocalDate end, List<OrderStatus> statuses) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);

        Root<OrderEntity> order = cq.from(OrderEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (start != null) {
            predicates.add(cb.greaterThanOrEqualTo(order.get("deadline"), start));
        }
        if (end != null) {
            predicates.add(cb.lessThan(order.get("deadline"), start));
        }
        if (statuses != null && !statuses.isEmpty()) {
            List<Predicate> statusPredicates = new ArrayList<>();
            if (statuses.contains(OrderStatus.SHIPPED)) {
                statusPredicates.add(cb.isTrue(order.get("shipped")));
            }
            if (statuses.contains(OrderStatus.IN_PROGRESS)) {
                Subquery<OrderEntity> subquery = cq.subquery(OrderEntity.class);
                Root<OrderLineEntity> inProgress = subquery.from(OrderLineEntity.class);
                subquery.where(cb.and(cb.equal(inProgress.get("order").get("id"), order.get("id"))), cb.isFalse(inProgress.get("done")));

                Subquery<OrderEntity> subquery1 = cq.subquery(OrderEntity.class);
                Root<OrderLineEntity> inProgress1 = subquery1.from(OrderLineEntity.class);
                subquery1.where(cb.and(cb.equal(inProgress1.get("order").get("id"), order.get("id"))), cb.isTrue(inProgress1.get("done")));
                statusPredicates.add(cb.and(cb.isFalse(order.get("shipped")), cb.or(cb.exists(subquery), cb.exists(subquery1))));
            }
            if (statuses.contains(OrderStatus.DONE)) {
                Subquery<OrderEntity> subquery = cq.subquery(OrderEntity.class);
                Root<OrderLineEntity> inProgress = subquery.from(OrderLineEntity.class);
                subquery.where(cb.and(cb.equal(inProgress.get("order").get("id"), order.get("id"))), cb.isFalse(inProgress.get("done")));
                statusPredicates.add(cb.and(cb.isFalse(order.get("shipped")), cb.not(cb.exists(subquery))));
            }
            predicates.add(cb.or(statusPredicates.toArray(new Predicate[0])));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
