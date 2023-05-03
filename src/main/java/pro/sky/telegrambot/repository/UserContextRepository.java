package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.UserContext;

import java.util.Optional;

@Repository
public interface UserContextRepository extends JpaRepository<UserContext, Integer> {
   Optional <UserContext> findByChatId(Long chatId);
}
