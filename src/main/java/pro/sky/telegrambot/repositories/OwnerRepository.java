package pro.sky.telegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Owner;

import java.util.List;


@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Owner getOwnerByChatId(Long chatID);
    List<Owner> findAll();
}
