package edu.northeastern.cs5500.starterbot.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edu.northeastern.cs5500.starterbot.model.registerList;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    
    private GenericRepository<registerList> registerListRepository;
    
    public void setregisterListRepository(GenericRepository<registerList> registerListRepository){
        this.registerListRepository = registerListRepository;
    }
  
    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        switch (event.getName()) {
            case "register":{
                registerList registerlist = null;
                for (registerList r : registerListRepository.getAll()){
                    registerlist = r;
                }
                String name = event.getOption("content").getAsString();
                
                if (registerlist.getNameList().contains(name)){
                    event.reply("Registration failed " +  name + " has been registered").queue();
                    break;
                } else {
                    List<String> array = registerlist.getNameList();
                    array.add(name);
                    registerlist.setNameList(array);
                    registerListRepository.update(registerlist);
                    event.reply(name + " Registered successfully").queue();
                    break;
                }
            }
            case "time":{   
                Date timestamp = new Date();
                DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
                df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                String temp = df.format(timestamp);
                event.reply(temp).queue();
                break;
            }
            case "say":{
              event.reply(event.getOption("content").getAsString()).queue();
              break;
            }
        }
    }
}
