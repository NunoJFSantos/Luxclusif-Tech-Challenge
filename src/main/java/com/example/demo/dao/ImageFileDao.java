package com.example.demo.dao;

import com.example.demo.model.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileDao extends JpaRepository<ImageFile, Integer> {
}
