package com.lens.learningspringboot.learningspringbootch2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

/**
 * @author Greg Turnquist
 */
@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload-dir";

    private final ResourceLoader resourceLoader;

    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public Mono<Resource> findOneImage(String filename) {
        return Mono.fromSupplier(() ->
                resourceLoader.getResource(
                        "file:" + UPLOAD_ROOT + "/" + filename));
    }

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .flatMap(file -> file.transferTo(
                        Paths.get(UPLOAD_ROOT, file.filename()).toFile()))
                .then();
    }

    public Mono<Void> deleteImage(String filename) {
        return Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Pre-load some fake images
     *
     * @return Spring Boot {@link CommandLineRunner} automatically run after app context is loaded.
     */
    @Bean
    CommandLineRunner setUp() throws IOException {

        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file", new FileWriter(UPLOAD_ROOT + "/test"));

            FileCopyUtils.copy("Test file2", new FileWriter(UPLOAD_ROOT + "/test2"));

            FileCopyUtils.copy("Test file3", new FileWriter(UPLOAD_ROOT + "/test3"));
        };

    }
}

