package info.itsthesky.lavaplayer;

import ch.njol.skript.SkriptAddon;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.api.modules.DiSkyModule;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.lavaplayer.elements.effects.SearchTracks;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LavaPlayer extends DiSkyModule {

    private static AudioPlayerManager playerManager;
    private static LavaPlayer instance;
    private static HashMap<AudioData, AudioPlayerWrapper> players;

    public LavaPlayer(String name, String author, String version, File moduleJar) {
        super(name, author, version, moduleJar);
    }

    @Override
    public void init(DiSky disky, SkriptAddon addon) {
        instance = this;
        players = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();

        registerType(AudioTrack.class, "audiotrack", AudioTrack::getIdentifier);
        registerType(SearchTracks.AudioSource.class, "audiosource");
        registerType(AudioListener.TrackEventType.class, "trackeventtype");

        try {
            loadClasses("info.itsthesky.lavaplayer.elements");
        } catch (IOException e) {
            disky.getLogger().severe("Failed to load LavaPLayer Skript elements:");
            e.printStackTrace();
            return;
        }

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

    }

    public static @NotNull AudioPlayerWrapper getPlayer(@NotNull Guild guild) {
        return getPlayer(DiSky.getManager().fromJDA(guild.getJDA()), guild);
    }

    public static @NotNull AudioPlayerWrapper getPlayer(final Bot bot, final Guild guild) {
        final @Nullable AudioPlayerWrapper nullablePlayer = players.get(new AudioData(bot, guild));
        if (nullablePlayer != null)
            return nullablePlayer;
        final @NotNull AudioPlayerWrapper newPlayer = new AudioPlayerWrapper(getPlayerManager().createPlayer());
        newPlayer.addListener(new AudioListener(bot, guild));
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(newPlayer));
        players.put(new AudioData(bot, guild), newPlayer);
        return newPlayer;
    }

    public static AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public static LavaPlayer getInstance() {
        return instance;
    }
}
