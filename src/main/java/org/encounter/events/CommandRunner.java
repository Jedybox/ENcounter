package org.encounter.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
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

        final String[] N_words = {
                "Nigga", "Nigger", "Negro", "Negra", "Monkey", "N1gga", "Africanus", "Negrito",
                "Negrita", "Chigger", "Wigger", "Wachigger", "Nigawatt", "Niggawatt", "Nega",
                "Ape", "Bamboula", "Banaan", "Coon", "Nig@", "Nigg@", "Blacky", "Blackie",
                "Hakuna Matata", "N1gg4", "Noggers", "Nuggers", "Nibbas", "Neggers", "Coon",
                "Invisible at Dark", "Invisible at Night", "Ch1gger", "Nigha", "Negha",
                "Chocolate skin", "Choco skin", "Niggas", "Monkeys", "Slaves", "Slave",
                "Neggawatt", "Negawatt", "niga", "Gorilla", "Go back to africa", "africaman",
                "N1gger", "N1gg3r", "C00n", "Z1gger", "Zigger", "Z1gg3r", "N1gg@", "nigs", "nig"
        };


        switch (event.getName()) {
            case "top-nigga":

                HashMap<User, Integer> nigger_board = new HashMap<>();
                HashMap<User, List<String>> scores = new HashMap<>();
                Guild guild = event.getGuild();
                List<TextChannel> textChannels = Objects.requireNonNull(guild).getTextChannels();
                for (TextChannel textChannel: textChannels){
                    MessageHistory messageHistory = MessageHistory.getHistoryFromBeginning(textChannel).complete();
                    List<Message> messages = messageHistory.getRetrievedHistory();

                    for (Message message: messages) {
                        if (message.getAuthor().isBot()) continue;
                        for (String n_word: N_words) {
                            Pattern pattern = Pattern.compile(Pattern.quote(n_word), Pattern.CASE_INSENSITIVE);
                            if (pattern.matcher(message.getContentRaw()).find()) {
                                if (!nigger_board.containsKey(message.getAuthor())) {
                                    nigger_board.put(message.getAuthor(), 1);
                                } else {
                                    int previous_num = nigger_board.get(message.getAuthor());
                                    nigger_board.replace(message.getAuthor(), previous_num + 1);
                                }

                                if (!scores.containsKey(message.getAuthor())) {
                                    List<String> ass = new ArrayList<>();
                                    ass.add(n_word);
                                    scores.put(message.getAuthor(), ass);
                                } else {
                                    List<String> ass = scores.get(message.getAuthor());
                                    ass.add(n_word);
                                    scores.replace(message.getAuthor(), ass);
                                }

                            }
                        }
                    }
                }


                if (nigger_board.isEmpty()) {
                    event.reply("I see no Top Niggers in here").queue();
                } else {
                    int top_nigga = Collections.max(nigger_board.values());
                    String top = ":top_arrow:";
                    String n = ":regional_indicator_n:";
                    String i = ":regional_indicator_i:";
                    String g = ":regional_indicator_g:";
                    String e = ":regional_indicator_e:";
                    String r = ":regional_indicator_r:";
                    String _100 = ":100:";
                    for (Map.Entry<User, Integer> nigger: nigger_board.entrySet()) {
                        if (nigger.getValue() == top_nigga) {

                            String content = "<@"+nigger.getKey().getId() + ">" + top + n + i + g + g + e + r + _100 + nigger.getValue();
                            event.reply("").setEphemeral(false).queue();
                            event.getChannel().sendMessage(content + scores.get(nigger.getKey()).toString()).queue();
                            break;
                        }
                    }
                }
                break;
            case "leading-niggas":
                event.reply("There are no niggers yet").queue();
                break;
            default:
                break;
        }

    }
}
