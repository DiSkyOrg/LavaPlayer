package info.itsthesky.lavaplayer.elements.effects;

import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class PauseAudio extends AudioWaiterEffect {

    public static void load() {
        register(
                PauseAudio.class,
                "pause [the] [audio] [track]"
        );
    }

    @Override
    public String effectToString() {
        return "pause audio track";
    }

    @Override
    public void execute(@NotNull Guild guild, Bot bot) {
        LavaPlayer.getPlayer(bot, guild).setPaused(true);
    }
}
