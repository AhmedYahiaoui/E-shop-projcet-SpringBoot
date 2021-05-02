package com.example.demo2.repository;
import java.util.List;
import com.example.demo2.model.Shop;
import com.example.demo2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByNameContaining(String title);

    Shop findByName(String title);
    List<Shop> findByCountry(String country);
    List<Shop> findByDepartement(String Departement);
    List<Shop> findByCity(String City);

    List<Shop> findByUser(long id);


//    Shop save(Shop shop, User user);
}
