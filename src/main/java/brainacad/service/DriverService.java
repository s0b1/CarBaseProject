package brainacad.service;

import brainacad.model.Driver;
import brainacad.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DriverService
{

    private final DriverRepository driverRepository;

    public List<Driver> getAllDrivers()
    {
        return driverRepository.findAll();
    }

    public List<Driver> getAvailableDrivers()
    {
        return driverRepository.findByAvailableTrue();
    }

    public Optional<Driver> getDriverById(Long id)
    {
        return driverRepository.findById(id);
    }

    public Driver saveDriver(Driver driver)
    {
        return driverRepository.save(driver);
    }

    public void deleteDriver(Long id)
    {
        driverRepository.deleteById(id);
    }

    public Driver generateRandomDriver()
    {
        String[] firstNames = {"Samuel", "Avery", "William", "Candice", "Lincoln", "Jackson", "Aaron", "Blake", "Miguel", "Julian", "Patrick", "Arnold", "Olivia", "David", "Carson"};
        String[] lastNames = {"Clay", "Koval", "Barbaro", "Martinez", "Vance", "Sansavera", "Anderson", "Walker", "Baker", "Schneider", "Fitzgerald", "Schmidt", "Trapani", "Prestley", "Christmas"};

        String name = firstNames[new Random().nextInt(firstNames.length)] + " " +
                lastNames[new Random().nextInt(lastNames.length)];

        int experience = new Random().nextInt(15) + 1; // 1 to 15 years

        Driver driver = Driver.builder()
                .name(name)
                .experience(experience)
                .available(true)
                .totalEarnings(BigDecimal.ZERO)
                .build();

        return saveDriver(driver);
    }


}
