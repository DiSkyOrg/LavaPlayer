package info.itsthesky.lavaplayer.elements.getters;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.api.changers.ChangeableSimplePropertyExpression;
import info.itsthesky.disky.elements.components.properties.SimpleChangeableProperty;
import info.itsthesky.disky.elements.properties.guilds.MultipleGuildProperty;
import info.itsthesky.lavaplayer.LavaPlayer;
import net.dv8tion.jda.api.entities.Guild;

public class GuildQueue extends MultipleGuildProperty<AudioTrack> {

    public static void load() {
        register(
                GuildQueue.class,
                AudioTrack.class,
                "[audio] queue"
        );
    }

    @Override
    public AudioTrack[] converting(Guild guild) {
        return LavaPlayer.getPlayer(guild).getQueue().toArray(new AudioTrack[0]);
    }
}
