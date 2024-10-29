package com.backend.investate.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.investate.enums.ProfileType;
import com.backend.investate.model.Profile;
/**
 * @author E Praveen Kumar
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    
    // Find a profile by name
    Profile findByName(String name);

    // Find a profile by profile type
    List<Profile> findByProfileType(ProfileType profileType);

    // Check if a profile exists by name
    boolean existsByName(String name);
    
    // Optional: Additional custom queries can be defined here
    
}

