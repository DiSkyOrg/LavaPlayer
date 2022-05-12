package info.itsthesky.lavaplayer.elements.getters;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.api.skript.EasyElement;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CurrentTrack extends SimpleExpression<AudioTrack> {

    public static void load() {
        Skript.registerExpression(
                CurrentTrack.class,
                AudioTrack.class,
                ExpressionType.PROPERTY,
                "[the] current track of [the] [bot] %bot% in [the] [guild] %guild%"
        );
    }

    private Expression<Bot> exprBot;
    private Expression<Guild> exprGuild;

    @Override
    protected AudioTrack @NotNull [] get(@NotNull Event e) {
        final Bot bot = EasyElement.parseSingle(exprBot, e, null);
        final Guild guild = EasyElement.parseSingle(exprGuild, e, null);
        if (EasyElement.anyNull(bot, guild))
            return new AudioTrack[0];
        return new AudioTrack[] {LavaPlayer.getPlayer(bot, guild).getPlayingTrack()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends AudioTrack> getReturnType() {
        return AudioTrack.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "current track of the bot "+exprBot.toString(e, debug) + " in the guild "+exprGuild.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprBot = (Expression<Bot>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        return true;
    }
}
