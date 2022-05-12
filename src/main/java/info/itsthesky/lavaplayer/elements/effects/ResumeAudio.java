package info.itsthesky.lavaplayer.elements.effects;

import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ResumeAudio extends AudioWaiterEffect {

    public static void load() {
        register(
                ResumeAudio.class,
                "resume [the] [audio] [track]"
        );
    }

    @Override
    public String effectToString() {
        return "resume audio track";
    }

    @Override
    public void execute(@NotNull Guild guild, Bot bot) {
        LavaPlayer.getPlayer(bot, guild).setPaused(false);
    }
}
