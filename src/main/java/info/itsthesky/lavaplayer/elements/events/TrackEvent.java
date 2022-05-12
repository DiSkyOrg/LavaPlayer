package info.itsthesky.lavaplayer.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.disky.core.SkriptUtils;
import info.itsthesky.lavaplayer.AudioListener;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackEvent extends SkriptEvent {

    public static void load() {
        Skript.registerEvent(
                "Track Event", TrackEvent.class, AudioListener.TrackEvent.class,
                "track [event] %trackeventtype%"
        ).description("Fired when a track receive a specific event. Use the literal to define the event's type such as:",
                "  - START",
                "  - END",
                "  - STUCK",
                "  - PAUSE",
                "  - RESUME",
                "  - SEEK"
        ).examples("on track play:", "on track end:", "on track exception:");

        LavaPlayer.getInstance().registerValue(AudioListener.TrackEvent.class, Bot.class, AudioListener.TrackEvent::getBot);
        LavaPlayer.getInstance().registerValue(AudioListener.TrackEvent.class, Guild.class, AudioListener.TrackEvent::getGuild);
        LavaPlayer.getInstance().registerValue(AudioListener.TrackEvent.class, AudioTrack.class, AudioListener.TrackEvent::getTrack);
        LavaPlayer.getInstance().registerValue(AudioListener.TrackEvent.class, AudioListener.TrackEventType.class, AudioListener.TrackEvent::getType);

    }

    private AudioListener.TrackEventType type;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, SkriptParser.@NotNull ParseResult parseResult) {
        type = ((Literal<AudioListener.TrackEventType>) args[0]).getSingle();
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return e instanceof AudioListener.TrackEvent &&  type == ((AudioListener.TrackEvent) e).getType();
    }

    @Override
    public boolean isEventPrioritySupported() {
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "track " + type.name();
    }
}
