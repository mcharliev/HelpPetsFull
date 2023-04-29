package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.Owner;

@Repository
public interface DogRepository extends JpaRepository<Dog, Integer> {
    Dog findDogById(int id);

}



