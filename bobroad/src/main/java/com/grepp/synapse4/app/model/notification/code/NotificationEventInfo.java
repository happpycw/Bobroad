package com.grepp.synapse4.app.model.notification.code;

import java.util.Map;

public record NotificationEventInfo(String name, Map<String, Object> data) {}
