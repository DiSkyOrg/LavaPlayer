package info.itsthesky.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.api.events.BukkitEvent;
import info.itsthesky.disky.core.Bot;
import net.dv8tion.jda.api.entities.Guild;

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
    public void onPlayerPause(AudioPlayer player) {
        super.onPlayerPause(player);
    }

    public enum TrackEventType {
        TRACK_START,
        TRACK_END,
        TRACK_STUCK,
        TRACK_PAUSE,
        TRACK_RESUME,
        TRACK_SEEK,
        ;
    }

    public class TrackEvent extends BukkitEvent {

        private final TrackEventType type;
        private final AudioTrack track;
        private final AudioPlayer player;

        public TrackEvent(final TrackEventType type, AudioPlayer player) {
            super(false);
            this.type = type;
            this.player = player;
            this.track = player.getPlayingTrack();
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
    }
}
