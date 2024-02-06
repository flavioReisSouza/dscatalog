package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController {

  private final CategoryService service;

  @GetMapping()
  public ResponseEntity<List<Category>> findAll() {
    List<Category> list = service.findAll();
    return ResponseEntity.ok().body(list);
  }
}
