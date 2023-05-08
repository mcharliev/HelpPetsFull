package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.model.Dog;

import java.time.LocalDate;

@Repository
public interface DogRepository extends JpaRepository<Dog, Integer> {
    Dog findDogById(int id);
    Dog findByNameAndBirthDateAndBreed(String name,
                                       LocalDate birthDate,
                                       String breed);
}



