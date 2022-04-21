package info.itsthesky.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import info.itsthesky.disky.api.events.BukkitEvent;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.disky.core.SkriptUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;

public class AudioListener extends AudioEventAdapter {

    private final Bot bot;
    private final Guild guild;

    public AudioListener(final Bot bot, final Guild guild) {
        this.bot = bot;
        this.guild = guild;
    }

    public Bot getBot() {
        return bot;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        call(TrackEventType.END, player);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        call(TrackEventType.RESUME, player);
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        call(TrackEventType.EXCEPTION, player);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        call(TrackEventType.STUCK, player);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        call(TrackEventType.START, player);
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        call(TrackEventType.PAUSE, player);
    }

    private void call(TrackEventType type, AudioPlayer player) {
        final TrackEvent event = new TrackEvent(type, player, bot, guild);
        SkriptUtils.sync(() -> Bukkit.getPluginManager().callEvent(event));
    }

    public enum TrackEventType {
        START,
        END,
        STUCK,
        EXCEPTION,
        PAUSE,
        RESUME,
        SEEK,
        ;
    }

    public class TrackEvent extends BukkitEvent {

        private final TrackEventType type;
        private final AudioTrack track;
        private final AudioPlayer player;
        private final Guild guild;
        private final Bot bot;

        public TrackEvent(final TrackEventType type, AudioPlayer player, Bot bot, Guild guild) {
            super(false);
            this.type = type;
            this.player = player;
            this.track = player.getPlayingTrack();
            this.bot = bot;
            this.guild = guild;
        }

        public TrackEventType getType() {
            return type;
        }

        public AudioPlayer getPlayer() {
            return player;
        }

        public AudioTrack getTrack() {
            return track;
        }

        public Guild getGuild() {
            return guild;
        }

        public Bot getBot() {
            return bot;
        }
    }
}
