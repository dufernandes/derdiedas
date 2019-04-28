package com.derdiedas.repository;

import com.derdiedas.model.DefaultSettings;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DefaultSettingsRepository extends CrudRepository<DefaultSettings, Long> {

    @Override
    @Caching(evict = @CacheEvict(value = "findDefaultSettings", key = "#p0.name"))
    <S extends DefaultSettings> S save(S s);

    @Cacheable("findDefaultSettings")
    @Query("from DefaultSettings where name = com.derdiedas.model.DefaultSettings.DEFAULT_NAME")
    DefaultSettings findDefault();
}
