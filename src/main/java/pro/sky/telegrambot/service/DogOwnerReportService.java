package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.model.DogOwnerReport;
import pro.sky.telegrambot.repository.DogOwnerReportRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DogOwnerReportService {
    private final DogOwnerReportRepository dogOwnerReportRepository;

    public DogOwnerReportService(DogOwnerReportRepository DogOwnerReportRepository) {
        this.dogOwnerReportRepository = DogOwnerReportRepository;
    }

    public void saveReport(DogOwnerReport report) {
        dogOwnerReportRepository.save(report);
    }

    public Optional<DogOwnerReport> findLastReportByOwnerId(Integer id) {
        return Optional.ofNullable(dogOwnerReportRepository.findLastReportByOwnerId(id));
    }

    public void saveTextInNewReport(String text,
                                    DogOwner owner,
                                    LocalDateTime localDateTime) {
        DogOwnerReport report = new DogOwnerReport();
        report.setStringReport(text);
        report.setDogOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveTextInExistingReport(DogOwnerReport report,
                                         String text,
                                         DogOwner owner,
                                         LocalDateTime localDateTime) {
        report.setStringReport(text);
        report.setDogOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveImageInNewReport(byte[] image,
                                     DogOwner owner,
                                     LocalDateTime localDateTime) {
        DogOwnerReport report = new DogOwnerReport();
        report.setPhotoReport(image);
        report.setDogOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveImageInExistingReport(DogOwnerReport report,
                                          byte[] image,
                                          DogOwner owner,
                                          LocalDateTime localDateTime) {
        report.setPhotoReport(image);
        report.setDogOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public List<DogOwnerReport> findAllReports() {
        return dogOwnerReportRepository.findAll();
    }

    public List<DogOwnerReport> findReportsByOwnerId(Integer ownerId) {
       return dogOwnerReportRepository.findByDogOwnerId(ownerId);
    }
}
