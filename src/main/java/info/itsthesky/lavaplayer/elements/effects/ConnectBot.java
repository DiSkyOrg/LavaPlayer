package info.itsthesky.lavaplayer.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import de.leonhard.storage.shaded.jetbrains.annotations.NotNull;
import info.itsthesky.disky.api.skript.SpecificBotEffect;
import info.itsthesky.disky.core.Bot;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Connect / Disconnect Bot")
@Description({"Connect or disconnect a bot to a specific audio channel (or disconnect it from the current one).",
        "The bot must have the required permissions to connect to the channel.",
        "If using the disconnect pattern, only the guild will be required."
})
@Examples({"connect the bot to voice channel with id \"000\"",
"disconnect the bot from event-guild"})
public class ConnectBot extends SpecificBotEffect {

    static {
        System.out.println("Registering ...");
        Skript.registerEffect(
            ConnectBot.class,
            "connect [the] [bot] (in|to) [to] [the] [audio] [channel] %audiochannel%",
                "disconnect [the] [bot] (from|from) [the] [guild] %guild%"
        );
    }

    private Expression<AudioChannel> exprChannel;
    private Expression<Guild> exprGuild;
    private boolean disconnect;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        disconnect = i == 1;
        if (disconnect)
            exprGuild = expressions[0];
        else
            exprChannel = expressions[0];
        return true;
    }

    @Override
    public void runEffect(@NotNull Event e, @NotNull Bot bot) {
        if (disconnect) {
            final Guild guild = parseSingle(exprGuild, e, null);
            if (anyNull(guild)) {
                restart();
                return;
            }
            guild.getAudioManager().closeAudioConnection();
            restart();
        } else {
            final AudioChannel channel = parseSingle(exprChannel, e, null);
            if (anyNull(channel)) {
                restart();
                return;
            }
            final Guild target = bot.findSimilarEntity(channel.getGuild());
            target.getAudioManager().openAudioConnection(channel);
            restart();
        }
    }

    @Override
    public @org.jetbrains.annotations.NotNull String toString(@Nullable Event e, boolean debug) {
        return disconnect ? "disconnect the bot from " + exprGuild.toString(e, debug) : "connect the bot to " + exprChannel.toString(e, debug);
    }
}
