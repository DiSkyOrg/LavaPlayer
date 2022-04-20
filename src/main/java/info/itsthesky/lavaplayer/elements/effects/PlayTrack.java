package info.itsthesky.lavaplayer.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.leonhard.storage.shaded.jetbrains.annotations.NotNull;
import info.itsthesky.disky.api.skript.SpecificBotEffect;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.AudioPlayerWrapper;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Play Tracks in Guild")
@Description({"Play a specific **loaded** track (through search or local) in a specific guild.",
"Even if the bot is not yet connected, it will still start playing and the track will continue.",
"Don't forget to use the `Connect` effect before!",
"If the bot was already playing tracks, then the new one will simply be queued."})
@Examples("play {_track} in event-guild")
public class PlayTrack extends SpecificBotEffect {

    static {
        Skript.registerEffect(PlayTrack.class,
                "[force] (play|start) [the] [track[s]] %audiotracks% (in|to) [the] [guild] %guilds%"
        );
    }

    private Expression<Guild> exprGuild;
    private Expression<AudioTrack> exprTracks;
    private boolean force;

    @Override
    public boolean initEffect(Expression[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprTracks = expressions[0];
        exprGuild = expressions[1];
        force = parseResult.expr.startsWith("force");
        return true;
    }

    @Override
    public void runEffect(@NotNull Event event, @NotNull Bot bot) {
        final Guild guild = parseSingle(exprGuild, event, null);
        final AudioTrack[] tracks = parseList(exprTracks, event, null);
        if (anyNull(guild, tracks))
            return;
        final AudioPlayerWrapper player = LavaPlayer.getPlayer(bot, guild);
        player.playTracks(force, tracks);
        restart();
    }

    @Override
    public @org.jetbrains.annotations.NotNull String toString(@Nullable Event e, boolean debug) {
        return "play tracks "+exprTracks.toString(e, debug)+" in guild " + exprGuild.toString(e, debug);
    }
}
