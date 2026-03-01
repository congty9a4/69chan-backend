package com.congty9a4.backend.annotation.profileTagName;

import com.congty9a4.backend.entity.Profile;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;

/**
 * Custom value generator for Profile tag names.
 * Generates unique tag names with the pattern: @{username}{sequence}
 * Example: @johndoe1, @johndoe2, etc.
 * This generator is invoked before INSERT operations to auto-generate
 * the keyName field when it's null.
 */
public class ProfileTagNameGenerator implements BeforeExecutionGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        // If value is already set, return it
        if (currentValue != null) {
            return currentValue;
        }

        if (owner instanceof Profile profile) {
            // Get the username from the associated user
            String username = profile.getUser() != null ? profile.getUser().getUsername() : "unknown";

            // Query to get the next sequence number for this username
            Long sequenceValue = getNextSequenceForUsername(session, username);

            // Generate the tag name with pattern: @{username}{sequence}
            return username + sequenceValue;
        }

        return null;
    }

    @Override
    public boolean generatedOnExecution() {
        return false;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }

    /**
     * Get the next sequence value for a given username.
     * Queries the database to find the highest sequence number for this username
     * and returns the next available number.
     */
    private Long getNextSequenceForUsername(SharedSessionContractImplementor session, String username) {
        String hql = "SELECT COALESCE(MAX(CAST(SUBSTRING(p.keyName, LENGTH(:prefix) + 1) AS long)), 0) + 1 " +
                     "FROM Profile p " +
                     "WHERE p.keyName LIKE :pattern";

        String pattern = username + "%";

        try {
            Long result = session.createQuery(hql, Long.class)
                    .setParameter("prefix", username)
                    .setParameter("pattern", pattern)
                    .getSingleResult();
            return result != null ? result : 1L;
        } catch (Exception e) {
            // If query fails (e.g., on first insert), return 1
            return 1L;
        }
    }
}

