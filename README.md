# LavaPlayer
The official LavaPlayer's DiSky module, to play &amp; search music on Youtube, Soundcloud and more.

# Audio Example

Don't forget to use the `loadMusicCommands` function in order to load the commands!

```applescript
function loadMusicCommands(guild: guild):
    set {_play} to new slash command named "play" with description "Search & play a track from YouTube or SoundCloud."
        
    set {_type} to new string option named "source" with description "Where to search the specified query."
    add new choice named "YouTube" with value "youtube" to choices of {_type}
    add new choice named "SoundCloud" with value "soundcloud" to choices of {_type}
    
    add new required string option named "query" with description "The query to search." to options of {_play}
    add {_type} to options of {_play}

    set {_pause} to new slash command named "pause" with description "Pause the current track."
    set {_stop} to new slash command named "stop" with description "Stop the current track & clear the queue."
    set {_resume} to new slash command named "resume" with description "Resume the current paused track."
    set {_skip} to new slash command named "skip" with description "Skip the current track and play the next one."
    set {_queue} to new slash command named "queue" with description "Check the queue, if any is active."
    set {_repeat} to new slash command named "repeat" with description "Either the current playing track should repeat."
    add new required boolean option named "repeat" with description "The repeat boolean state." to options of {_repeat}
    set {_continue} to new slash command named "continue" with description "Either the next track should be played when the current finish."
    add new required boolean option named "continue" with description "The continue boolean state." to options of {_continue}

    update {_play}, {_stop}, {_pause}, {_resume}, {_skip}, {_repeat}, {_continue} and {_queue} locally in {_guild}

function cut(text: text, chars: integer) :: text:
	set {_v::*} to split {_text} at ""
	if size of {_v::*} is smaller or equal to 0:
		return ""
	loop {_chars} times:
		add {_v::%loop-number%} to {_f::*}
	add ".", "." and "." to {_f::*}
	return join {_f::*} with ""

function trackEmbed(track: audiotrack, type: text) :: embedbuilder:
    make embed:
        set title of embed to "%{_type}%: %track title of {_track}%"
        add "`•` __Author:__ %track author of {_track}%" to {_l::*}
        add "`•` __Duration:__ %track duration of {_track}%" to {_l::*}
        add "`•` __Identifier:__ %track identifier of {_track}%" to {_l::*}
        add "" to {_l::*}
        add ":link: **Direct Link:** <%track url of {_track}%>" to {_l::*}
        set thumbnail of embed to track thumbnail of {_track}
        set description of embed to join {_l::*} with nl
        set embed color of embed to orange
    return last embed

on slash command:
    if event-string is "play":
        set {_query} to argument "query" as string
        set {_provider} to argument "source" as string
        if {_provider} is not set:
            set {_provider} to "youtube"
        if "%{_query}%" contain "?list=":
            set {_isList} to true
        else:
            set {_isList} to false
        
        search in ({_provider} parsed as audiosource) for {_query} and store the tracks in {_result::*}
        if {_result::*} is not set:
            reply with ":x: **Nothing found for this query in `%{_provider}%`!**"
            stop
        
        if voice channel of event-member is not set:
            reply with ":x: **You are not connected to any voice channel!**"
            stop
        
        connect the bot to voice channel of event-member
        
        if queue of event-guild is set:
            set {_t} to "Queued"
        else:
            set {_t} to "Now Playing"

        if {_isList} is true:
            play {_result::*} in event-guild
            make embed:
                set title of embed to "Queued Playlist (%size of {_result::*}% tracks):"
                set embed color of embed to orange
                loop {_result::*}:
                    add "`•` **%cut(track title of loop-value, 30)%** by *%track author of loop-value%*" to {_l::*}
                set description of embed to join {_l::*} with nl
                set footer of embed to "Executed by %event-member%"
            reply with last embed
        else:
            play {_result::1} in event-guild
            reply with trackEmbed({_result::1}, {_t})
    else if event-string is "continue":
        set {_c} to argument "continue" as boolean
        set continue state of event-guild to {_c}
        if {_c} is true:
            reply with ":sparkles: **Enabled the auto-continue system!**"
        else:
            reply with ":sparkles: **Disabled the auto-continue system!**"
    else if event-string is "repeat":
        set {_c} to argument "repeat" as boolean
        set repeating state of event-guild to {_c}
        if {_c} is true:
            reply with ":sparkles: **Repeating enabled!**"
        else:
            reply with ":sparkles: **Repeating disabled!**"
    else if event-string is "stop":
        stop queue in event-guild
        reply with ":wave: **Goodbye!**"
        disconnect bot from event-guild
    else if event-string is "queue":
        set {_queue::*} to queue of event-guild
        if {_queue::1} is set:
            make embed:
                set title of embed to "Queue of %event-guild% (%size of {_queue::*}% tracks):"
                set embed color of embed to orange
                set footer of embed to "Executed by %event-member%"
                set {_place} to 1
                loop {_queue::*}:
                    add "`%{_place}%)` **%cut(track title of loop-value, 30)%** by *%track author of loop-value%*" to {_l::*}
                set description of embed to join {_l::*} with nl
            reply with last embed
        else:
            reply with ":x: **There's no current queue in this guild!**"
    else if event-string is "skip":
        skip track of event-guild and store it in {_track}
        if {_track} is not set:
            reply with ":x: **There's no other track to play!**"
            stop
        reply with trackEmbed({_track}, "Now Playing")
```
