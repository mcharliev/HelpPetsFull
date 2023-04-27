package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.repository.ReportRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    public Report findLastReportByOwnerId(Integer id) {
        return reportRepository.findLastReportByOwnerId(id);
    }

    public void saveTextInNewReport(String text,
                                       Owner owner,
                                       LocalDateTime localDateTime) {
        Report report = new Report();
        report.setStringReport(text);
        report.setOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveTextInExistingReport(Report report,
                                          String text,
                                          Owner owner,
                                          LocalDateTime localDateTime) {
        report.setStringReport(text);
        report.setOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveImageInNewReport(byte[] image,
                                        Owner owner,
                                        LocalDateTime localDateTime) {
        Report report = new Report();
        report.setPhotoReport(image);
        report.setOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public void saveImageInExistingReport(Report report,
                                           byte[] image,
                                           Owner owner,
                                           LocalDateTime localDateTime) {
        report.setPhotoReport(image);
        report.setOwner(owner);
        report.setDateOfLastReport(localDateTime);
        saveReport(report);
    }

    public List<Report> findAllReports(){
        return reportRepository.findAll();
    }
}
