package com.example.kafkatodb.Repository;

import com.example.kafkatodb.Entity.UserColorSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserColorSelectionRepository extends JpaRepository<UserColorSelection, Long> {
}
