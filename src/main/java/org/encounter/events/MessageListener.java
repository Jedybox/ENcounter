package org.encounter.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getMessage().getAuthor().isBot()) return;

        if (event.getMessage().getContentRaw().length() >= 3) {
            if (!event.getMessage().getContentRaw().contains("nig!")) return;

            final String command = event.getMessage().getContentRaw();
            final String[] commands = {"nig!top", "nig!leads"};
            boolean notACommand = true;
            for (String ACommand: commands) {
                if (command.equals(ACommand)) {
                    notACommand = false;
                    break;
                }
            }
            if (notACommand) {
                event.getChannel().sendMessage("Nigga, that ain't a command of mine").queue();
                return;
            }

            final String[] N_words = {
                    "Nigga", "Nigger", "Negro", "Negra", "Chigger",
                    "Whigger", "Wigger", "Monkey", "N1gga", "N1gger",
                    "Nigg@", "N1gg@", "Nig", "Neg", "N1g", "N!gga", "N1gg@",
                    "Ape"
            };

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
                        if (pattern.matcher(message.getContentRaw()).find()) {
                            if (!nigger_board.containsKey(message.getAuthor())) {
                                nigger_board.put(message.getAuthor(), 1);
                            } else {
                                nigger_board.replace(message.getAuthor(), nigger_board.get(message.getAuthor()) +1);
                            }
                        }
                    }
                }

            }

            switch (command){
                case "nig!top":
                    if (nigger_board.isEmpty()) {
                        event.getChannel().sendMessage("I see no niggers").queue();
                        return;
                    }

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
                    if (nigger_board.size() >= 3) {
                        int topScore = Collections.max(nigger_board.values());
                        User[] niggas = new User[3];
                        int[] scores = new int[3];
                        int checker = 0;

                        while (checker < 3) {
                            for (Map.Entry<User, Integer> nigger: nigger_board.entrySet()) {
                                if (topScore == nigger.getValue()) {
                                    nigger_board.remove(nigger.getKey());
                                    topScore = Collections.max(nigger_board.values());

                                    niggas[checker] = nigger.getKey();
                                    scores[checker] = nigger.getValue();
                                    checker++;
                                    break;
                                }
                            }
                        }

                        event.getChannel()
                                .sendMessage(
                                        "The only 2 " + ":regional_indicator_n:" + ":regional_indicator_i:" +
                                                ":regional_indicator_g:" + ":regional_indicator_g:" +
                                                ":regional_indicator_e:" + ":regional_indicator_r:" +
                                                ":regional_indicator_s:\n" +
                                                "<@" + niggas[0].getId() + ">    Score: " + scores[0] + "\n" +
                                                "<@" + niggas[1].getId() + ">    Score: " + scores[1] + "\n" +
                                                "<@" + niggas[2].getId() + ">    Score: " + scores[2]
                                ).queue();
                    } else if (nigger_board.size() == 2) {
                        int topScore = Collections.max(nigger_board.values());
                        User[] niggas = new User[2];
                        int[] scores = new int[2];
                        int checker = 0;

                        while (checker < 2) {
                            for (Map.Entry<User, Integer> nigga: nigger_board.entrySet()) {
                                if (topScore == nigga.getValue()) {
                                    nigger_board.remove(nigga.getKey());
                                    topScore = Collections.max(nigger_board.values());

                                    niggas[checker] = nigga.getKey();
                                    scores[checker] = nigga.getValue();
                                    checker++;
                                    break;
                                }
                            }
                        }

                        event.getChannel()
                                .sendMessage(
                                        "The only 2 " + ":regional_indicator_n:" + ":regional_indicator_i:" +
                                                ":regional_indicator_g:" + ":regional_indicator_g:" +
                                                ":regional_indicator_e:" + ":regional_indicator_r:" +
                                                ":regional_indicator_s:\n" +
                                                "<@" + niggas[0].getId() + ">    Score: " + scores[0] + "\n" +
                                                "<@" + niggas[1].getId() + ">    Score: " + scores[1]
                                ).queue();
                    } else if (nigger_board.size() == 1) {
                        for (Map.Entry<User, Integer> nigger: nigger_board.entrySet()) {
                            String top_N = "<@" + nigger.getKey().getId() +">";
                            event.getChannel().sendMessage(
                                    top_N + "Only leading nigger in the server" + ", SCORE: " + nigger.getValue()
                            ).queue();
                        }
                    } else {
                        event.getChannel().sendMessage(
                                        "I see no"
                                                + ":regional_indicator_n:" + ":regional_indicator_i:" + ":regional_indicator_g:"
                                                + ":regional_indicator_g:" + ":regional_indicator_e:" + ":regional_indicator_r:"
                                                + ":regional_indicator_s:")
                                .queue();
                    }
                    break;
            }
        }
    }
}
