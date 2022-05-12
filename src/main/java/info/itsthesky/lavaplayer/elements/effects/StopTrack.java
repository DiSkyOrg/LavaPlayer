package info.itsthesky.lavaplayer.elements.effects;

import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class StopTrack extends AudioWaiterEffect{

    public static void load() {
        register(
                StopTrack.class,
                "stop (track|queue)"
        );
    }

    @Override
    public String effectToString() {
        return "stop queue";
    }

    @Override
    public void execute(@NotNull Guild guild, Bot bot) {
        LavaPlayer.getPlayer(bot, guild).clearQueue();
        LavaPlayer.getPlayer(bot, guild).stopTrack();
        restart();
    }
}
