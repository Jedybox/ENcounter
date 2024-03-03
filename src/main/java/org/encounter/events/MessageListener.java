package org.encounter.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
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

        final String[] commands = {
                "n$top", "n$leads", "n$help"
        };

        boolean messageIsCommand = false;
        for (String c : commands) {
            if (event.getMessage().getContentRaw().equals(c) ||
                    event.getMessage().getContentRaw().contains(c)) {

                messageIsCommand = true;
                break;
            }
        }

        String author = event.getMessage().getAuthor().getName();

        if(!messageIsCommand) {
            return;
        } else if (event.getMessage().getContentRaw().contains("n$help")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Commands");
            eb.setDescription("LaIndio Commands");
            eb.setColor(new Color(53, 30, 16));
            eb.addField("n$top", "Show top N-word enjoyer.", false);
            eb.addField("n$leads", "Show top-3 leading N-word enjoyer.", false);
            eb.addField("n$help", "Show's you this nigga.", false);
            event.getChannel().sendMessage("n$help by " + author).setEmbeds(eb.build()).queue();
            return;
        }

        final String[] N_words = {
                "nigga", "nigger", "nig", "wachigger", "negro", "negra",
                "n1gga", "n1gger", "n1g", "wach1gger", "n3gro",
        };

        HashMap<String, Integer> Nigga_board = new HashMap<>();
        Guild g = event.getGuild();
        for (TextChannel textChannel : g.getTextChannels()) {
            MessageHistory messageHistory = MessageHistory.getHistoryFromBeginning(textChannel).complete();
            List<Message> messages = messageHistory.getRetrievedHistory();

            for (Message message: messages) {
                for (String n_word : N_words) {

                    if (message.getAuthor().isBot()) break;

                    Pattern pattern = Pattern.compile(n_word, Pattern.CASE_INSENSITIVE);
                    if (pattern.matcher(message.getContentRaw()).find()) {
                        if (!Nigga_board.containsKey(message.getAuthor().getEffectiveName())) {
                            Nigga_board.put(message.getAuthor().getEffectiveName(), 1);
                        } else {
                            Nigga_board.replace(message.getAuthor().getEffectiveName(),
                                    Nigga_board.get(message.getAuthor().getEffectiveName()) + 1);
                        }
                    }
                }
            }
        }

        if (Nigga_board.isEmpty()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Top Nigga");
            eb.setDescription("I see no N-word enjoyers in this server");
            eb.setColor(new Color(53, 30, 16));
            event.getChannel().sendMessage("n$help by " + author).setEmbeds(eb.build()).queue();
            return;
        }

        switch (event.getMessage().getContentRaw()) {
            case "n$top":
                int topScore = Collections.max(Nigga_board.values());
                for (Map.Entry<String, Integer> nigger : Nigga_board.entrySet()) {

                    if (nigger.getValue() == topScore) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Top Nigga");
                        eb.setDescription("Most happiest n-word enjoyer");
                        eb.setColor(new Color(53, 30, 16));
                        eb.addField(nigger.getKey(),
                                String.format("Score: %s\nKeep being happy you racist Mf",
                                        topScore), false);
                        event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                        break;
                    }

                }

                break;
            case "n$leads":
                int score = Collections.max(Nigga_board.values());
                int i = 0;
                String[] users = new String[3];
                int[] scores = new int[3];

                if (Nigga_board.size() == 1) {
                    i++;
                } else if (Nigga_board.size() == 2) {
                    i += 2;
                } else if (Nigga_board.size() > 2) {
                    i += 3;
                }

                for (int j = i; j < Nigga_board.size() ;j++) {
                    for (Map.Entry<String, Integer> nigga : Nigga_board.entrySet()) {
                        if (score == nigga.getValue()) {
                            users[j] = nigga.getKey();
                            scores[j] = nigga.getValue();
                            Nigga_board.remove(nigga.getKey());
                            break;
                        }
                    }
                }

                if (i == 1) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Top Nigga");
                    eb.setDescription("The only Enjoyer I see. Keep it up");
                    eb.setColor(new Color(53, 30, 16));
                    eb.addField(users[0],
                            String.format("Score: %s", scores[0]) , false);
                    event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                } else if (i == 2) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Top Nigga");
                    eb.setDescription("The only Enjoyer I see. Keep it up");
                    eb.setColor(new Color(53, 30, 16));
                    eb.addField(users[0],
                            String.format("Score: %s", scores[0]) , false);
                    eb.addField(users[1],
                            String.format("Score: %s", scores[1]) , false);
                    event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                } else if (i > 2) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Top Nigga");
                    eb.setDescription("The only Enjoyer I see. Keep it up");
                    eb.setColor(new Color(53, 30, 16));
                    eb.addField(users[1],
                            String.format("Score: %s", scores[1]) , false);
                    eb.addField(users[1],
                            String.format("Score: %s", scores[1]) , false);
                    eb.addField(users[2],
                            String.format("Score: %s", scores[2]) , false);
                    event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                }

                break;
        }
    }
}