package com.example.demo2.controller;

import com.example.demo2.model.Shop;
import com.example.demo2.model.User;
import com.example.demo2.repository.ShopRepository;
import com.example.demo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    public ShopController(UserRepository userRepository, ShopRepository shopRepository) {
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
    }

    private long CheckUserConnected() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> UserData = userRepository.findByUsername(userDetails.getUsername());
        return UserData.get().getId();

//        if (UserData.isPresent()) {
//            System.out.println("USER getId: ");
//            System.out.println(UserData.get().getId());
//        } else { System.out.println("UserData : is empty  ");}
    }



    @GetMapping("/")
    public ResponseEntity<List<Shop>> getAllShopsIndex(@RequestParam(required = false) String title) {
        try {
            List<Shop> shops = new ArrayList<Shop>();
            if (title == null)
                shopRepository.findAll().forEach(shops::add);
            else
                shopRepository.findByNameContaining(title).forEach(shops::add);
            if (shops.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(shops, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getAllShops(@RequestParam(required = false) String title) {
        try {
            List<Shop> shops = new ArrayList<Shop>();
            for (Shop name : shopRepository.findAll()) {
                if (name.getUser().getId() == CheckUserConnected()) {
                    System.out.println("shops --- "+name.getName()+"  Owner ---"+name.getUser().getId());
                    shops.add(name);
                }
            }
//          shopRepository.findByNameContaining(title).forEach(shops::add);
            if (shops.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(shops, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Value("${file.upload-dir}")
//    @Value("${spring.mvc.static-path-pattern}")
    String FILE_DIRECTORY;

    @PostMapping("/uploadFile")
    public ResponseEntity<Object> fileUpload(
            @RequestParam("File") MultipartFile file,Shop shop
    ) throws IOException{
        Shop _shop = shopRepository.save(
                new Shop(
                        shop.getName(),
                        shop.getAdresse(),
                        shop.getNumber(),
                        shop.getLat(),
                        shop.getLng(),
                        shop.getCountry(),
                        shop.getDepartement(),
                        shop.getCity(),
                        shop.getChaine(),
                        shop.getNomMainImage())
        );

        File myFile = new File(FILE_DIRECTORY+shop.getName()+"____"+shop.getLat()+"____"+".png");
        myFile.createNewFile();
        FileOutputStream fos =new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        return new ResponseEntity<Object>("The File Uploaded Successfully", HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<Object> createShop(
            @RequestParam("File") MultipartFile file,Shop shop
    ) throws IOException {
        try {
            Random random = new Random();
            int randomWithNextInt = random.nextInt();
            Optional<User> UserData = userRepository.findById(CheckUserConnected());

            Shop _shopp = new Shop();
            _shopp.setName(shop.getName());
            _shopp.setAdresse(shop.getAdresse());
            _shopp.setNumber(shop.getNumber());
            _shopp.setLat(shop.getLat());
            _shopp.setLng(shop.getLng());
            _shopp.setCountry(shop.getCountry());
            _shopp.setDepartement(shop.getDepartement());
            _shopp.setCity(shop.getCity());
            _shopp.setChaine(shop.getChaine());
            _shopp.setNomMainImage(shop.getName()+"____"+file.getSize()+"____"+shop.getLat()+"___"+randomWithNextInt+"___"+shop.getLng()+".png");
            _shopp.setUser(UserData.get());
            shopRepository.save(_shopp);

//            Shop _shop = shopRepository.save(
//                    new Shop(
//                            shop.getName(),
//                            shop.getAdresse(),
//                            shop.getNumber(),
//                            shop.getLat(),
//                            shop.getLng(),
//                            shop.getCountry(),
//                            shop.getDepartement(),
//                            shop.getCity(),
//                            shop.getChaine(),
//                            shop.getName()+"____"+file.getSize()+"____"+shop.getLat()+"___"+randomWithNextInt+"___"+shop.getLng()+".png")
//            );

            File myFile = new File(FILE_DIRECTORY+
                    shop.getName()+"____"+file.getSize()+"____"+shop.getLat()+"___"+randomWithNextInt+"___"+shop.getLng()+".png");
            myFile.createNewFile();
            FileOutputStream fos =new FileOutputStream(myFile);
            fos.write(file.getBytes());
            fos.close();

            return new ResponseEntity<>("The File Uploaded Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable("id") long id) {
        Optional<Shop> shopData = shopRepository.findById(id);

        if (shopData.isPresent()) {
            return new ResponseEntity<>(shopData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable("id") long id, @RequestBody Shop shop) {
        Optional<Shop> tutorialData = shopRepository.findById(id);

            if (tutorialData.isPresent()) {
                Shop _Shop = tutorialData.get();

                if (CheckUserConnected() == _Shop.getUser().getId()) {
                    _Shop.setName(shop.getName());
                    _Shop.setAdresse(shop.getAdresse());
                    _Shop.setNumber(shop.getNumber());
                    _Shop.setLat(shop.getLat());
                    _Shop.setLng(shop.getLng());
                    _Shop.setCountry(shop.getCountry());
                    _Shop.setDepartement(shop.getDepartement());
                    _Shop.setCity(shop.getCity());
                    _Shop.setChaine(shop.getChaine());
                    _Shop.setNomMainImage(shop.getNomMainImage());
                }
                return new ResponseEntity<>(shopRepository.save(_Shop), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteShop(@PathVariable("id") long id) {
        try {
            Optional<Shop> shop = shopRepository.findById(id);
            if(CheckUserConnected() == shop.get().getUser().getId()){
                shopRepository.deleteById(id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/allshops")
    public ResponseEntity<HttpStatus> deleteAllShops() {
        try {
            for (Shop shop : shopRepository.findAll()) {
                if (shop.getUser().getId() == CheckUserConnected()) {
                    System.out.println("shops --- "+shop.getName()+"  Owner ---"+shop.getUser().getId()+" -> DELETED ");
                    shopRepository.deleteById(shop.getId());
                }
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



//    @GetMapping("/tutorials/published")
//    public ResponseEntity<List<Shop>> findByPublished() {
//        try {
//            List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
//
//            if (tutorials.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(tutorials, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


}

