package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.enam.ProbationaryStatus;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.exception.WrongInputDataException;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.repository.DogOwnerRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class DogOwnerService {
    private final DogOwnerRepository ownerRepository;

    public DogOwnerService(DogOwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public void saveOwner(DogOwner owner) {
        ownerRepository.save(owner);
    }

    public Optional<DogOwner> findDogOwnerByChatId(Long chatId) {
        return Optional.ofNullable(ownerRepository.getOwnerByChatId(chatId));
    }

    public List<DogOwner> findAllOwners() {
        return ownerRepository.findAll();
    }

    public DogOwner saveOwnerByNameAndChatId(String name,
                                             long chatId) {
        DogOwner owner = new DogOwner();
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

    public DogOwner findOwnerById(Integer id) {
        DogOwner owner = ownerRepository.findOwnerById(id);
        if (owner == null) {
            throw new NotFoundException();
        }
        return ownerRepository.findOwnerById(id);
    }

    public DogOwner extendProbationaryPeriod(Integer id, Integer days) {
        DogOwner owner = findOwnerById(id);
        if (days < 0 || days > 15) {
            throw new WrongInputDataException();
        }
        owner.setPeriodExtend(days);
        owner.setDateOfEndProbation(owner.getDateOfEndProbation().plusDays(days));
        owner.setProbationaryStatus(ProbationaryStatus.EXTENDED);
        ownerRepository.save(owner);
        return owner;
    }

    public DogOwner changeProbationaryStatus(Integer id, ProbationaryStatus status) {
        DogOwner owner = findOwnerById(id);
        owner.setProbationaryStatus(status);
        ownerRepository.save(owner);
        return owner;
    }
}
