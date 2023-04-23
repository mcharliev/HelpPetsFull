package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.enam.ProbationaryStatus;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.repository.OwnerRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    public Owner findOwnerByChatId(Long chatId) {
        return ownerRepository.getOwnerByChatId(chatId);
    }

    public List<Owner> findAllOwners() {
        return ownerRepository.findAll();
    }

    public Owner saveOwnerByNameAndChatId(String name,
                                          long chatId) {
        Owner owner = new Owner();
        owner.setName(name);
        owner.setChatId(chatId);
        owner.setDateOfStartProbation(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        owner.setDateOfEndProbation(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusDays(30));
        owner.setProbationaryStatus(ProbationaryStatus.ACTIVE);

        ownerRepository.findAll()
                .forEach(owners -> {
                    if (owners.getName().equals(owner.getName()) &&
                            owners.getChatId().equals(owner.getChatId())) {
                        throw new AlreadyExistException();
                    }
                });
        return ownerRepository.save(owner);
    }
}
