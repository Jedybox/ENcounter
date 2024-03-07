package org.encounter.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
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
                "n!top", "n!leads", "n!help"
        };

        boolean messageIsCommand = false;
        for (String c : commands) {
            if (event.getMessage().getContentRaw().equals(c) ||
                    event.getMessage().getContentRaw().contains(c)) {

                messageIsCommand = true;
                break;
            }
        }

        boolean commandIsNameCommand = false;
        List<Member> members = event.getGuild().getMembers();
        for (Member member : members) {
            String name = member.getUser().getGlobalName();
            if (event.getMessage().getContentRaw().equals("n!"+name) ||
                    event.getMessage().getContentRaw().contains("n!"+name)) {
                messageIsCommand = true;
                commandIsNameCommand = true;
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
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Please wait");
            eb.setDescription("This command may take a long time");
            eb.setColor(new Color(53, 30, 16));
            event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
        }

        final String[] N_words = {
                "nigga", "nigger", "nig", "wachigger", "negro", "negra",
                "n1gga", "n1gger", "n1g", "wach1gger", "n3gro", "n3gra",
                "chigger", "ch1gger", "chigg3r", "ch1gg3r", "monkey", "m0nkey",
                "monk3y", "m0nk3y"
        };

        HashMap<String, Integer> Nigga_board = new HashMap<>();
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
        }

        if (messages.isEmpty()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Top Nigga");
            eb.setDescription("This place looks shit, No one have yet sent a message");
            eb.setColor(new Color(53, 30, 16));
            event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
            return;
        }

        for (Message message : messages) {
            for (String w : N_words) {
                Pattern pattern = Pattern.compile(w, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(message.getContentRaw());
                String name = message.getAuthor().getGlobalName();
                if (matcher.find()) {
                    if (!Nigga_board.containsKey(name)) {
                        Nigga_board.put(name, 1);
                    } else {
                        Nigga_board.replace(name, Nigga_board.get(name) + 1);
                    }
                }
            }
        }

        if (Nigga_board.isEmpty()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Top Nigga");
            eb.setDescription("I see no N-word enjoyers in this server");
            eb.setColor(new Color(53, 30, 16));
            event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
            return;
        }

        if (commandIsNameCommand) {
            for (Member member : event.getGuild().getMembers()){
                String name = member.getUser().getGlobalName();
                if (event.getMessage().getContentRaw().equals("n!"+name) ||
                        event.getMessage().getContentRaw().contains("n!"+name)) {
                    EmbedBuilder eb = new EmbedBuilder();
                    if (!Nigga_board.containsKey(name)){
                        eb.setTitle(name+" N-word count stat");
                        eb.setDescription("Bro haven't message N-words, yet.");
                        event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                    } else  {
                        for (Map.Entry<String, Integer> nig : Nigga_board.entrySet()) {
                            if (nig.getKey().equals(name)) {
                                eb.setTitle(name+" N-word count stat");
                                eb.setDescription("N-word score: " + nig.getValue());
                                event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                                return;
                            }
                        }
                    }
                }
            }
        }

        switch (event.getMessage().getContentRaw()) {
            case "n!top":
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
            case "n!leads":
                int score = Collections.max(Nigga_board.values());
                String[] users = new String[3];
                int[] scores = new int[3];

                if (Nigga_board.size() > 3){
                    for (int j = 0; j < Nigga_board.size(); j++) {
                        for (Map.Entry<String, Integer> nigga : Nigga_board.entrySet()) {
                            if (score == nigga.getValue()) {
                                users[j] = nigga.getKey();
                                scores[j] = nigga.getValue();
                                Nigga_board.remove(nigga.getKey());
                                break;
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < 3;j++) {
                        for (Map.Entry<String, Integer> nigga : Nigga_board.entrySet()) {
                            if (score == nigga.getValue()) {
                                users[j] = nigga.getKey();
                                scores[j] = nigga.getValue();
                                Nigga_board.remove(nigga.getKey());
                                break;
                            }
                        }
                    }
                }

                EmbedBuilder eb = new EmbedBuilder();
                if (Nigga_board.size() == 1) {
                    eb.setTitle("Top Nigga");
                    eb.setDescription("The only Enjoyer I see. Keep it up");
                    eb.setColor(new Color(53, 30, 16));
                    eb.addField(users[0],
                            String.format("Score: %s", scores[0]) , false);
                    event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                } else if (Nigga_board.size() == 2) {
                    eb.setTitle("Top Niggas");
                    eb.setDescription("The only Enjoyer I see. Keep it up");
                    eb.setColor(new Color(53, 30, 16));
                    eb.addField(users[0],
                            String.format("Score: %s", scores[0]) , false);
                    eb.addField(users[1],
                            String.format("Score: %s", scores[1]) , false);
                    event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                } else if (Nigga_board.size() > 2) {
                    eb.setTitle("Top Niggas");
                    eb.setDescription("The only Enjoyer I see. Keep it up");
                    eb.setColor(new Color(53, 30, 16));
                    eb.addField(users[0],
                            String.format("Score: %s", scores[0]) , false);
                    eb.addField(users[1],
                            String.format("Score: %s", scores[1]) , false);
                    eb.addField(users[2],
                            String.format("Score: %s", scores[2]) , false);
                    event.getChannel().sendMessage("").setEmbeds(eb.build()).queue();
                }
                break;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}