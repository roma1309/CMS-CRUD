package com.example.demo.repo;

import com.example.demo.entity.ProductType;
import org.springframework.data.repository.CrudRepository;

public interface ProductTypeRepository extends CrudRepository<ProductType,Long> {
}
