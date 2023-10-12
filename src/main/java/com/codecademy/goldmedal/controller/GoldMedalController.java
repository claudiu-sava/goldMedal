package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repos.CountryRepo;
import com.codecademy.goldmedal.repos.GoldMedalRepo;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    // xTODO: declare references to your repositories
    @Autowired
    private  CountryRepo countryRepo;
    @Autowired
    private  GoldMedalRepo goldMedalRepo;



    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                if (ascendingOrder){
                    medalsList = goldMedalRepo.findAllByCountryOrderByYearAsc(countryName);// xTODO: list of medals sorted by year in the given order
                } else {
                    medalsList = goldMedalRepo.findAllByCountryOrderByYearDesc(countryName);// xTODO: list of medals sorted by year in the given order
                }
                break;

            case "season":
                if(ascendingOrder){
                    medalsList = goldMedalRepo.findAllByCountryOrderBySeasonAsc(countryName);// xTODO: list of medals sorted by season in the given order
                } else {
                    medalsList = goldMedalRepo.findAllByCountryOrderBySeasonDesc(countryName);// xTODO: list of medals sorted by season in the given order
                }
                break;

            case "city":
                if(ascendingOrder){
                    medalsList = goldMedalRepo.findAllByCountryOrderByCityAsc(countryName);// xTODO: list of medals sorted by city in the given order
                } else {
                    medalsList = goldMedalRepo.findAllByCountryOrderByCityDesc(countryName);// xTODO: list of medals sorted by city in the given order
                }
                break;

            case "name":
                if(ascendingOrder){
                    medalsList = goldMedalRepo.findAllByCountryOrderByNameAsc(countryName);// xTODO: list of medals sorted by athlete's name in the given order
                } else {
                    medalsList = goldMedalRepo.findAllByCountryOrderByNameDesc(countryName);// xTODO: list of medals sorted by athlete's name in the given order
                }
                break;

            case "event":
                if(ascendingOrder){
                    medalsList = goldMedalRepo.findAllByCountryOrderByEventAsc(countryName);// xTODO: list of medals sorted by event in the given order
                } else {
                    medalsList = goldMedalRepo.findAllByCountryOrderByEventDesc(countryName);// xTODO: list of medals sorted by event in the given order
                }
                break;

            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }


    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        var countryOptional = countryRepo.findByName(countryName); // xTODO: get the country; this repository method should return a java.util.Optional
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        var goldMedalCount = goldMedalRepo.findAllByCountry(countryName).size();// xTODO: get the medal count

        var summerWins = goldMedalRepo.findAllBySeasonAndCountryOrderByYearAsc("Summer", countryName); // xTODO: get the collection of wins at the Summer Olympics, sorted by year in ascending order
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        var totalSummerEvents = goldMedalRepo.findAllBySeason("Summer").size(); // xTODO: get the total number of events at the Summer Olympics
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        var winterWins = goldMedalRepo.findAllBySeasonAndCountryOrderByYearAsc("Winter", countryName);// xTODO: get the collection of wins at the Winter Olympics
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = goldMedalRepo.findAllBySeason("Winter").size();// xTODO: get the total number of events at the Winter Olympics, sorted by year in ascending order

        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = goldMedalRepo.findAllByGenderAndCountry("Women", countryName).size();// xTODO: get the number of wins by female athletes
        var numberEventsWonByMaleAthletes = goldMedalRepo.findAllByGenderAndCountry("Men", countryName).size();// xTODO: get the number of wins by male athletes

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);

    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;

        switch (sortBy) {
            case "name":
                countries = countryRepo.findAllByOrderByName();// xTODO: list of countries sorted by name in the given order
                break;
            case "gdp":
                countries = countryRepo.findAllByOrderByGdp();// xTODO: list of countries sorted by gdp in the given order
                break;
            case "population":
                countries = countryRepo.findAllByOrderByPopulation();// xTODO: list of countries sorted by population in the given order
                break;
            case "medals":
            default:
                countries = countryRepo.findAllByOrderByName();// xTODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }



        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = goldMedalRepo.findAllByCountry(country.getName()).size();// xTODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }


}
