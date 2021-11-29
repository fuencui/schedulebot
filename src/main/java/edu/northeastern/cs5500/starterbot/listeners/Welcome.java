package edu.northeastern.cs5500.starterbot.listeners;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Welcome extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        embed.setTitle("Office Hour Schedule Bot");
        embed.setDescription("Let's make office hour more efficient");
        embed.addField(
                "Step 1",
                "/register: provide your first name, NUID, and role in order (only for first time user)",
                false);
        embed.addField(
                "Step 2",
                "/vaccinated: if schedule for a in-person appointment, please provide your vaccinated status",
                false);
        embed.addField(
                "Step 3", "/reserve: make your reservation! pick a time fits for you", false);
        embed.addField("Additional", "/listmyofficehour: check your scheduled appointments", true);
        embed.setAuthor("Fuen,Yuchi,Hao,Ziling,Fanxing");
        event.getUser()
                .openPrivateChannel()
                .queue(channel -> channel.sendMessage(embed.build()).queue());
        ;
    }
}
