package info.itsthesky.lavaplayer;

import com.sedmelluq.discord.lavaplayer.filter.PcmFilterFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AudioPlayerWrapper implements AudioPlayer {

    private final AudioPlayer wrapped;
    private final LinkedList<AudioTrack> queue;
    private boolean shouldPlayNext;
    private boolean isRepeating;

    public AudioPlayerWrapper(AudioPlayer wrapped) {
        this.wrapped = wrapped;
        this.queue = new LinkedList<>();
        this.shouldPlayNext = false;
        addListener(new Handler(this));
    }

    public LinkedList<AudioTrack> getQueue() {
        return queue;
    }

    public AudioTrack nextTrack() {
        if (queue.isEmpty())
            return null;
        AudioTrack track = queue.iterator().next();
        queue.remove(track);
        startTrack(track, false);
        return track;
    }

    public void playTracks(boolean force, AudioTrack... tracks) {
        if (tracks.length < 1)
            return;
        if (getPlayingTrack() != null)
            queue.addAll(Arrays.asList(tracks));
        else {
            startTrack(tracks[0], !force);
            final List<AudioTrack> list = new ArrayList<>(Arrays.asList(tracks));
            list.remove(0);
            if (tracks.length > 1)
                queue.addAll(list);
        }
    }

    public boolean shouldPlayNext() {
        return shouldPlayNext;
    }

    public void setShouldPlayNext(boolean shouldPlayNext) {
        this.shouldPlayNext = shouldPlayNext;
    }

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public boolean isRepeating() {
        return isRepeating;
    }

    public void clearQueue() {
        queue.clear();
    }

    public static class Handler extends AudioEventAdapter {

        private final AudioPlayerWrapper wrapper;

        public Handler(AudioPlayerWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
            if (endReason == AudioTrackEndReason.FINISHED) {
                if (wrapper.isRepeating())
                    wrapper.playTracks(true, track);
                else if (wrapper.shouldPlayNext())
                    wrapper.nextTrack();
            }
        }
    }

    @Override
    public AudioTrack getPlayingTrack() {
        return wrapped.getPlayingTrack();
    }

    @Override
    public void playTrack(AudioTrack track) {
        wrapped.playTrack(track);
    }

    @Override
    public boolean startTrack(AudioTrack track, boolean noInterrupt) {
        return wrapped.startTrack(track, noInterrupt);
    }

    @Override
    public void stopTrack() {
        wrapped.stopTrack();
    }

    @Override
    public int getVolume() {
        return wrapped.getVolume();
    }

    @Override
    public void setVolume(int volume) {
        wrapped.setVolume(volume);
    }

    @Override
    public void setFilterFactory(PcmFilterFactory factory) {
        wrapped.setFilterFactory(factory);
    }

    @Override
    public void setFrameBufferDuration(Integer duration) {
        wrapped.setFrameBufferDuration(duration);
    }

    @Override
    public boolean isPaused() {
        return wrapped.isPaused();
    }

    @Override
    public void setPaused(boolean value) {
        wrapped.setPaused(value);
    }

    @Override
    public void destroy() {
        wrapped.destroy();
    }

    @Override
    public void addListener(AudioEventListener listener) {
        wrapped.addListener(listener);
    }

    @Override
    public void removeListener(AudioEventListener listener) {
        wrapped.removeListener(listener);
    }

    @Override
    public void checkCleanup(long threshold) {
        wrapped.checkCleanup(threshold);
    }

    @Override
    public AudioFrame provide() {
        return wrapped.provide();
    }

    @Override
    public AudioFrame provide(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        return wrapped.provide(timeout, unit);
    }

    @Override
    public boolean provide(MutableAudioFrame targetFrame) {
        return wrapped.provide(targetFrame);
    }

    @Override
    public boolean provide(MutableAudioFrame targetFrame, long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        return wrapped.provide(targetFrame, timeout, unit);
    }
}
