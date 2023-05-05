package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.DogOwner;


@Repository
public interface DogOwnerRepository extends JpaRepository<DogOwner, Integer> {
    DogOwner getOwnerByChatId(Long chatID);
    DogOwner findOwnerById(int id);
}
