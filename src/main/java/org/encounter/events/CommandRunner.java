package org.encounter.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandRunner extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        switch (event.getName()) {
            case "top-nigga":
                event.reply("No one is a nigger yet").setEphemeral(true).queue();
                break;
            case "leading-niggas":
                event.reply("There are no niggers yet").setEphemeral(true).queue();
                break;
        }

    }
}
