package pro.sky.telegrambot.service;


import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.repository.OwnerRepository;

import java.util.List;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }



    public void saveOwner(Owner owner) {
        ownerRepository.save(owner);
    }

    /**
     * Получаем информацию о владельце по идентификатору. <br>
     * Используется метод репозитория {@link OwnerRepository#getOwnerByChatId(Long chatID)}
     *
     * @param chatId идентификатор владельца
     * @return Owner
     */
    public Owner findOwnerByChatId(Long chatId) {
        return ownerRepository.getOwnerByChatId(chatId);
    }

    /**
     * Получаем информацию о владельцах из БД. <br>
     * Используется метод репозитория {@link OwnerRepository#findAll()}
     *
     * @return List<Owner>
     */
    public List<Owner> findAllOwners() {
        return ownerRepository.findAll();
    }
}
