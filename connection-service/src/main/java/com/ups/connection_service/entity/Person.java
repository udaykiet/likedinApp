package com.ups.connection_service.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private Long userId;

    private String name;
}
