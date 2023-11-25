package com.example.userregistration.impl;

import com.example.userregistration.service.JaversService;
import org.javers.core.Javers;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.repository.jql.ShadowScope;
import org.javers.shadow.Shadow;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author norulshahlam.mohsen
 */
@Service
public class JaversServiceImpl<T> implements JaversService<T> {

    private final Javers javers;

    public JaversServiceImpl(Javers javers) {
        this.javers = javers;
    }

    @Override
    public List<Shadow<T>> getShadowsWithScopeDeepPlusQuery(Long id, Class entityClass, Integer page, Integer pageSize, Boolean isNextRecordRequired) {
        boolean isPaginated = (page != null && pageSize != null);
        int extraRecord = Boolean.TRUE.equals(isNextRecordRequired) ? 1 : 0;
        JqlQuery jqlQuery;
        if (isPaginated) {
            jqlQuery = QueryBuilder.byInstanceId(id, entityClass).withScopeDeepPlus(Integer.MAX_VALUE).limit((page * pageSize) + extraRecord).skip((page - 1) * pageSize).build();
        } else {
            jqlQuery = QueryBuilder.byInstanceId(id, entityClass).withScopeDeepPlus(Integer.MAX_VALUE).build();
        }
        List<Shadow<T>> shadows = javers.findShadows(jqlQuery);
        return shadows;
    }

    @Override
    public List<Shadow<T>> getShadowsWithShadowScopeQuery(Long id, Class EntityClass, Integer page, Integer pageSize, Boolean isNextRecordRequired) {

        boolean isPaginated = (page != null && pageSize != null);
        int extraRecord = Boolean.TRUE.equals(isNextRecordRequired) ? 1 : 0;
        JqlQuery jqlQuery;
        if (isPaginated)
            jqlQuery = QueryBuilder.byInstanceId(id, EntityClass).withShadowScope(ShadowScope.DEEP_PLUS).limit((page * pageSize) + extraRecord).skip((page - 1) * pageSize).build();
        else
            jqlQuery = QueryBuilder.byInstanceId(id, EntityClass).withShadowScope(ShadowScope.DEEP_PLUS).build();
        List<Shadow<T>> shadows = javers.findShadows(jqlQuery);
        return shadows;
    }
}
