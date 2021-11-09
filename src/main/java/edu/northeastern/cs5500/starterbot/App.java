package edu.northeastern.cs5500.starterbot;

import static spark.Spark.*;

import edu.northeastern.cs5500.starterbot.listeners.MessageListener;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.MongoDBRepository;
import edu.northeastern.cs5500.starterbot.service.MongoDBService;
import java.util.EnumSet;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class App {

    static String getBotToken() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String token = processBuilder.environment().get("BOT_TOKEN");
        return token;
    }

    public static void main(String[] arg) throws LoginException {
        String token = getBotToken();
        if (token == null) {
            throw new IllegalArgumentException(
                    "The BOT_TOKEN environment variable is not defined.");
        }

        MessageListener messageListener = new MessageListener();
        MongoDBService mongoDBService = new MongoDBService();
        GenericRepository<NEUUser> userRepository =
                new MongoDBRepository<NEUUser>(NEUUser.class, mongoDBService);
        messageListener.setNEUUserRepository(userRepository);
        JDA jda =
                JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class))
                        .addEventListeners(messageListener)
                        .build();
        // check duplicate here

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("say", "Makes the bot say what you told it to say")
                        .addOptions(
                                new OptionData(
                                                OptionType.STRING,
                                                "content",
                                                "What the bot should say")
                                        .setRequired(true)),
                new CommandData("register", "register a student by name,NUID, and role")
                        .addOptions(
                                new OptionData(
                                                OptionType.STRING,
                                                "content",
                                                "format: {firstname} {NUID} {role(Student/TA/Professor)}")
                                        .setRequired(true)),
                new CommandData("time", "Display current time"),
                new CommandData("vaccinated", "Get or set your own vaccination status.")
                        .addOptions(
                                new OptionData(
                                                OptionType.BOOLEAN,
                                                "vaccinated",
                                                "true if you are vaccinated; false if you are not")
                                        .setRequired(false)));
        commands.queue();

        port(8080);

        get(
                "/",
                (request, response) -> {
                    return "{\"status\": \"OK\"}";
                });
    }
}
