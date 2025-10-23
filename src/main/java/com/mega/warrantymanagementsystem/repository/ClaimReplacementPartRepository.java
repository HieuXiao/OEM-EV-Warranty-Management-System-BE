package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.ClaimReplacementPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimReplacementPartRepository extends JpaRepository<ClaimReplacementPart, Integer> {

    List<ClaimReplacementPart> findByPart_PartId(int partId);

}
