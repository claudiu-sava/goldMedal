package com.codecademy.goldmedal.repos;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepo extends CrudRepository<Country, Long> {
    Optional<Country> findByName(String name);
    List<Country> findAllByOrderByName();
    List<Country> findAllByOrderByGdp();
    List<Country> findAllByOrderByPopulation();
}
