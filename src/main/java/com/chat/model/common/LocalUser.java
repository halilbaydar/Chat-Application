package com.chat.model.common;

import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class LocalUser implements SimpUser {
    private final String name;
    private final Principal user;
    private final Map<String, SimpSession> userSessions = new ConcurrentHashMap(1);

    public LocalUser(String userName, Principal user) {
        Assert.notNull(userName, "User name must not be null");
        this.name = userName;
        this.user = user;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public Principal getPrincipal() {
        return this.user;
    }

    public boolean hasSessions() {
        return !this.userSessions.isEmpty();
    }

    @Nullable
    public SimpSession getSession(@Nullable String sessionId) {
        return sessionId != null ? this.userSessions.get(sessionId) : null;
    }

    public Set<SimpSession> getSessions() {
        return new HashSet(this.userSessions.values());
    }

    void addSession(SimpSession session) {
        this.userSessions.put(session.getId(), session);
    }

    void removeSession(String sessionId) {
        this.userSessions.remove(sessionId);
    }

    public boolean equals(@Nullable Object other) {
        return this == other || other instanceof SimpUser && this.getName().equals(((SimpUser) other).getName());
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    public String toString() {
        return "name=" + this.getName() + ", sessions=" + this.userSessions;
    }
}
