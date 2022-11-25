package com.ecommerce.user.web;

import com.ecommerce.user.Exception.UserNotFoundException;
import com.ecommerce.user.Repo.RepoUser;
import com.ecommerce.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class ControllerUser {
  RepoUser Userrepo;
    @GetMapping("/users")
    public ResponseEntity<List<User>> listeusers() {
        List<User> prods = (List<User>) Userrepo.findAll();
        if(prods.isEmpty()) throw new UserNotFoundException("Aucun user n'est disponible");

        return new ResponseEntity<>(prods, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> afficherUnProduit(@PathVariable int id) {

        Optional<User> us = Userrepo.findById(id);
        if(!us.isPresent())  throw new UserNotFoundException("Le produit correspondant à l'id " + id + " n'existe pas");

        return new ResponseEntity<>(us,HttpStatus.OK);
    }

  /*  @PutMapping("/updateProduit/{id}")
    public ResponseEntity<User> updateProduit(@RequestBody User us, @PathVariable int id){
        Optional<User> use = Userrepo.findById(id);
        if(use!=null){

            us.setNom(us.getNom());
            us.setEmail(us.getEmail());
            Userrepo.save(us);
            return new ResponseEntity<>(us,HttpStatus.OK);
        }
        return null;
    }
*/
    @PostMapping(value = "/Saveusers")
    public ResponseEntity<User> ajouterUser(@RequestBody User User) {
        User UserAdded = Userrepo.save(User);
        if (Objects.isNull(UserAdded)) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(UserAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<User> supprimeruser(@PathVariable int id){

        Userrepo.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
