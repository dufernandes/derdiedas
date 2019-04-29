package com.derdiedas.repository;

import com.derdiedas.model.DefaultSettings;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the entity {@link DefaultSettings}.
 */
public interface DefaultSettingsRepository extends CrudRepository<DefaultSettings, Long> {

    /**
     * Save a {@link DefaultSettings} in the database.
     *
     * @param s {@link DefaultSettings} entity to be saved.
     * @param <S> type of {@link DefaultSettings}
     * @return Saved {@link DefaultSettings} entity.
     */
    @Override
    @Caching(evict = @CacheEvict(value = "findDefaultSettings", key = "#p0.name"))
    <S extends DefaultSettings> S save(S s);

    /**
     * Find the default {@link DefaultSettings} stored
     * in the database. it is recognized by the
     * name {@link DefaultSettings#DEFAULT_NAME}.
     *
     * @return Default {@link DefaultSettings} entity.
     */
    @Cacheable("findDefaultSettings")
    @Query("from DefaultSettings where name = com.derdiedas.model.DefaultSettings.DEFAULT_NAME")
    DefaultSettings findDefault();
}
