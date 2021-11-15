package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import lombok.Data;

@Data
public abstract class ScheduleBotCommandsWithRepositoryAbstract
        implements ScheduleBotCommandsInterface {
    protected GenericRepository<OfficeHour> officeHourRepository;
    protected OfficeHour officeHour;
    protected NEUUser neuuser;
    protected GenericRepository<NEUUser> userRepository;
}
