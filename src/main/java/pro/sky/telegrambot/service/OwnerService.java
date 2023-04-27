package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.enam.ProbationaryStatus;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.exception.WrongInputDataException;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.repository.DogRepository;
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

    public Owner findOwnerById(Integer id) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) {
            throw new NotFoundException();
        }
        return ownerRepository.findOwnerById(id);
    }

    public Owner extendProbationaryPeriod(Integer id, Integer days) {
        Owner owner = findOwnerById(id);
        if (days < 0 || days > 15) {
            throw new WrongInputDataException();
        }
        owner.setPeriodExtend(days);
        owner.setProbationaryStatus(ProbationaryStatus.EXTENDED);
        ownerRepository.save(owner);
        return owner;
    }

    public Owner changeProbationaryStatus(Integer id, ProbationaryStatus status) {
        Owner owner = findOwnerById(id);
        owner.setProbationaryStatus(status);
        ownerRepository.save(owner);
        return owner;
    }
}
