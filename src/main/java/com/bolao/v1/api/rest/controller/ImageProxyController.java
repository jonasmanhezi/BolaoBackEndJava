package com.bolao.v1.api.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;

@RestController
@RequestMapping("/images")
@Slf4j
public class ImageProxyController {

    private final RestClient restClient = RestClient.builder()
            .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            .build();

    @Value("${app.images.cache-dir:./team-logos-cache}")
    private String cacheDir;

    @GetMapping("/teams/{filename}")
    public ResponseEntity<byte[]> getTeamLogo(@PathVariable String filename) {
        String file = filename.endsWith(".png") || filename.endsWith(".svg") ? filename : filename + ".png";

        String externalUrl = "https://media.api-sports.io/football/teams/" + file;
        Path cachePath = Paths.get(cacheDir, file);

        try {
            if (Files.exists(cachePath)) {
                byte[] cachedBytes = Files.readAllBytes(cachePath);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(file.endsWith(".svg") 
                    ? MediaType.valueOf("image/svg+xml") 
                    : MediaType.IMAGE_PNG);
                headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic());
                return new ResponseEntity<>(cachedBytes, headers, HttpStatus.OK);
            }

            byte[] imageBytes = restClient.get()
                    .uri(externalUrl)
                    .retrieve()
                    .body(byte[].class);

            if (imageBytes == null || imageBytes.length == 0) {
                return servePlaceholder();
            }

            Files.createDirectories(Paths.get(cacheDir));
            Files.write(cachePath, imageBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(file.endsWith(".svg") 
                ? MediaType.valueOf("image/svg+xml") 
                : MediaType.IMAGE_PNG);
            headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic());

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.warn("Failed to fetch team logo from external CDN: {}", externalUrl, e);
            return servePlaceholder();
        }
    }

    private ResponseEntity<byte[]> servePlaceholder() {
        String svg = "<svg width='32' height='32' viewBox='0 0 32 32' fill='none' xmlns='http://www.w3.org/2000/svg'><circle cx='16' cy='16' r='15' fill='#e5e7eb'/><text x='16' y='21' text-anchor='middle' fill='#6b7280' font-size='14'>⚽</text></svg>";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/svg+xml"));
        headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(7)).cachePublic());
        return new ResponseEntity<>(svg.getBytes(), headers, HttpStatus.OK);
    }
}
