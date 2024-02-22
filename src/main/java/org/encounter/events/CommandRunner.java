package org.encounter.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandRunner extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        super.onSlashCommandInteraction(event);

        final String[] Nwords = {
                "Nigga", "Nigger", "Negro", "Negra", "Monkey", "N1gga", "Africanus", "Negrito",
                "Negrita", "Chigger", "Wigger", "Wachigger", "Nigawatt", "Niggawatt", "Nega",
                "Ape", "Bamboula", "Banaan", "Coon", "Nig@", "Nigg@", "Blacky", "Blackie",
                "Hakuna Matata", "N1gg4", "Noggers", "Nuggers", "Nibbas", "Neggers", "Coon",
                "Invisible at Dark", "Invisible at Night", "Ch1gger", "Nigha", "Negha",
                "Chocolate skin", "Choco skin", "Niggas", "Monkeys", "Slaves", "Slave",
                "Neggawatt", "Negawatt", "niga", "Gorilla", "Go back to africa", "africaman",
                "N1gger", "N1gg3r", "C00n", "Z1gger", "Zigger", "Z1gg3r", "N1gg@", "nigs", "nig"
        };

        JDA jda = event.getJDA();
        HashMap<User, Integer> nigger_board = new HashMap<>();

        for (Guild guild: jda.getGuilds()) {
            for (TextChannel textChannel: guild.getTextChannels()){

                try {
                    List<Message> messages = textChannel.getHistory().retrievePast(150).complete();
                    for (Message message : messages) {
                        if (message.getAuthor().isBot()) break;
                        for (String n_word : Nwords) {

                            Pattern pattern = Pattern.compile(Pattern.quote(message.toString()), Pattern.CASE_INSENSITIVE);

                            if (pattern.matcher(n_word).find()) {
                                nigger_board.putIfAbsent(message.getAuthor(), 1);

                                if (nigger_board.containsKey(message.getAuthor())) {
                                    nigger_board.replace(message.getAuthor(), nigger_board.get(message.getAuthor()) + 1);
                                } else {
                                    nigger_board.putIfAbsent(message.getAuthor(), 1);
                                }
                            }
                        }
                    }
                }  catch (Exception e) {
                    break;
                }
            }
        }

        switch (event.getName()) {
            case "top-nigga":
                if (nigger_board.isEmpty()) {
                    event.reply("I see no Top Niggers in here").setEphemeral(true).queue();
                } else {
                    int top_nigga = Collections.max(nigger_board.values());

                    for (Map.Entry<User, Integer> nigger: nigger_board.entrySet()) {
                        if (nigger.getValue() == top_nigga) {
                            event.reply(nigger.getKey().getName() + ", the top niggeracist").setEphemeral(true).queue();
                            break;
                        }
                    }
                }
                break;
            case "leading-niggas":
                event.reply("There are no niggers yet").setEphemeral(true).queue();
                break;
        }

    }
}
