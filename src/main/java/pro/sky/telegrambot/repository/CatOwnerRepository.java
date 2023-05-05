package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.CatOwner;


@Repository
public interface CatOwnerRepository extends JpaRepository<CatOwner, Integer> {
    CatOwner getOwnerByChatId(Long chatID);

    CatOwner findOwnerById(int id);
}
