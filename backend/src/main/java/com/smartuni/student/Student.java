package com.smartuni.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A JPA entity: one row in the "students" table.
 * Hibernate (the JPA provider) generates the schema from this class on startup (ddl-auto=create-drop).
 *
 * Lombok annotations, and why each one is here:
 *   @Builder                         new entities are built with Student.builder()...build(), never with a chain of setters.
 *   @AllArgsConstructor(PRIVATE)     @Builder needs an all-args constructor; private so callers must go through the builder.
 *   @NoArgsConstructor(PROTECTED)    JPA must be able to instantiate the entity reflectively (and subclass it for lazy proxies).
 *                                    Protected satisfies JPA while keeping "new Student()" out of application code.
 *   @Setter                          still needed: updating a MANAGED entity (dirty checking) mutates it in place.
 *   @Builder.Default                 without it the builder ignores the "= true" initialiser and active would be false.
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
