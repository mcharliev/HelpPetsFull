package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "cat_owner_reports")
public class CatOwnerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "photo_report")
    private byte[] photoReport;

    @Column(name = "string_report")
    private String stringReport;

    @Column(name = "last_report")
    private LocalDateTime dateOfLastReport;

    @ManyToOne
    @JoinColumn(name = "cat_owner_id",referencedColumnName = "id")
    private CatOwner catOwner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Optional<byte[]> getPhotoReport() {
        return Optional.ofNullable(photoReport);
    }

    public void setPhotoReport(byte[] photoReport) {
        this.photoReport = photoReport;
    }

    public Optional<String> getStringReport() {
        return Optional.ofNullable(stringReport);
    }

    public void setStringReport(String stringReport) {
        this.stringReport = stringReport;
    }

    public LocalDateTime getDateOfLastReport() {
        return dateOfLastReport;
    }

    public void setDateOfLastReport(LocalDateTime dateOfLastReport) {
        this.dateOfLastReport = dateOfLastReport;
    }

    public CatOwner getCatOwner() {
        return catOwner;
    }

    public void setCatOwner(CatOwner catOwner) {
        this.catOwner = catOwner;
    }
}
