package org.encounter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.encounter.events.*;

public class Main {

    public static void main(String[] args) {
        final String token = "MTIxMjM1NzI0MDI0MjMwNzE1Mg.G1w1Dx.-AqkGBjwx4MlALZHTzoyStOVqtsbo1oMlaF_LM";
        JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("your balls"));
        JDA jda = jdaBuilder
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_MEMBERS
                )
                .addEventListeners(
                        new ReadyListener(),
                        new MessageListener()
                ).build();
    }

}