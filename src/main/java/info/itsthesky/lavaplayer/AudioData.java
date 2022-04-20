package info.itsthesky.lavaplayer;

import info.itsthesky.disky.core.Bot;
import net.dv8tion.jda.api.entities.Guild;

public class AudioData {

    private final Bot bot;
    private final Guild guild;

    public AudioData(Bot bot, Guild guild) {
        this.bot = bot;
        this.guild = guild;
    }

    public Bot getBot() {
        return bot;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public String toString() {
        return getBot().getInstance().getSelfUser().getId() + ":" + getGuild().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioData audioData = (AudioData) o;
        return toString().equals(audioData.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
