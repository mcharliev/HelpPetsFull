package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Integer> {

    /*запрос который находит последний отчет в таблице по id овнера*/
    @Query(value = "SELECT * FROM reports ORDER BY owners_id DESC, id DESC LIMIT 1", nativeQuery = true)
    Report findLastReportByOwnerId(Integer ownerId);
    List<Report> findByOwnerId(Integer ownerId);

}
