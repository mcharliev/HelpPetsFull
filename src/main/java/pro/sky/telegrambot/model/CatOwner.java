package pro.sky.telegrambot.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import pro.sky.telegrambot.enam.ProbationaryStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "cat_owners")
public class CatOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "catOwner")
    @JsonIgnore
    private Collection<CatOwnerReport> reports;

    @Column(name = "start_probation")
    private LocalDateTime dateOfStartProbation;

    @Column(name = "end_probation")
    private LocalDateTime dateOfEndProbation;

    @Column(name = "probationary_status")
    @Enumerated(EnumType.STRING)
    private ProbationaryStatus probationaryStatus;
    @Column(name = "period_extend")
    private int periodExtend;

    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Cat cats;

    public int getPeriodExtend() {
        return periodExtend;
    }

    public void setPeriodExtend(int periodExtend) {
        if (periodExtend >= 0 && periodExtend < 15)
            this.periodExtend = periodExtend;
    }

    public LocalDateTime getDateOfStartProbation() {
        return dateOfStartProbation;
    }

    public void setDateOfStartProbation(LocalDateTime dateOfStartProbation) {
        this.dateOfStartProbation = dateOfStartProbation;
    }

    public LocalDateTime getDateOfEndProbation() {
        return dateOfEndProbation;
    }

    public void setDateOfEndProbation(LocalDateTime dateOfEndProbation) {
        this.dateOfEndProbation = dateOfEndProbation;
    }

    public ProbationaryStatus getProbationaryStatus() {
        return probationaryStatus;
    }

    public void setProbationaryStatus(ProbationaryStatus probationaryStatus) {
        this.probationaryStatus = probationaryStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Cat getCats() {
        return cats;
    }

    public void setCats(Cat cats) {
        this.cats = cats;
    }

    public Collection<CatOwnerReport> getReports() {
        return reports;
    }

    public void setReports(Collection<CatOwnerReport> reports) {
        this.reports = reports;
    }

}
