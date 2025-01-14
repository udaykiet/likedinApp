package com.ups.connection_service.repository;

import com.ups.connection_service.entity.Person;
import org.neo4j.cypherdsl.core.Where;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person , Long> {

    Optional<Person> findByName(String name);

    @Query("MATCH (p:Person) -[:CONNECTED_TO]- (connectedPersons:Person) " +
            "WHERE p.userId = $userId " +
            " RETURN connectedPersons")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (p1:Person) -[r:REQUESTED_TO] -> (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean connectionRequestExist(Long senderId  , Long receiverId);


    @Query("MATCH (p1:Person) -[r:CONNECTED_TO] - (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId  , Long receiverId);

    @Query("MATCH (p1:Person) , (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId  , Long receiverId);


    @Query("MATCH (p1:Person) -[r:REQUESTED_TO]-> (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r " +
            "CREATE (p1) - [:CONNECTED_TO] -> (p2)")
    void acceptConnectionRequest(Long senderId , Long receiverId);


    @Query("MATCH (p1:Person) -[r:REQUESTED_TO]-> (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r")
    void rejectConnectionRequest(Long senderId , Long receiverId);

}
