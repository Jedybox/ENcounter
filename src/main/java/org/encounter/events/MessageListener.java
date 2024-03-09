package org.encounter.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getMessage().getAuthor().isBot()) return;

        final String[] commands = {
                "n|top", "n|leads", "n|help", "n|all"
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
            eb.addField("n|top", "Show top N-word enjoyer.", false);
            eb.addField("n|leads", "Show top-3 leading N-word enjoyer.", false);
            eb.addField("n|help", "Show's you this nigga.", false);
            eb.addField("n|all","Show all members who have said the word", false);
            event.getChannel().sendMessage("n$help by " + author).setEmbeds(eb.build()).queue();
            return;
        } else{
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Please wait");
            eb.setDescription("This command may take a long time\n" +
                    "If the bot didn't respond it could be knocked-off by the amount of messages in this server");
            eb.setColor(new Color(53, 30, 16));
            event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
        }

        final String[] N_words = {
                "nigga", "nigger", "nig", "wachigger", "negro", "negra",
                "n1gga", "n1gger", "n1g", "wach1gger", "n3gro", "n3gra",
                "chigger", "ch1gger", "chigg3r", "ch1gg3r", "monkey", "m0nkey",
                "monk3y", "m0nk3y"
        };

        HashMap<User, Integer> Nigga_board = new HashMap<>();
        List<Message> messages = new ArrayList<>();
        for (TextChannel textChannel : event.getGuild().getTextChannels()) {
            List<Message> history = textChannel.getHistory().retrievePast(100).complete();
            messages.addAll(history);
            long lastMessageId = history.getLast().getIdLong();
            List<Message> newMessages = textChannel.getHistoryBefore(lastMessageId, 100)
                    .complete().getRetrievedHistory();
            do {
                messages.addAll(newMessages);
                history = newMessages;
                try {
                    lastMessageId = history.getLast().getIdLong();
                    newMessages = textChannel.getHistoryBefore(lastMessageId, 100).complete().getRetrievedHistory();
                } catch (Exception e) {
                    break;
                }
            } while (!newMessages.isEmpty());

            for (Message message : messages) {
                for (String w : N_words) {
                    Pattern pattern = Pattern.compile(w, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(message.getContentRaw());
                    User user = message.getAuthor();
                    if (matcher.find()) {
                        if (!Nigga_board.containsKey(user)) {
                            Nigga_board.put(user, 1);
                        } else {
                            Nigga_board.replace(user, Nigga_board.get(user) + 1);
                        }
                    }
                }
            }

            messages.clear();
        }

        if (Nigga_board.isEmpty()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Top Nigga");
            eb.setDescription("I see no N-word enjoyers in this server");
            eb.setColor(new Color(53, 30, 16));
            event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(53, 30, 16));
        switch (event.getMessage().getContentRaw()) {
            case "n|top":
                int topScore = Collections.max(Nigga_board.values());
                for (Map.Entry<User, Integer> nigger : Nigga_board.entrySet()) {

                    if (nigger.getValue() == topScore) {
                        eb.setTitle("Top Nigga");
                        eb.setDescription("Most happiest n-word enjoyer");
                        eb.addField(nigger.getKey().getEffectiveName(),
                                String.format("Score: %s\nKeep being happy you racist Mf",
                                        topScore), false);
                        event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                        break;
                    }
                }

                break;
            case "n|leads":
                eb.setTitle("Leading Nigga users");

                for (int i = 0; i < 3; i++) {
                    int score = Collections.max(Nigga_board.values());

                    if (Nigga_board.isEmpty()) break;

                    for (Map.Entry<User, Integer> n_wad : Nigga_board.entrySet()) {
                        if (score == n_wad.getValue()) {
                            eb.addField(
                                    n_wad.getKey().getEffectiveName(),
                                    String.format("Score: %s",n_wad.getValue()),
                                    false);
                            Nigga_board.remove(n_wad.getKey());
                            break;
                        }
                    }
                }

                event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();

                break;
            case "n|all":
                eb.setTitle("All Members");
                eb.setDescription("Score of all members");
                for (Map.Entry<User, Integer> n_wad : Nigga_board.entrySet()) {
                    eb.addField(
                            n_wad.getKey().getEffectiveName(),
                            String.format("Scores: %s", n_wad.getValue()),
                            false);
                }
                event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                System.out.println("done");
                break;
        }
    }
}