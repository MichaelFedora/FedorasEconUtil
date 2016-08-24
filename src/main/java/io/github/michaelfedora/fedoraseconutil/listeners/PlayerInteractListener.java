package io.github.michaelfedora.fedoraseconutil.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

/**
 * Created by Michael on 8/20/2016.
 */
public class PlayerInteractListener {

    @Listener
    public void onPlayerPrimary(InteractBlockEvent.Primary event, @First Player player) {
        // Trade Forwards (a for b)
    }

    @Listener
    public void onPlayerSecondary(InteractBlockEvent.Secondary event, @First Player player) {
        // Trade Backwards (b for a)
    }
}
