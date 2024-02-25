package org.encounter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.encounter.events.*;

public class Main {

    public static void main(String[] args) {
        final String token = "MTIwNzcwMzUzOTA5MTI0NzE1NQ.GuLyua.dhPdOA0DBvLJw3glC_uwTuC4EB8m77WXiBz2z8";
        JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("you niggas"));
        JDA jda = jdaBuilder
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_MEMBERS
                )
                .addEventListeners(
                        new ReadyListener(),
                        new MessageListener())
                .build();
    }

}