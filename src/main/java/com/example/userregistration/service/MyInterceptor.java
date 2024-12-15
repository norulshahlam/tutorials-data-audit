package com.example.userregistration.service;


import com.example.userregistration.entity.ContactEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This interceptor is thread safe since it will be constructed per session, not per session factory.
 */
@Slf4j
public class MyInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        log.info("********************AUDIT INFO START*******************");
        log.info("Entity Name :: {}", entity.getClass().getSimpleName());

        if (entity instanceof ContactEntity) {
            IntStream.range(0, propertyNames.length)
                    .filter(i -> !Objects.equals(previousState[i], currentState[i])) // Filter only changed properties
                    .forEach(i -> log.info("Property Changed: {} | Old Value: {} | New Value: {}",
                            propertyNames[i], previousState[i], currentState[i])); // Log details of changed properties
        }
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
    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
        log.info("onCollectionRemove");
        super.onCollectionRemove(collection, key);
    }
}