package ml.socshared.bstatistics.service.sentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SentrySender {

    @Autowired
    SentryService sentry;

    public void sentryMessage(String message, Map<String, Object> additionalData, List<SentryTag> tags) {
        Map<String, String> tm = new HashMap<>();
        tm.put("service", SentryTag.service_name);
        for(SentryTag tag : tags) {
            tm.put(tag.type(), tag.value());
        }
        sentry.logMessage(message, tm, additionalData);
    }
}