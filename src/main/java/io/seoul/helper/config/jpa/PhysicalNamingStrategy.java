package io.seoul.helper.config.jpa;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PhysicalNamingStrategy implements org.hibernate.boot.model.naming.PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        if (identifier == null)
            return null;
        identifier = convertToSnakeCase(identifier);
        identifier = convertToUpperCase(identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        if (identifier == null)
            return null;
        return identifier;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        identifier = convertToSnakeCase(identifier);
        identifier = convertToUpperCase(identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        identifier = convertToSnakeCase(identifier);
        identifier = convertToUpperCase(identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        identifier = convertToSnakeCase(identifier);
        identifier = convertToUpperCase(identifier);
        return identifier;
    }

    private Identifier convertToSnakeCase(final Identifier identifier) {
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
        return Identifier.toIdentifier(newName);
    }

    private Identifier convertToUpperCase(final Identifier identifier) {
        final String newName = identifier.getText().toUpperCase();
        return Identifier.toIdentifier(newName);
    }
}
