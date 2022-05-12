package info.itsthesky.lavaplayer.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.api.skript.WaiterEffect;
import info.itsthesky.lavaplayer.LavaPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Load Local Track")
@Description({"Loads a local track from the files of your server.",
        "The supported file types are:",
        "- MP3",
        "- FLAC",
        "- WAV",
        "- Matroska/WebM (AAC, Opus or Vorbis codecs)",
        "- MP4/M4A (AAC codec)",
        "- OGG streams (Opus, Vorbis and FLAC codecs)",
        "- AAC streams",
        "- Stream playlists (M3U and PLS)",
})
public class LoadLocal extends WaiterEffect<AudioTrack> {

    public static void load() {
        Skript.registerEffect(
                LoadLocal.class,
                "load local track [from] [the] [file] %string% and store (it|the track) in %-objects%"
        );
    }

    private Expression<String> exprPath;

    @Override
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprPath = (Expression<String>) expressions[0];
        setChangedVariable((Variable<AudioTrack>) expressions[1]);
        return true;
    }

    @Override
    public void runEffect(Event event) {
        final String path = parseSingle(exprPath, event, null);
        if (anyNull(path)) {
            restart();
            return;
        }
        LavaPlayer.getPlayerManager().loadItem(path, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                restart(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                Skript.error("Playlist fom local files are not yet handled.");
                restart();
            }

            @Override
            public void noMatches() {
                Skript.error("No local file track found at " + path);
                restart();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                Skript.error("An internal error occured while loading '" + path + "': ");
                exception.printStackTrace();
                restart();
            }
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "load local track from the file " + exprPath.toString(e, debug);
    }
}
