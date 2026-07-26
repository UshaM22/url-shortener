package com.urlshortener.repository;

import com.urlshortener.model.UrlDetail;
import com.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlDetailRepository extends JpaRepository<UrlDetail, Long> {

    Optional<UrlDetail> findByShortCode(String shortCode);

    Boolean existsByShortCode(String shortCode);


    List<UrlDetail> findByUser(User user);
}
