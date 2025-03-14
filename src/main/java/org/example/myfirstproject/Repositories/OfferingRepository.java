package org.example.myfirstproject.Repositories;

import org.example.myfirstproject.Models.Entities.Offering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long> {
}
