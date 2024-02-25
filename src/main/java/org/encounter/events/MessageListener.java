package org.encounter.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getMessage().getAuthor().isBot()) return;

        String[] N_words = {
                "Nigga", "Nigger", "Negro", "Negra", "Chigger",
                "Whigger", "Wigger", "Monkey", "N1gga", "N1gger",
                "Nigg@", "N1gg@", "Monkey"
        };

        for (String n_word: N_words) {
            Pattern pattern = Pattern.compile(n_word, Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(event.getMessage().getContentDisplay()).find())
                event.getChannel().sendMessage("Nigger detected").queue();
        }

        HashMap<User, Integer> nigger_board = new HashMap<>();
        Guild guild = event.getGuild();
        List<TextChannel> textChannels = guild.getTextChannels();
        for (TextChannel textChannel: textChannels) {
            MessageHistory history = MessageHistory.getHistoryFromBeginning(textChannel).complete();
            List<Message> messages = history.getRetrievedHistory();

            for (Message message: messages) {
                for (String n_word: N_words) {

                    if (message.getAuthor().isBot()) break;

                    Pattern pattern = Pattern.compile(n_word, Pattern.CASE_INSENSITIVE);
                    if (!pattern.matcher(message.getContentRaw()).find()) {
                        continue;
                    } else {
                        if (!nigger_board.containsKey(message.getAuthor())) {
                            nigger_board.put(message.getAuthor(), 1);
                        } else {
                            nigger_board.replace(message.getAuthor(), nigger_board.get(message.getAuthor()) +1);
                        }
                    }

                }
            }

        }

        if (event.getMessage().getContentRaw().length() >= 4) {
            if (!event.getMessage().getContentRaw().contains("nig!")) return;

            String command = event.getMessage().getContentRaw();

            switch (command){
                case "nig!top":
                    if (nigger_board.isEmpty())
                        event.getChannel().sendMessage("I see no niggers").queue();

                    int top_score = Collections.max(nigger_board.values());
                    for (Map.Entry<User, Integer> nigger: nigger_board.entrySet()) {
                        if (top_score == nigger.getValue()) {
                            String top_N = "<@" + nigger.getKey().getId() +">";
                            event.getChannel().sendMessage(
                                    top_N + ":regional_indicator_n:" + ":regional_indicator_i:"
                                    + ":regional_indicator_g:" + ":regional_indicator_g:" + ":regional_indicator_e:"
                                    + ":regional_indicator_r:" + ":100:" + "\nSCORE: " + nigger.getValue()
                            ).queue();
                            break;
                        }
                    }
                    break;
                case "nig!leads":
                    break;
                default:
                    event.getChannel().sendMessage("Nigga, that ain't a command of mine").queue();
                    break;
            }
        }


    }
}
