package com.example.demo2.repository;

import com.example.demo2.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    List<Images> findByShop(int ShopID);
    List<Images> findById(int id);

}
