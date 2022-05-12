package info.itsthesky.lavaplayer.elements.tracks;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.api.skript.EasyElement;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Track Position")
@Description({"Return the position of a specific track",
        "This property can eb changed to move the current position of the track.",
        "It will only accept timespan (e.g. '1 second', '25 minutes', etc...)!"})
@Examples({"set {_position} to track position of track event-bot is playing in event-guild",
        "add 10 second to track position of track event-bot is playing in event-guild"})
public class ExprTrackPosition extends SimplePropertyExpression<AudioTrack, Timespan> {

    public static void load() {
        register(ExprTrackPosition.class, Timespan.class,
                "[discord] [audio] track position",
                "audiotrack"
        );
    }

    @Nullable
    @Override
    public Timespan convert(AudioTrack entity) {
        return new Timespan(entity.getPosition());
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "position";
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        final AudioTrack track = getExpr().getSingle(e);
        if (track == null || delta == null || delta.length == 0 || delta[0] == null)
            return;
        final Timespan time = (Timespan) delta[0];

        switch (mode) {
            case SET:
                track.setPosition(time.getMilliSeconds());
                return;
            case ADD:
                track.setPosition(track.getPosition() + time.getMilliSeconds());
                return;
            case REMOVE:
                track.setPosition(track.getPosition() - time.getMilliSeconds());
                return;
        }
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (EasyElement.isChangerMode(mode))
            return new Class[] {Timespan.class};
        return new Class[0];
    }
}