package ml.socshared.bstatistics.service.sentry;

public enum SentryTag {
    PostSummary("type", "post_summary"),
    GroupUpdate("type", "group_update"),
    PostUpdate("type", "post_update"),
    PostInfo("type", "get_post_info"),
    GroupOnline("type", "get_group_online"),
    ScheduledStatisticCollection("type", "scheduled_statistic_collection");

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

    private String sentryTag;
    private String type;
    public static final String service_name = "BST";

}
