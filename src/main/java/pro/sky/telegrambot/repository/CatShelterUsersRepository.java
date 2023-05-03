package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.CatShelterUser;

public interface CatShelterUsersRepository extends JpaRepository<CatShelterUser, Integer> {
}
