package com.example.demo2.controller;

import java.io.IOException;
import java.util.*;

import com.example.demo2.model.Images;
import com.example.demo2.model.Shop;
import com.example.demo2.model.User;
import com.example.demo2.repository.ImagesRepository;
import com.example.demo2.repository.ShopRepository;
import com.example.demo2.repository.UserRepository;
import com.example.demo2.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


//@Controller
@RestController
@CrossOrigin("http://localhost:8081")
public class FilesController {

    final ImagesRepository imagesRepository;
    final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public FilesController(ImagesRepository imagesRepository, ShopRepository shopRepository,UserRepository userRepository) {
        this.imagesRepository = imagesRepository;
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    FilesStorageService storageService;

    private long CheckUserConnected() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> UserData = userRepository.findByUsername(userDetails.getUsername());
        return UserData.get().getId();
    }


    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;


    @PostMapping("/api/uploadImages")
    public ResponseEntity<String> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            long id
    ) throws IOException {
        String message = "";
        Random random = new Random();
        System.out.println("id = " + id);

        try {
            Optional<Shop> tutorialData = shopRepository.findById(id);

            if (CheckUserConnected() == tutorialData.get().getUser().getId()) {
                List<String> fileNames = new ArrayList<>();
                Arrays.asList(files).stream().forEach(file -> {
                    int randomWithNextInt = random.nextInt();
//                Optional<Shop> tutorialData = shopRepository.findById(id);

                    if (tutorialData.isPresent()) {
                        Shop shopcu = tutorialData.get();
                        Images _images = imagesRepository.save(
                                new Images(
                                        randomWithNextInt + "____" + file.getSize() + "____" + file.getName() + "____" + new Date().getTime() + ".png",
                                        file.getSize() + "",
                                        new Date(),
                                        shopcu
                                ));
                        storageService.save(file, "____" + randomWithNextInt + "_" + file.getSize() + "_" + file.getName() + "__ID____" + id + "__" + new Date().getTime() + ".png");
                    } else {
                        System.out.println("else probs");
                    }

                });
                return ResponseEntity.status(HttpStatus.OK).body("Uploaded the files successfully: " + fileNames);
            }else{
                message = "Fail to upload files AUTH !";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        } catch (Exception e) {
            message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
}
