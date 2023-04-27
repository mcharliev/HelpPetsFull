package pro.sky.telegrambot.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import pro.sky.telegrambot.enam.ProbationaryStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private Collection<Report> reports;

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
    private Dog dogs;

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

    public Dog getDogs() {
        return dogs;
    }

    public void setDogs(Dog dogs) {
        this.dogs = dogs;
    }

    public Collection<Report> getReports() {
        return reports;
    }

    public void setReports(Collection<Report> reports) {
        this.reports = reports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return periodExtend == owner.periodExtend && Objects.equals(id, owner.id) && Objects.equals(chatId, owner.chatId) && Objects.equals(name, owner.name) && Objects.equals(dogs, owner.dogs) && Objects.equals(reports, owner.reports) && Objects.equals(dateOfStartProbation, owner.dateOfStartProbation) && Objects.equals(dateOfEndProbation, owner.dateOfEndProbation) && probationaryStatus == owner.probationaryStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, name, dogs, reports, dateOfStartProbation, dateOfEndProbation, probationaryStatus, periodExtend);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", dogs=" + dogs +
                ", reports=" + reports +
                ", dateOfStartProbation=" + dateOfStartProbation +
                ", dateOfEndProbation=" + dateOfEndProbation +
                ", probationaryStatus=" + probationaryStatus +
                ", periodExtend=" + periodExtend +
                '}';
    }
}
