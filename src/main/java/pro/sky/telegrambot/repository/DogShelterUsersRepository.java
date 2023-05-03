package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.DogShelterUser;

public interface DogShelterUsersRepository extends JpaRepository <DogShelterUser,Integer> {
}
