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

@Name("Track Author")
@Description("Return the author of a specific track")
@Examples("set {_author} to author of last played track.")
public class ExprTrackAuthor extends SimplePropertyExpression<AudioTrack, String> {

    public static void load() {
        register(ExprTrackAuthor.class, String.class,
                "[discord] [audio] track author",
                "audiotrack"
        );
    }

    @Nullable
    @Override
    public String convert(AudioTrack entity) {
        return entity.getInfo().author;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "track author";
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