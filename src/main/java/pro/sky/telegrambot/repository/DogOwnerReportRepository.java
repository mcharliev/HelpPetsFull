package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.DogOwnerReport;

import java.util.List;

@Repository
public interface DogOwnerReportRepository extends JpaRepository<DogOwnerReport,Integer> {

    /*запрос который находит последний отчет в таблице по id овнера*/
    @Query(value = "SELECT * FROM dog_owner_reports ORDER BY dog_owner_id DESC, id DESC LIMIT 1", nativeQuery = true)
    DogOwnerReport findLastReportByOwnerId(Integer ownerId);
    List<DogOwnerReport> findByDogOwnerId(Integer ownerId);

}
