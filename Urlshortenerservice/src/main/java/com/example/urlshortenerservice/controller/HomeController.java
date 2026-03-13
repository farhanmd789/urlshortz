package com.example.urlshortenerservice.controller;

import com.example.urlshortenerservice.dto.ShortenUrlRequest;
import com.example.urlshortenerservice.dto.ShortenUrlResponse;
import com.example.urlshortenerservice.entity.ShortUrl;
import com.example.urlshortenerservice.repository.ShortUrlRepository;
import com.example.urlshortenerservice.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final UrlShortenerService urlShortenerService;
    private final ShortUrlRepository shortUrlRepository;

    public HomeController(UrlShortenerService urlShortenerService, ShortUrlRepository shortUrlRepository) {
        this.urlShortenerService = urlShortenerService;
        this.shortUrlRepository = shortUrlRepository;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("shortenUrlRequest", new ShortenUrlRequest());
        return "index";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@Valid ShortenUrlRequest request,
                             BindingResult bindingResult,
                             HttpServletRequest httpRequest,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("shortenUrlRequest", request);
            return "index";
        }

        ShortUrl shortUrl = urlShortenerService.createShortUrl(request.getUrl());

        String baseUrl = httpRequest.getScheme() + "://" + httpRequest.getServerName();
        if (httpRequest.getServerPort() != 80 && httpRequest.getServerPort() != 443) {
            baseUrl += ":" + httpRequest.getServerPort();
        }

        ShortenUrlResponse response = new ShortenUrlResponse();
        response.setShortUrl(baseUrl + "/" + shortUrl.getShortcode());
        response.setShortCode(shortUrl.getShortcode());
        response.setOriginalUrl(shortUrl.getOriginalUrl());
        response.setCreatedAt(shortUrl.getCreatedAt());
        response.setExpiresAt(shortUrl.getExpiredAt());

        model.addAttribute("shortenUrlRequest", new ShortenUrlRequest());
        model.addAttribute("result", response);

        return "index";
    }

    @GetMapping("/{shortCode}")
    public String redirectToOriginal(@PathVariable String shortCode) {
        return urlShortenerService.resolveAndTrack(shortCode)
                .map(shortUrl -> "redirect:" + shortUrl.getOriginalUrl())
                .orElse("redirect:/");
    }

    @GetMapping("/analytics")
    public String analytics(HttpServletRequest httpRequest, Model model) {
        List<ShortUrl> urls = shortUrlRepository.findAll(Sort.by(Sort.Direction.DESC, "visitCount"));

        String baseUrl = httpRequest.getScheme() + "://" + httpRequest.getServerName();
        if (httpRequest.getServerPort() != 80 && httpRequest.getServerPort() != 443) {
            baseUrl += ":" + httpRequest.getServerPort();
        }

        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("urls", urls);
        return "analytics";
    }
}
