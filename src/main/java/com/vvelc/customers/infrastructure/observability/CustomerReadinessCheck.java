package com.vvelc.customers.infrastructure.observability;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import java.sql.Connection;

@Readiness
@ApplicationScoped
public class CustomerReadinessCheck implements HealthCheck {

    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (Connection conn = dataSource.getConnection()) {
            return HealthCheckResponse.up("Database connection OK");
        } catch (Exception e) {
            return HealthCheckResponse.down("Database not reachable: " + e.getMessage());
        }
    }
}
