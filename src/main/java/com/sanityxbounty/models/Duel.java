package com.sanityxbounty.models;

import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class Duel {

    private final UUID requester;
    private final UUID target;
    private boolean accepted;
    private long expiresAt;

    public Duel(UUID requester, UUID target, boolean accepted, long expiresAt) {
        this.requester = requester;
        this.target = target;
        this.accepted = accepted;
        this.expiresAt = expiresAt;
    }

    public UUID getRequester() {
        return requester;
    }

    public UUID getTarget() {
        return target;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired(long now) {
        return now >= expiresAt;
    }

    public void save(ConfigurationSection section) {
        section.set("requester", requester.toString());
        section.set("target", target.toString());
        section.set("accepted", accepted);
        section.set("expires-at", expiresAt);
    }

    public static Duel load(ConfigurationSection section) {
        if (section == null || !section.contains("requester") || !section.contains("target")) {
            return null;
        }

        UUID requester = UUID.fromString(section.getString("requester"));
        UUID target = UUID.fromString(section.getString("target"));
        boolean accepted = section.getBoolean("accepted", false);
        long expiresAt = section.getLong("expires-at", 0L);
        return new Duel(requester, target, accepted, expiresAt);
    }
}
