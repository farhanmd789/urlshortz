package com.example.urlshortenerservice.service;

import com.example.urlshortenerservice.entity.ShortUrl;
import com.example.urlshortenerservice.repository.ShortUrlRepository;
import com.example.urlshortenerservice.util.ShortCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlShortenerService(ShortUrlRepository shortUrlRepository,
                               ShortCodeGenerator shortCodeGenerator) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    public ShortUrl createShortUrl(String originalUrl) {
        Optional<ShortUrl> existing = shortUrlRepository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return existing.get();
        }

        String shortcode = generateUniqueShortcode();

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortcode(shortcode);
        shortUrl.setOriginalUrl(originalUrl);

        return shortUrlRepository.save(shortUrl);
    }

    public Optional<String> getOriginalUrl(String shortcode) {
        return shortUrlRepository
                .findByShortcode(shortcode)
                .map(ShortUrl::getOriginalUrl);
    }

    public Optional<ShortUrl> resolveAndTrack(String shortcode) {
        Optional<ShortUrl> found = shortUrlRepository.findByShortcode(shortcode);
        if (found.isEmpty()) {
            return Optional.empty();
        }

        ShortUrl shortUrl = found.get();
        shortUrl.setVisitCount(shortUrl.getVisitCount() + 1);
        shortUrl.setLastVisitedAt(LocalDateTime.now());
        return Optional.of(shortUrlRepository.save(shortUrl));
    }

    private String generateUniqueShortcode() {
        String shortcode;
        int attempts = 0;
        do {
            if (attempts++ > 10) {
                throw new IllegalStateException("Unable to generate unique shortcode after multiple attempts");
            }
            shortcode = shortCodeGenerator.generate();
        } while (shortUrlRepository.findByShortcode(shortcode).isPresent());
        return shortcode;
    }
}
