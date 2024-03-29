package com.test.eclipselink;

import com.test.eclipselink.model.HotelBooking;
import com.test.eclipselink.model.RoomBookingId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Date;
import java.time.LocalDate;

/**
 * java -jar $DERBY_HOME/lib/derbyrun.jar server start
 * <p>
 * connect 'jdbc:derby://localhost:1527/test';
 *
 * @author adamato
 */
public class EmbIdBookingTest {

    @Test
    public void persist() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("emb_booking");
        final EntityManager em = emf.createEntityManager();

        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        RoomBookingId roomBookingId = new RoomBookingId();
        Date date = Date.valueOf(LocalDate.of(2020, 10, 1));
        roomBookingId.setDateof(date);
        roomBookingId.setRoomNumber(23);

        HotelBooking hotelBooking = new HotelBooking();
        hotelBooking.setRoomBookingId(roomBookingId);
        hotelBooking.setCustomerId(1);
        hotelBooking.setPrice(45.5f);

        em.persist(hotelBooking);

        Assertions.assertNotNull(hotelBooking.getRoomBookingId());
        tx.commit();

        HotelBooking b = em.find(HotelBooking.class, hotelBooking.getRoomBookingId());
        Assertions.assertTrue(b == hotelBooking);
        Assertions.assertNotNull(b);
        RoomBookingId bookingId = b.getRoomBookingId();
        Assertions.assertNotNull(bookingId);
        Assertions.assertEquals(date, bookingId.getDateof());

        em.detach(hotelBooking);
        b = em.find(HotelBooking.class, hotelBooking.getRoomBookingId());
        Assertions.assertFalse(b == hotelBooking);
        Assertions.assertNotNull(b);

        HotelBooking b2 = em.find(HotelBooking.class, b.getRoomBookingId());
        Assertions.assertTrue(b2 == b);

        em.close();
        emf.close();
    }

}
