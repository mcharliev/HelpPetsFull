package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserContext {
    @Id
    private Long chatId;

    private boolean catShelter;
    private boolean dogShelter;

    public boolean isDogShelter() {
        return dogShelter;
    }

    public void setDogShelter(boolean dogShelter) {
        this.dogShelter = dogShelter;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isCatShelter() {
        return catShelter;
    }

    public void setCatShelter(boolean catShelter) {
        this.catShelter = catShelter;
    }
}
