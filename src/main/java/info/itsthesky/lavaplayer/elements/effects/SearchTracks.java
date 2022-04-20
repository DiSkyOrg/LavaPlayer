package info.itsthesky.lavaplayer.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.track.*;
import info.itsthesky.disky.api.skript.WaiterEffect;
import info.itsthesky.lavaplayer.LavaPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SearchTracks extends WaiterEffect<AudioTrack[]> {

    private static final YoutubeSearchProvider youtubeSearch = new YoutubeSearchProvider();
    private static final YoutubeAudioSourceManager youtubeSource = new YoutubeAudioSourceManager();
    private static final SoundCloudAudioSourceManager soundCloud = SoundCloudAudioSourceManager.createDefault();

    static {
        Skript.registerEffect(
            SearchTracks.class,
            "search in %audiosource% [for] %string% and store (them|the tracks) in %-objects%"
        );
    }

    private Expression<String> exprQuery;
    private Expression<AudioSource> exprSource;

    @Override
    public boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprSource = (Expression<AudioSource>) expressions[0];
        exprQuery = (Expression<String>) expressions[1];
        setChangedVariable((Variable<AudioTrack[]>) expressions[2]);
        return true;
    }

    @Override
    public void runEffect(Event event) {
        final String query = parseSingle(exprQuery, event, null);
        final AudioSource source = parseSingle(exprSource, event, null);
        if (anyNull(query, source)) {
            restart();
            return;
        }
        final AudioItem item;
        switch (source) {
            case YOUTUBE:
                item = youtubeSearch.loadSearchResult(query, info -> new YoutubeAudioTrack(info, youtubeSource));
                break;
            case SOUNDCLOUD:
                item = soundCloud.loadItem(
                        LavaPlayer.getPlayerManager(),
                        new AudioReference("scsearch:" + query, null)
                );
                break;
            default:
                item = null;
                break;
        }
        if (item == null) {
            restart();
            return;
        }
        final AudioTrack[] tracks;
        if (item instanceof AudioPlaylist)
            tracks = ((AudioPlaylist) item).getTracks().toArray(new AudioTrack[0]);
        else if (item instanceof AudioTrack)
            tracks = new AudioTrack[] {(AudioTrack) item};
        else
            tracks = new AudioTrack[0];
        restart(tracks);
    }

    public enum AudioSource {
        YOUTUBE,
        SOUNDCLOUD,
        ;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "search for " + exprQuery.toString(e, debug);
    }
}
