package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Interview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Interview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

}
