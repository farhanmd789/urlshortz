package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortcode(String shortcode);

    Optional<ShortUrl> findByOriginalUrl(String originalUrl);
}
