package com.congty9a4.backend.repository;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.StandardBasicTypes;

/**
 * Custom PostgreSQL dialect that registers Full Text Search functions
 * This allows us to use PostgreSQL FTS functions in JPA Criteria API and Specifications
 * References: https://supabase.com/docs/guides/database/full-text-search
 */



public class PostgreFullTextSearchDialect extends PostgreSQLDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);

        SqmFunctionRegistry registry = functionContributions.getFunctionRegistry();
        BasicTypeRegistry basicTypeRegistry = functionContributions.getTypeConfiguration().getBasicTypeRegistry();

        // Register to_tsvector function
        registry.registerPattern(
                "to_tsvector",
                "to_tsvector(?1, ?2)",
                basicTypeRegistry.resolve(StandardBasicTypes.STRING)
        );

        // Register plainto_tsquery function
        registry.registerPattern(
                "plainto_tsquery",
                "plainto_tsquery(?1, ?2)",
                basicTypeRegistry.resolve(StandardBasicTypes.STRING)

        );

        registry.registerPattern(
                "to_tsquery",
                "to_tsquery(?1, ?2)",
                basicTypeRegistry.resolve(StandardBasicTypes.STRING)

        );

        // Register websearch_to_tsquery function
        registry.registerPattern(
                "websearch_to_tsquery",
                "websearch_to_tsquery(?1, ?2)",
                basicTypeRegistry.resolve(StandardBasicTypes.STRING)
        );

        // Register ts_rank function
        registry.registerPattern(
                "ts_rank",
                "ts_rank(?1, ?2)",
                basicTypeRegistry.resolve(StandardBasicTypes.DOUBLE)
        );

        // Register @@ operator as a function
        registry.registerPattern(
                "ts_match",
                "(?1 @@ ?2)",
                basicTypeRegistry.resolve(StandardBasicTypes.BOOLEAN)
        );
    }
}

