package org.encounter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.encounter.events.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        final String token = "MTIxMjM1NzI0MDI0MjMwNzE1Mg.G1w1Dx.-AqkGBjwx4MlALZHTzoyStOVqtsbo1oMlaF_LM";
        JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("n|help"));
        JDA jda = jdaBuilder
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_MEMBERS
                )
                .addEventListeners(
                        new ReadyListener(),
                        new MessageListener(),
                        new JoinedAGuildListener()
                ).build();

        ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
        exe.scheduleAtFixedRate(() -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(dtf);

            if (time.equals("12:00")) {
                for (Guild g : jda.getGuilds()) {
                    g.getTextChannels().getFirst().sendMessage("It's 12am niggas").queue();
                }
            }
        },0,45, TimeUnit.SECONDS);
    }

}