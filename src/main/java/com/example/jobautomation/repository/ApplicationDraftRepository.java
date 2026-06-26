package com.example.jobautomation.repository;

import com.example.jobautomation.model.ApplicationDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDraftRepository extends JpaRepository<ApplicationDraft, Long> {
}
