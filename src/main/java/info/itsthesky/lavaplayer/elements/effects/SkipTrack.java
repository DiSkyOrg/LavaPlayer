package info.itsthesky.lavaplayer.elements.effects;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class SkipTrack extends AudioWaiterEffect<AudioTrack> {

    static {
        register(
                SkipTrack.class,
                "skip [current] track",
                "[new] track"
        );
    }

    @Override
    public String effectToString() {
        return "skip current track";
    }

    @Override
    public void execute(@NotNull Guild guild, Bot bot) {
        restart(LavaPlayer.getPlayer(bot, guild).nextTrack());
    }
}
