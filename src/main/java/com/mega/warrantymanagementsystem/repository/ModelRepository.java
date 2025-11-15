package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {
    boolean existsByModelName(String modelName);
    boolean existsByModelNameAndIdNot(String modelName, Long id);

}
