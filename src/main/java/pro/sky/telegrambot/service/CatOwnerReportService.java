package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.model.CatOwner;
import pro.sky.telegrambot.model.CatOwnerReport;
import pro.sky.telegrambot.repository.CatOwnerReportRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class CatOwnerReportService {
    private final CatOwnerReportRepository catOwnerReportRepository;

    public CatOwnerReportService(CatOwnerReportRepository catOwnerReportRepository) {
        this.catOwnerReportRepository = catOwnerReportRepository;
    }


    public void saveReport(CatOwnerReport report) {
        catOwnerReportRepository.save(report);
    }

    public Optional<CatOwnerReport> findLastReportByOwnerId(Integer id) {
        return Optional.ofNullable(catOwnerReportRepository.findLastReportByOwnerId(id));
    }

    public void saveTextInNewReport(String text,
                                    CatOwner owner,
                                    LocalDateTime localDateTime) {
        CatOwnerReport report = new CatOwnerReport();
        report.setStringReport(text);
        report.setCatOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveTextInExistingReport(CatOwnerReport report,
                                         String text,
                                         CatOwner owner,
                                         LocalDateTime localDateTime) {
        report.setStringReport(text);
        report.setCatOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveImageInNewReport(byte[] image,
                                     CatOwner owner,
                                     LocalDateTime localDateTime) {
        CatOwnerReport report = new CatOwnerReport();
        report.setPhotoReport(image);
        report.setCatOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveImageInExistingReport(CatOwnerReport report,
                                          byte[] image,
                                         CatOwner owner,
                                          LocalDateTime localDateTime) {
        report.setPhotoReport(image);
        report.setCatOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public List<CatOwnerReport> findAllReports() {
        return catOwnerReportRepository.findAll();
    }

    public List<CatOwnerReport> findReportsByOwnerId(Integer ownerId) {
        List<CatOwnerReport> list = catOwnerReportRepository.findByCatOwnerId(ownerId);
        if (list.isEmpty()) {
            throw new NotFoundException();
        }
        return list;
    }
}
