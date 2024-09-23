package com.example.CRUD.Repository;

import com.example.CRUD.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    public Client findByEmail (String email);

}
