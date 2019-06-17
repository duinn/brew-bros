package com.duinn.brewbros.config;

import com.duinn.brewbros.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.*;

import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.spring.SSMCache;
import com.google.code.ssm.spring.SSMCacheManager;

import io.github.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    @Bean
    public CacheManager memcachedCacheManager(JHipsterProperties jHipsterProperties, List<CacheFactory> caches)
        throws Exception {

        if (!jHipsterProperties.getCache().getMemcached().isEnabled()) {
            // Note that Memcached cannot work with Spring Boot devtools
            // So it should be disabled in development mode
            log.debug("Memcached is disabled");
            return new NoOpCacheManager();
        }
        log.debug("Starting Memcached configuration");
        SSMCacheManager cacheManager = new SSMCacheManager();
        List<SSMCache> ssmCaches = new ArrayList<>();
        for (CacheFactory cache : caches) {
            SSMCache ssmCache =
                new SSMCache(cache.getObject(), jHipsterProperties.getCache().getMemcached().getExpiration(),
                    false);

            ssmCaches.add(ssmCache);
        }
        cacheManager.setCaches(ssmCaches);
        return cacheManager;
    }

    @Bean
    public CacheFactory usersByLoginCache(JHipsterProperties jHipsterProperties) {
        return this.createCache(UserRepository.USERS_BY_LOGIN_CACHE, jHipsterProperties);
    }

    @Bean
    public CacheFactory usersByEmailCache(JHipsterProperties jHipsterProperties) {
        return this.createCache(UserRepository.USERS_BY_EMAIL_CACHE, jHipsterProperties);
    }

    private CacheFactory createCache(String cacheName, JHipsterProperties jHipsterProperties) {
        if (!jHipsterProperties.getCache().getMemcached().isEnabled()) {
            // Note that Memcached cannot work with Spring Boot devtools
            // So it should be disabled in development mode
            return null;
        }
        CacheFactory defaultCache = new CacheFactory();
        defaultCache.setCacheName(cacheName);
        defaultCache.setCacheClientFactory(new MemcacheClientFactoryImpl());

        DefaultAddressProvider addressProvider = new DefaultAddressProvider();
        addressProvider.setAddress(jHipsterProperties.getCache().getMemcached().getServers());
        defaultCache.setAddressProvider(addressProvider);

        com.google.code.ssm.providers.CacheConfiguration cacheConfiguration = new com.google.code.ssm.providers
            .CacheConfiguration();
        cacheConfiguration.setConsistentHashing(true);
        cacheConfiguration.setUseBinaryProtocol(jHipsterProperties.getCache().getMemcached().isUseBinaryProtocol());
        defaultCache.setConfiguration(cacheConfiguration);

        return defaultCache;
    }
}
