package com.test.eclipselink;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.test.eclipselink.model.Stove;

public class StoveTest {
    private static EntityManagerFactory emf;

    @BeforeAll
    public static void beforeAll() {
        emf = Persistence.createEntityManagerFactory("stoves");
    }

    @AfterAll
    public static void afterAll() {
        emf.close();
    }

    private Stove create4BurnersStove() {
        Stove stove = new Stove();
        stove.setModel("My Model");
        stove.setNumberOfBurners(4);
        stove.setInduction(false);
        return stove;
    }

    private Stove createInductionStove() {
        Stove stove = new Stove();
        stove.setModel("Smeg");
        stove.setNumberOfBurners(6);
        stove.setInduction(true);
        return stove;
    }

    private Stove create8BurnersStove() {
        Stove stove = new Stove();
        stove.setModel("Smeg");
        stove.setNumberOfBurners(8);
        stove.setInduction(false);
        return stove;
    }

    @Test
    public void isTrueCriteria() {
        final EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Stove stove = create4BurnersStove();
        em.persist(stove);
        Stove s_4 = em.find(Stove.class, stove.getId());
        Assertions.assertTrue(stove == s_4);

        stove = create8BurnersStove();
        em.persist(stove);
        Stove s_8 = em.find(Stove.class, stove.getId());
        Assertions.assertTrue(stove == s_8);

        stove = createInductionStove();
        em.persist(stove);
        Stove s_induction = em.find(Stove.class, stove.getId());
        Assertions.assertTrue(stove == s_induction);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Stove> cq = cb.createQuery(Stove.class);
        Root<Stove> root = cq.from(Stove.class);

        cb.and();

        // 'induction' is true
        Predicate isTrue = cb.isTrue(root.get("induction"));
        cq.where(isTrue);

        cq.select(root);

        TypedQuery<Stove> typedQuery = em.createQuery(cq);
        List<Stove> stoves = typedQuery.getResultList();

        Assertions.assertEquals(1, stoves.size());
        CollectionUtils.containsAll(stoves, Arrays.asList(s_induction));

        // 'induction' is false
        Predicate isFalse = cb.isFalse(root.get("induction"));
        cq.where(isFalse);

        cq.select(root);

        typedQuery = em.createQuery(cq);
        stoves = typedQuery.getResultList();

        Assertions.assertEquals(2, stoves.size());
        CollectionUtils.containsAll(stoves, Arrays.asList(s_4, s_8));

        em.remove(s_4);
        em.remove(s_8);
        em.remove(s_induction);

        tx.commit();
        em.close();
    }

}
