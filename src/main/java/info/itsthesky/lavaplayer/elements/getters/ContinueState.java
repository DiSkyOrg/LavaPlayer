package info.itsthesky.lavaplayer.elements.getters;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.api.changers.ChangeableSimplePropertyExpression;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Continue State of Guild")
@Description({"Get ot set the continue state of a guild.",
        "If it's true, and there's a next track available once the current finish, it will play it.."})
public class ContinueState extends ChangeableSimplePropertyExpression<Guild, Boolean> {

    public static void load() {
        register(ContinueState.class, Boolean.class,
                "[discord] continue [state]",
                "guild"
        );
    }

    @Nullable
    @Override
    public Boolean convert(@NotNull Guild guild) {
        return LavaPlayer.getPlayer(guild).shouldPlayNext();
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "repeating state";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(@NotNull Event e, @Nullable Object[] delta, Bot bot, Changer.@NotNull ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        boolean state = Boolean.parseBoolean(delta[0].toString());
        if (mode == Changer.ChangeMode.SET) {
            for (Guild guild : getExpr().getArray(e)) {
                guild = bot.findSimilarEntity(guild);
                LavaPlayer.getPlayer(bot, guild).setShouldPlayNext(state);
            }
        }
    }
}