package com.example.userregistration.service;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This interceptor is thread safe since it will be constructed per session, not per session factory.
 */
@Slf4j
public class MyInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        log.info("onSave");

        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        log.info("********************AUDIT INFO START*******************");
        log.info("Entity Name    :: " + entity.getClass());
        log.info("Previous state :: " + Arrays.deepToString(previousState));
        log.info("Current  state :: " + Arrays.deepToString(currentState));
        log.info("propertyNames  :: " + Arrays.deepToString(propertyNames));
        log.info("********************AUDIT INFO END*******************");
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        log.info("onDelete");

        super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public void postFlush(final Iterator entities) {
        log.info("postFlush");
        super.postFlush(entities);
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        log.info("afterTransactionBegin");
        super.afterTransactionBegin(tx);
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        log.info("beforeTransactionCompletion");
        super.beforeTransactionCompletion(tx);
    }
}