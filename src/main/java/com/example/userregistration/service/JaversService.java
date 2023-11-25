package com.example.userregistration.service;

import org.javers.shadow.Shadow;

import java.util.List;

public interface JaversService<T> {

    List<Shadow<T>> getShadowsWithScopeDeepPlusQuery(Long id, Class entityClass, Integer page, Integer pageSize, Boolean isNextRecordRequired);

    List<Shadow<T>> getShadowsWithShadowScopeQuery(Long id, Class EntityClass,Integer page,Integer pageSize,Boolean isNextRecordRequired);



}
