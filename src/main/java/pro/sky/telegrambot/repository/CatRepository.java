package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Cat;

@Repository
public interface CatRepository extends JpaRepository<Cat,Integer> {
    Cat findCatById(Integer id);
}
