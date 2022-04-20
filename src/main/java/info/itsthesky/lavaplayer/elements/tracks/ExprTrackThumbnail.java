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

@Name("Track Thumbnail")
@Description("Return the thumbnail URL of a specific track")
@Examples("set thumbnail of embed to thumbnail of last played track.")
public class ExprTrackThumbnail extends SimplePropertyExpression<AudioTrack, String> {

    static {
        register(ExprTrackThumbnail.class, String.class,
                "[discord] [audio] track thumbnail",
                "audiotrack"
        );
    }

    @Nullable
    @Override
    public String convert(AudioTrack entity) {
        return "https://img.youtube.com/vi/"+entity.getIdentifier()+"/maxresdefault.jpg";
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "track thumbnail";
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