package org.encounter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.encounter.events.*;

public class Main {

    public static void main(String[] args) {
        final String token = "MTIwNzcwMzUzOTA5MTI0NzE1NQ.GuLyua.dhPdOA0DBvLJw3glC_uwTuC4EB8m77WXiBz2z8";
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        JDA jda = jdaBuilder.
                enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(
                        new ReadyListener(),
                        new MessageListener(),
                        new CommandRunner())
                .build();

        jda.upsertCommand("top-nigga","Top nigger").setGuildOnly(true).queue();
        jda.upsertCommand("leading-niggas", "Leading niggas").setGuildOnly(true).queue();
    }

}