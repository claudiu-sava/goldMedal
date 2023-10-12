package com.codecademy.goldmedal.repos;
import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoldMedalRepo extends CrudRepository<GoldMedal, Long> {
    List<GoldMedal> findAllByCountryOrderByYearAsc(String country);
    List<GoldMedal> findAllByCountryOrderByYearDesc(String country);
    List<GoldMedal> findAllByCountryOrderBySeasonAsc(String country);
    List<GoldMedal> findAllByCountryOrderBySeasonDesc(String country);
    List<GoldMedal> findAllByCountryOrderByCityAsc(String country);
    List<GoldMedal> findAllByCountryOrderByCityDesc(String country);
    List<GoldMedal> findAllByCountryOrderByNameAsc(String country);
    List<GoldMedal> findAllByCountryOrderByNameDesc(String country);
    List<GoldMedal> findAllByCountryOrderByEventAsc(String country);
    List<GoldMedal> findAllByCountryOrderByEventDesc(String country);
    List<GoldMedal> findAllBySeasonAndCountryOrderByYearAsc(String season, String country);
    List<GoldMedal> findAllBySeason(String season);

    List<GoldMedal> findAllByCountry(String country);
    List<GoldMedal> findAllByGenderAndCountry(String gender, String country);
}
