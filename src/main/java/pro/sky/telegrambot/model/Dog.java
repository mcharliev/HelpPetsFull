package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "dogs")
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Column(name = "name")
    private String name;

    @Column(name = "breed")
    private String breed;

    @OneToOne
    @JoinColumn(name = "owners_id",referencedColumnName = "id")
    private Owner owner;

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return  Objects.equals(birthDate, dog.birthDate) && Objects.equals(name, dog.name) && Objects.equals(owner, dog.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, birthDate, name, owner);
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", birthDate=" + birthDate +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
}
