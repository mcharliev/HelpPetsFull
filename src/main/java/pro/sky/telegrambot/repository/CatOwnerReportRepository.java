package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.CatOwnerReport;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatOwnerReportRepository extends JpaRepository<CatOwnerReport, Integer> {

    /*запрос который находит последний отчет в таблице по id овнера*/
    @Query(value = "SELECT * FROM cat_owner_reports ORDER BY cat_owner_id DESC, id DESC LIMIT 1", nativeQuery = true)
    CatOwnerReport findLastReportByOwnerId(Integer ownerId);

    List<CatOwnerReport> findByCatOwnerId(Integer ownerId);
}
