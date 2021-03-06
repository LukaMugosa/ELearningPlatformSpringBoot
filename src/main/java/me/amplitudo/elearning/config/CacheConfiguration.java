package me.amplitudo.elearning.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, me.amplitudo.elearning.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, me.amplitudo.elearning.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, me.amplitudo.elearning.domain.User.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Authority.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.User.class.getName() + ".authorities");
            createCache(cm, me.amplitudo.elearning.domain.Faculty.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Faculty.class.getName() + ".orientations");
            createCache(cm, me.amplitudo.elearning.domain.Faculty.class.getName() + ".orientationFaculties");
            createCache(cm, me.amplitudo.elearning.domain.Faculty.class.getName() + ".users");
            createCache(cm, me.amplitudo.elearning.domain.Orientation.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Orientation.class.getName() + ".users");
            createCache(cm, me.amplitudo.elearning.domain.Orientation.class.getName() + ".faculties");
            createCache(cm, me.amplitudo.elearning.domain.Orientation.class.getName() + ".courses");
            createCache(cm, me.amplitudo.elearning.domain.Course.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Course.class.getName() + ".notifications");
            createCache(cm, me.amplitudo.elearning.domain.Course.class.getName() + ".assignments");
            createCache(cm, me.amplitudo.elearning.domain.Course.class.getName() + ".orientations");
            createCache(cm, me.amplitudo.elearning.domain.Course.class.getName() + ".users");
            createCache(cm, me.amplitudo.elearning.domain.Year.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Year.class.getName() + ".users");
            createCache(cm, me.amplitudo.elearning.domain.Building.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Building.class.getName() + ".faculties");
            createCache(cm, me.amplitudo.elearning.domain.Notification.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Assignment.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Assignment.class.getName() + ".assignmentProfiles");
            createCache(cm, me.amplitudo.elearning.domain.AssignmentProfile.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Lecture.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Lecture.class.getName() + ".courses");
            createCache(cm, me.amplitudo.elearning.domain.Profile.class.getName());
            createCache(cm, me.amplitudo.elearning.domain.Profile.class.getName() + ".assignmentProfiles");
            createCache(cm, me.amplitudo.elearning.domain.Profile.class.getName() + ".lectures");
            createCache(cm, me.amplitudo.elearning.domain.Profile.class.getName() + ".notifications");
            createCache(cm, me.amplitudo.elearning.domain.Profile.class.getName() + ".assignments");
            createCache(cm, me.amplitudo.elearning.domain.Profile.class.getName() + ".faculties");
            createCache(cm, me.amplitudo.elearning.domain.Profile.class.getName() + ".courses");
            createCache(cm, me.amplitudo.elearning.domain.Course.class.getName() + ".lectures");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
