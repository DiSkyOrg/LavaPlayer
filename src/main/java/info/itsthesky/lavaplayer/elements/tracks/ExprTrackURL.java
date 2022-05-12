package info.itsthesky.lavaplayer.elements.tracks;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Track URL")
@Description("Return the YouTube URL of a track")
@Examples("set {_url} to url of last played track.")
public class ExprTrackURL extends SimplePropertyExpression<AudioTrack, String> {

    public static void load() {
        register(ExprTrackURL.class, String.class,
                "[discord] [audio] track (url|uri)",
                "audiotrack"
        );
    }

    @Nullable
    @Override
    public String convert(AudioTrack entity) {
        return entity.getInfo().uri;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "url";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return CollectionUtils.array();
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {
        
    }
}