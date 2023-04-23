package pro.sky.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.ContactDetails;

@Repository
public interface ContactDetailsRepository extends JpaRepository<ContactDetails,Integer> {

}
