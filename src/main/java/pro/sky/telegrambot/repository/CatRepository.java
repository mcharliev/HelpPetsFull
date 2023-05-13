package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.model.DogOwnerReport;

import java.time.LocalDate;

@Repository
public interface CatRepository extends JpaRepository<Cat, Integer> {
    Cat findCatById(Integer id);

    Cat findByNameAndBirthDateAndBreed(String name,
                                       LocalDate birthDate,
                                       String breed);
}
