package info.itsthesky.lavaplayer.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.api.skript.SpecificBotEffect;
import info.itsthesky.disky.api.skript.WaiterEffect;
import info.itsthesky.disky.core.Bot;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AudioWaiterEffect<T> extends SpecificBotEffect<T> {

    private static final String END_PATTERN = " (in|from|of) [the] [guild] %guild%";

    public static void register(
            Class<? extends AudioWaiterEffect> eff,
            String pattern,
            String typeName
    ) {
        pattern = pattern + END_PATTERN + (typeName != null ? "[and store (it|the "+typeName+") in %-object%]" : "");
        Skript.registerEffect(eff, pattern);
    }

    public static void register(
            Class<? extends AudioWaiterEffect> eff,
            String pattern
    ) {
        register(eff, pattern, null);
    }

    private Expression<Guild> exprGuild;

    @Override
    public void runEffect(@de.leonhard.storage.shaded.jetbrains.annotations.NotNull Event event, @de.leonhard.storage.shaded.jetbrains.annotations.NotNull Bot bot) {
        Guild guild = parseSingle(exprGuild, event, null);
        if (guild == null)
            return;
        execute(guild, bot);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return effectToString() + " in guild " + exprGuild.toString(e, debug);
    }

    public abstract String effectToString();

    public abstract void execute(@NotNull Guild guild, Bot bot);

    @Override
    public boolean initEffect(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        if (exprs.length > 1)
            setChangedVariable((Variable<T>) exprs[1]);
        return true;
    }
}
