package ru.itwizardry.micro.product.service;

import ru.itwizardry.micro.product.service.dto.CreateProductDTO;

import java.util.concurrent.ExecutionException;

public interface ProductService {

    String createProduct(CreateProductDTO createProductDTO) throws ExecutionException, InterruptedException;
}