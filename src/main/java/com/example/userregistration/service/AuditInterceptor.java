package com.example.userregistration.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

@Slf4j
public class AuditInterceptor implements Interceptor {


    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, org.hibernate.type.Type[] types) throws CallbackException {

        if (log.isDebugEnabled()) {
            log.debug("********************AUDIT INFO START*******************");
            log.debug("Entity Name    :: " + entity.getClass());
            log.debug("Previous state :: " + Arrays.deepToString(previousState));
            log.debug("Current  state :: " + Arrays.deepToString(currentState));
            log.debug("propertyNames  :: " + Arrays.deepToString(propertyNames));
            log.debug("********************AUDIT INFO END*******************");
        }
        return false;
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, org.hibernate.type.Type[] types) throws CallbackException {
        log.info("1");
        return false;
    }


    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, org.hibernate.type.Type[] types) throws CallbackException {
        log.info("1");
        return false;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, org.hibernate.type.Type[] types) throws CallbackException {
        log.info("1");
    }

    @Override
    public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
        log.info("1");
    }

    @Override
    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
        log.info("1");
    }

    @Override
    public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
        log.info("1");
    }

    @Override
    public void preFlush(Iterator entities) throws CallbackException {
        log.info("1");
    }

    @Override
    public void postFlush(Iterator entities) throws CallbackException {
        log.info("1");
    }

    @Override
    public Boolean isTransient(Object entity) {
        log.info("1");
        return null;
    }

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, org.hibernate.type.Type[] types) {
        log.info("1");
        return new int[0];
    }

    @Override
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) throws CallbackException {
        log.info("1");
        return null;
    }

    @Override
    public String getEntityName(Object object) throws CallbackException {
        log.info("1");
        return null;
    }

    @Override
    public Object getEntity(String entityName, Serializable id) throws CallbackException {
        log.info("1");
        return null;
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        log.info("1");

    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        log.info("1");

    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        log.info("1");

    }

    @Override
    public String onPrepareStatement(String sql) {
        log.info("1");
        return null;
    }
}