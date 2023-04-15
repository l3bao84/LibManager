package com.example.LibManager.Generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.stream.Stream;

public class BorrowIDGenerator implements IdentifierGenerator {

    private String prefix = "BR";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String query = "SELECT borrowID from Borrow";
        Stream<String> count = session.createQuery(query, String.class).stream();

        Long max = count.map(o -> o.replace(prefix, "")).mapToLong(Long::parseLong).max().orElse(0L);
        return prefix +(String.format("%03d", max + 1));
    }
}
