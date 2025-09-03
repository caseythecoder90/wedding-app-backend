package com.wedding.backend.wedding_app.repository;
import com.wedding.backend.wedding_app.entity.ErrorDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorDefinitionEntity, String> {}
