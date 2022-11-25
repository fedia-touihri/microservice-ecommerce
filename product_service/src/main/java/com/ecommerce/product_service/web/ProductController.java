package com.ecommerce.product_service.web;


import com.ecommerce.product_service.Exception.ProductNotFoundException;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController //pour  indiquer à Spring que ce contrôleur est un contrôleur REST
public class ProductController {
    @Autowired
    ProductRepo productRepo;
    /*public ProductController(ProductRepo productRepo){
        this.productRepo = productRepo;
    }*/
    @GetMapping("/Produits")
    public ResponseEntity<List<Product>> listeProduits() {
        List<Product> prods = (List<Product>) productRepo.findAll();
        if(prods.isEmpty()) throw new ProductNotFoundException("Aucun produit n'est disponible à la vente");

        return new ResponseEntity<>(prods, HttpStatus.OK);
    }

    @GetMapping("/Produits/{id}")
    public ResponseEntity<Optional<Product>> afficherUnProduit(@PathVariable int id) {

        Optional<Product> prod = productRepo.findById(id);
        if(!prod.isPresent())  throw new ProductNotFoundException("Le produit correspondant à l'id " + id + " n'existe pas");

        return new ResponseEntity<>(prod,HttpStatus.OK);
    }

    @PutMapping("/updateProduit/{id}")
    public ResponseEntity<Product> updateProduit(@RequestBody Product prod,@PathVariable int id){
        Optional<Product> produ = productRepo.findById(id);
        if(produ!=null){
            prod.setNom(prod.getNom());
            prod.setPrix(prod.getPrix());
            productRepo.save(prod);
            System.out.println(prod.toString());
            return new ResponseEntity<>(prod,HttpStatus.OK);
        }
        return null;
    }

    @PostMapping(value = "/SaveProduits")
    public ResponseEntity<Product> ajouterProduit(@RequestBody Product product) {
        Product productAdded = productRepo.save(product);
        if (Objects.isNull(productAdded)) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Product> supprimer(@PathVariable int id){

            productRepo.deleteById(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
