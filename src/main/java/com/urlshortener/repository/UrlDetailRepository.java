package com.urlshortener.repository;

import com.urlshortener.model.UrlDetail;
import com.urlshortener.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UrlDetailRepository extends JpaRepository<UrlDetail, Long> {

    Optional<UrlDetail> findByShortCode(String shortCode);

    Boolean existsByShortCode(String shortCode);


    List<UrlDetail> findByUser(User user);
    @Transactional
    @Modifying
    @Query("UPDATE UrlDetail u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode")
    void incrementClickCount(@Param("shortCode") String shortCode);
}
