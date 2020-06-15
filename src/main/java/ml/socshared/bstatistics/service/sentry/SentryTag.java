package ml.socshared.bstatistics.service.sentry;

public enum SentryTag {
    POST_SUMMARY("type", "post_summary"),
    GROUP_UPDATE("type", "group_update"),
    POST_UPDATE("type", "post_update"),
    POST_INFO("type", "get_post_info"),
    GROUP_ONLINE("type", "get_group_online"),
    SCHEDULED_STATISTIC_COLLECTION("type", "scheduled_statistic_collection");

    SentryTag(String t, String tag) {
        type = t;
        sentryTag = tag;
    }

    public String type() {
        return type;
    }
    public String value() {
        return sentryTag;
    }

    private final String sentryTag;
    private final String type;

}
