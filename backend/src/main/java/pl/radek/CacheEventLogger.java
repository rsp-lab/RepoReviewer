package pl.radek;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import pl.radek.dto.ScanFullReview;

@Slf4j
public class CacheEventLogger implements CacheEventListener<Object, ScanFullReview>
{
    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends ScanFullReview> cacheEvent) {
        String oldId = cacheEvent.getOldValue() != null ? cacheEvent.getOldValue().repositoryUrl() : null;
        String newId = cacheEvent.getNewValue() != null ? cacheEvent.getNewValue().repositoryUrl() : null;
        log.debug("📦 Cache event \u001B[33m{}\u001B[0m for review with key ({}). Old value = ({}), new value = ({})",
                cacheEvent.getType().toString(),
                cacheEvent.getKey(),
                oldId,
                newId
        );
    }
}
